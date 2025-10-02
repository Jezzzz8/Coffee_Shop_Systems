package services;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    
    public static List<Product> searchProductsByName(String searchTerm) {
        List<Product> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_tb WHERE product_name LIKE ? AND is_available = 1 ORDER BY product_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
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
            System.err.println("Error searching products: " + e.getMessage());
        }
        return products;
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
}