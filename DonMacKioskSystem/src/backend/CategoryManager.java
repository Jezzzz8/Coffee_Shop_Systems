package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    
    public static List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM category_tb ORDER BY category_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Category category = new Category(
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("created_by"),
                    rs.getTimestamp("created_at")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error getting categories: " + e.getMessage());
        }
        return categories;
    }
    
    public static boolean addCategory(String name, String createdBy) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO category_tb (category_name, created_by) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, createdBy);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding category: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean deleteCategory(String name) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM category_tb WHERE category_name = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
        }
        return false;
    }
    
    public static int getCategoryCount() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(*) as count FROM category_tb";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting category count: " + e.getMessage());
        }
        return 0;
    }
    
    public static List<String> getCategoryNames() {
        List<String> categoryNames = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT category_name FROM category_tb ORDER BY category_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                categoryNames.add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting category names: " + e.getMessage());
        }
        return categoryNames;
    }
}