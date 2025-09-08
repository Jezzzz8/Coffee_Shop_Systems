// InventoryManager.java
package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    
    
    public static boolean createInventory(int productId, int initialStock) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO inventory_tb (product_id, stock_quantity, last_updated) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.setInt(2, initialStock);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating inventory: " + e.getMessage());
        }
        return false;
    }
    
    public static List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM inventory_tb ORDER BY product_id";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Inventory inventory = new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getInt("product_id"),
                    rs.getInt("stock_quantity"),
                    rs.getTimestamp("last_updated")
                );
                inventoryList.add(inventory);
            }
        } catch (SQLException e) {
            System.err.println("Error getting inventory: " + e.getMessage());
        }
        return inventoryList;
    }
    
    public static Inventory getInventoryByProductId(int productId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM inventory_tb WHERE product_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getInt("product_id"),
                    rs.getInt("stock_quantity"),
                    rs.getTimestamp("last_updated")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting inventory by product ID: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean updateStock(int productId, int quantityChange) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE inventory_tb SET stock_quantity = stock_quantity + ? WHERE product_id = ?";
            
            int currentStock = getStockQuantity(productId);
            int newStock = currentStock + quantityChange;
            
            if (newStock < 0) {
                System.out.println("Cannot update stock: would result in negative quantity");
                return false;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, quantityChange);
                pstmt.setInt(2, productId);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.err.println("Error updating stock: " + e.getMessage());
            }

            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean safeUpdateStock(int productId, int quantityChange) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE inventory_tb SET stock_quantity = GREATEST(0, stock_quantity + ?) WHERE product_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantityChange);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
        }
        return false;
    }
    
    public static List<Inventory> getLowStockProducts(int threshold) {
        List<Inventory> lowStockProducts = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM inventory_tb WHERE stock_quantity <= ? ORDER BY stock_quantity ASC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, threshold);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Inventory inventory = new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getInt("product_id"),
                    rs.getInt("stock_quantity"),
                    rs.getTimestamp("last_updated")
                );
                lowStockProducts.add(inventory);
            }
        } catch (SQLException e) {
            System.err.println("Error getting low stock products: " + e.getMessage());
        }
        return lowStockProducts;
    }
    
    public static int getStockQuantity(int productId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT stock_quantity FROM inventory_tb WHERE product_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("stock_quantity");
            }
        } catch (SQLException e) {
            System.err.println("Error getting stock quantity: " + e.getMessage());
        }
        return 0;
    }
    
    public static List<ProductInventoryView> getProductInventoryDetails() {
        return ProductInventoryViewManager.getAllProductInventory();
    }
    
    public static boolean logInventoryTransaction(int productId, int quantityChange, String transactionType, String notes) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO inventory_transactions (product_id, quantity_change, transaction_type, notes, transaction_date) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.setInt(2, quantityChange);
            pstmt.setString(3, transactionType);
            pstmt.setString(4, notes);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error logging transaction: " + e.getMessage());
        }
        return false;
    }
}