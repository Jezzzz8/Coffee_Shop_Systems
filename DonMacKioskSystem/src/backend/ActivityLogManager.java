package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogManager {
    
    public static boolean logActivity(String username, String activityType, String description) {
        User user = UserAuthentication.getUserByUsername(username);
        if (user != null) {
            return addActivityLog(user.getId(), activityType, description);
        }
        return false;
    }
    
    public static List<ActivityLog> getActivityLogs() {
        List<ActivityLog> logs = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT al.log_id, al.user_id, u.username, al.activity_type, al.description, al.timestamp " +
                   "FROM activity_log_tb al " +
                   "JOIN users u ON al.user_id = u.id " +
                   "ORDER BY al.timestamp DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ActivityLog log = new ActivityLog(
                    rs.getInt("log_id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("activity_type"),
                    rs.getString("description"),
                    rs.getTimestamp("timestamp")
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error getting activity logs: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }
    
    public static boolean addActivityLog(int userId, String activityType, String description) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO activity_log_tb (user_id, activity_type, description) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, activityType);
            pstmt.setString(3, description);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding activity log: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean addActivityLog(String username, String activityType, String description) {
        User user = UserAuthentication.getUserByUsername(username);
        if (user != null) {
            return addActivityLog(user.getId(), activityType, description);
        }
        return false;
    }
    
    public static boolean deleteActivityLog(int logId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM activity_log_tb WHERE log_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, logId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting activity log: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean clearAllActivityLogs() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM activity_log_tb";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error clearing activity logs: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<ActivityLog> getActivityLogsByUser(int userId) {
        List<ActivityLog> logs = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT al.log_id, al.user_id, u.username, al.activity_type, al.description, al.timestamp " +
                   "FROM activity_log_tb al " +
                   "JOIN users u ON al.user_id = u.id " +
                   "WHERE al.user_id = ? " +
                   "ORDER BY al.timestamp DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ActivityLog log = new ActivityLog(
                    rs.getInt("log_id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("activity_type"),
                    rs.getString("description"),
                    rs.getTimestamp("timestamp")
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error getting activity logs by user: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }
    
    public static List<ActivityLog> getActivityLogsByType(String activityType) {
        List<ActivityLog> logs = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT al.log_id, al.user_id, u.username, al.activity_type, al.description, al.timestamp " +
                   "FROM activity_log_tb al " +
                   "JOIN users u ON al.user_id = u.id " +
                   "WHERE al.activity_type = ? " +
                   "ORDER BY al.timestamp DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, activityType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ActivityLog log = new ActivityLog(
                    rs.getInt("log_id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("activity_type"),
                    rs.getString("description"),
                    rs.getTimestamp("timestamp")
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error getting activity logs by type: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }
    
    public static int getTotalActivityLogs() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(*) as count FROM activity_log_tb";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total activity logs: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}