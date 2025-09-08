package backend;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String role;
    private Timestamp createdAt;
    
    // Correct constructor with all fields including password
    public User(int id, String firstName, String lastName, String username, String password, String role, Timestamp createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }
    
    // Getters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public Timestamp getCreatedAt() { return createdAt; }
    public String getFullName() { return firstName + " " + lastName; }
    
    // Static method to get user by username
    public static User get(String username) {
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
}