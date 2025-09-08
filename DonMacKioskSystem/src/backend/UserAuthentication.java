package backend;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthentication {
    
    public static class AuthResult {
        public final boolean success;
        public final User user;
        public final String errorMessage;
        public final String errorType;

        public AuthResult(boolean success, User user, String errorMessage, String errorType) {
            this.success = success;
            this.user = user;
            this.errorMessage = errorMessage;
            this.errorType = errorType;
        }
        
        public boolean isSuccess() { 
            return success; 
        }
        
        public User getUser() { 
            return user; 
        }
        
        public String getErrorMessage() { 
            return errorMessage; 
        }
        
        public String getErrorType() { 
            return errorType; 
        }
    }
    
    public static boolean addUser(String firstName, String lastName, String username, String password, String role) {
        try {
            String hashedPassword = hashPassword(password);
            String sql = "INSERT INTO `users` (`first_name`, `last_name`, `username`, `password`, `role`) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, username);
            pstmt.setString(4, hashedPassword);
            pstmt.setString(5, role);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean authenticate(String username, String password) {
        try {
            String sql = "SELECT id, first_name, last_name, username, password, role, created_at FROM users WHERE username = ?";
            PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String hashedInputPassword = hashPassword(password);
                return hashedInputPassword.equals(storedPassword);
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void verifyAndFixAllPasswords() {
        System.out.println("=== VERIFYING ALL PASSWORDS ===");
        List<User> users = getAllUsers();

        for (User user : users) {
            String storedPassword = user.getPassword();
            String username = user.getUsername();

            System.out.println("User: " + username + ", Stored: " + storedPassword);

            // Check if password is already hashed (Base64 encoded SHA-256 should be 44 chars)
            if (storedPassword != null && storedPassword.length() != 44) {
                System.out.println("Password for " + username + " appears to be plain text. Hashing...");

                // Re-hash the password (assuming the stored value is the actual password)
                String hashedPassword = hashPassword(storedPassword);

                // Update in database
                try {
                    String sql = "UPDATE users SET password = ? WHERE username = ?";
                    PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql);
                    pstmt.setString(1, hashedPassword);
                    pstmt.setString(2, username);

                    int rowsAffected = pstmt.executeUpdate();
                    System.out.println("Updated " + username + " password. Rows affected: " + rowsAffected);
                } catch (SQLException e) {
                    System.out.println("Error updating password for " + username + ": " + e.getMessage());
                }
            } else {
                System.out.println("Password for " + username + " appears to be already hashed.");
            }
        }
        System.out.println("=== PASSWORD VERIFICATION COMPLETE ===");
    }
    
    public static String getUserFullName(String username) {
        User user = UserAuthentication.getUserByUsername(username);
        return user != null ? user.getFullName() : "User";
    }
    
    public static User getUserByUsername(String username) {
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("username"),
                    rs.getString("password"),  // password parameter
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static User getUserById(int userId) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean isAdmin(String username) {
        User user = UserAuthentication.getUserByUsername(username);
        return user != null && "admin".equalsIgnoreCase(user.getRole());
    }
    
    public static boolean isCashier(String username) {
        User user = UserAuthentication.getUserByUsername(username);
        return user != null && "cashier".equalsIgnoreCase(user.getRole());
    }
    
    public static boolean isCustomer(String username) {
        User user = UserAuthentication.getUserByUsername(username);
        return user != null && "customer".equalsIgnoreCase(user.getRole());
    }
    
    public static boolean isClerk(String username) {
        User user = UserAuthentication.getUserByUsername(username);
        return user != null && "clerk".equalsIgnoreCase(user.getRole());
    }

    public static boolean isManager(String username) {
        User user = UserAuthentication.getUserByUsername(username);
        return user != null && "manager".equalsIgnoreCase(user.getRole());
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("username"),
                    rs.getString("password"),  // Include password
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting users: " + e.getMessage());
        }
        return users;
    }
    
    public static boolean deleteUser(String username) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        return false;
    }
    
    public static String getUserPasswordForDebug(String username) {
        try {
            String sql = "SELECT password FROM users WHERE username = ?";
            PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Add method to check if username already exists
    public static boolean usernameExists(String username) {
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static AuthResult authenticateWithDetails(String username, String password) {
        try {
            // Check if user exists first
            if (!usernameExists(username)) {
                return new AuthResult(false, null, "Username not found", "USER_NOT_FOUND");
            }

            // Check if account is active (assuming you have an is_active column)
            if (!isAccountActive(username)) {
                return new AuthResult(false, null, "Account is inactive", "INACTIVE_ACCOUNT");
            }

            // Verify password - ADD created_at TO THE SELECT CLAUSE
            String sql = "SELECT id, first_name, last_name, username, password, role, created_at FROM users WHERE username = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String inputHash = hashPassword(password);

                    if (storedHash != null && storedHash.equals(inputHash)) {
                        // Password matches - create user object
                        User user = new User(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getTimestamp("created_at")  // This line was causing the error
                        );

                        return new AuthResult(true, user, null, null);
                    } else {
                        // Password doesn't match
                        return new AuthResult(false, null, "Invalid password", "INVALID_PASSWORD");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new AuthResult(false, null, "Database error: " + e.getMessage(), "DATABASE_ERROR");
        }

        return new AuthResult(false, null, "Authentication failed", "UNKNOWN_ERROR");
    }

    // Helper methods for account status checking
    private static boolean isAccountActive(String username) {
        return true; // All accounts are active since no is_active column exists
    }

    private static boolean isAccountLocked(String username) {
        // Implement account lockout logic if needed
        return false; // Default implementation
    }

    private static void incrementFailedLoginAttempts(String username) {
        // Implement failed login tracking if needed
    }

    private static void resetFailedLoginAttempts(String username) {
        // Reset failed attempts on successful login
    }
}