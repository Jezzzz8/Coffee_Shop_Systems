package services;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import objects.Product;

public class ProductManager {
    
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_tb ORDER BY product_name";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_filename"),
                    rs.getBoolean("is_available")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products: " + e.getMessage());
        }
        return products;
    }
    
    public static Product getProductByName(String productName) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_tb WHERE LOWER(product_name) = LOWER(?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_filename"),
                    rs.getBoolean("is_available")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting product by name: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean addProduct(Product product) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO product_tb (product_name, price, description, image_filename, is_available) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getDescription());
            pstmt.setString(4, product.getImageFilename());
            pstmt.setBoolean(5, product.isAvailable());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateProduct(Product product) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE product_tb SET product_name = ?, price = ?, description = ?, image_filename = ?, is_available = ? WHERE product_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getDescription());
            pstmt.setString(4, product.getImageFilename());
            pstmt.setBoolean(5, product.isAvailable());
            pstmt.setInt(6, product.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteProduct(int productId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM product_tb WHERE product_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean hardDeleteProduct(int productId) {
        if (hasExistingOrders(productId)) {
            System.err.println("Cannot delete product with ID " + productId + " - product has existing orders");
            return false;
        }

        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM product_tb WHERE product_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error hard deleting product: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Product> getAllAvailableProducts() {
        List<Product> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT product_id, product_name, price, description, " +
             "image_filename, is_available FROM product_tb WHERE is_available = 1";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_filename"),
                    true 
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting available products: " + e.getMessage());
        }
        return products;
    }
    
    public static List<Product> getBestSellingProducts(int limit) {
        List<Product> bestProducts = new ArrayList<>();

        try {
            String sql = "SELECT p.product_id, p.product_name, p.price, p.description, " +
                        "p.image_filename, p.is_available, " +
                        "COALESCE(SUM(oi.quantity), 0) AS total_sold " +
                        "FROM product_tb AS p " +
                        "LEFT JOIN order_item_tb oi ON p.product_id = oi.product_id " +
                        "GROUP BY p.product_id, p.product_name, p.price, p.description, " +
                        "p.image_filename, p.is_available " +
                        "ORDER BY total_sold DESC, p.product_name " +
                        "LIMIT ?";

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_filename"),
                    rs.getBoolean("is_available")
                );
                bestProducts.add(product);
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            System.out.println("Error loading best selling products: " + e.getMessage());
            e.printStackTrace();
        }

        return bestProducts;
    }
    
    public static Product getProductById(int productId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_tb WHERE product_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_filename"),
                    rs.getBoolean("is_available")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting product by ID: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean updateProductAvailability(int productId, boolean isAvailable) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE product_tb SET is_available = ? WHERE product_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isAvailable);
            pstmt.setInt(2, productId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product availability: " + e.getMessage());
        }
        return false;
    }
    
    public static int getTotalProducts() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(*) as count FROM product_tb";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total products: " + e.getMessage());
        }
        return 0;
    }
    
    public static int getAvailableProductsCount() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(*) as count FROM product_tb WHERE is_available = 1";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting available products count: " + e.getMessage());
        }
        return 0;
    }
    
    public static boolean hasExistingOrders(int productId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(*) as order_count FROM order_item_tb WHERE product_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("order_count") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking product orders: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}