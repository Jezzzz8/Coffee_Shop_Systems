package objects;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Admin {
    private int adminId;
    private String username;
    private String password;
    private LocalDate createdAt;
    private LocalDateTime lastLogin;
    
    public Admin() {}
    
    public Admin(int adminId, String username, String password, 
                    LocalDate createdAt, LocalDateTime lastLogin) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }
    
    // Getters and setters
    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
}
