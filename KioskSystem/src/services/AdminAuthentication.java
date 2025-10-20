package services;

import database.DatabaseConnection;
import objects.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AdminAuthentication {
    
    public static class AuthResult {
        public final boolean success;
        public final String errorMessage;
        public final String errorType;
        public final Admin admin;
            
        public AuthResult(boolean success, String errorMessage, String errorType, Admin admin) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.errorType = errorType;
            this.admin = admin;
        }
        
        public boolean isSuccess() { 
            return success; 
        }
        
        public String getErrorMessage() { 
            return errorMessage; 
        }
        
        public String getErrorType() { 
            return errorType; 
        }
        
        public Admin getAdmin() {
            return admin;
        }
    }
    
    public static boolean authenticate(String username, String password) {
        AuthResult result = authenticateWithDetails(username, password);
        return result.isSuccess();
    }
    
    public static AuthResult authenticateWithDetails(String username, String password) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM admin_users_tb WHERE username = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.next()) {
                return new AuthResult(false, "Invalid username", "USERNAME_ERROR", null);
            }
            
            String storedPassword = rs.getString("password");
            
            if (!storedPassword.equals(password)) {
                return new AuthResult(false, "Invalid password", "PASSWORD_ERROR", null);
            }
            
            // Create Admin object
            Admin admin = new Admin(
                rs.getInt("admin_id"),
                rs.getString("username"),
                storedPassword,
                rs.getDate("created_at").toLocalDate(),
                rs.getTimestamp("last_login") != null ? 
                    rs.getTimestamp("last_login").toLocalDateTime() : null
            );
            
            updateLastLogin(rs.getInt("admin_id"));
            
            return new AuthResult(true, null, null, admin);
            
        } catch (SQLException e) {
            System.err.println("Error authenticating admin: " + e.getMessage());
            return new AuthResult(false, "Database error: " + e.getMessage(), "DATABASE_ERROR", null);
        }
    }
    
    private static void updateLastLogin(int adminId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE admin_users_tb SET last_login = CURRENT_TIMESTAMP WHERE admin_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
        }
    }
    
    public static Admin getAdminByUsername(String username) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM admin_users_tb WHERE username = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Admin(
                    rs.getInt("admin_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getDate("created_at").toLocalDate(),
                    rs.getTimestamp("last_login") != null ? 
                        rs.getTimestamp("last_login").toLocalDateTime() : null
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting admin by username: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean changePassword(String username, String newPassword) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE admin_users_tb SET password = ? WHERE username = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error changing password: " + e.getMessage());
            return false;
        }
    }
}