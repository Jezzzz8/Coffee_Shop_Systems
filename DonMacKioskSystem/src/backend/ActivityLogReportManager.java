package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogReportManager {
    
    public static List<ActivityLogReport> getAllActivityLogs() {
        List<ActivityLogReport> logs = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT user_id, activity_type, description, timestamp FROM activity_log_tb ORDER BY timestamp DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ActivityLogReport log = new ActivityLogReport(
                    rs.getInt("user_id"),
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
    
    public static List<ActivityLogReport> getActivityLogsByUser(int userId) {
        List<ActivityLogReport> logs = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT user_id, activity_type, description, timestamp FROM activity_log_tb WHERE user_id = ? ORDER BY timestamp DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ActivityLogReport log = new ActivityLogReport(
                    rs.getInt("user_id"),
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
    
    public static List<ActivityLogReport> getActivityLogsByDateRange(Timestamp startDate, Timestamp endDate) {
        List<ActivityLogReport> logs = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT user_id, activity_type, description, timestamp FROM activity_log_tb " +
                   "WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, startDate);
            pstmt.setTimestamp(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ActivityLogReport log = new ActivityLogReport(
                    rs.getInt("user_id"),
                    rs.getString("activity_type"),
                    rs.getString("description"),
                    rs.getTimestamp("timestamp")
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error getting activity logs by date range: " + e.getMessage());
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
}