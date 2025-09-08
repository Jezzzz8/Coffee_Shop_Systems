// CashierManager.java
package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CashierManager {
    
    public static List<Cashier> getAllCashiers() {
        List<Cashier> cashiers = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM cashier_tb ORDER BY cashier_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Cashier cashier = new Cashier(
                    rs.getInt("cashier_id"),
                    rs.getString("cashier_name"),
                    rs.getString("shift_schedule"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at")
                );
                cashiers.add(cashier);
            }
        } catch (SQLException e) {
            System.err.println("Error getting cashiers: " + e.getMessage());
        }
        return cashiers;
    }
    
    public static List<Cashier> getActiveCashiers() {
        List<Cashier> cashiers = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM cashier_tb WHERE is_active = true ORDER BY cashier_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Cashier cashier = new Cashier(
                    rs.getInt("cashier_id"),
                    rs.getString("cashier_name"),
                    rs.getString("shift_schedule"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at")
                );
                cashiers.add(cashier);
            }
        } catch (SQLException e) {
            System.err.println("Error getting active cashiers: " + e.getMessage());
        }
        return cashiers;
    }
    
    public static boolean addCashier(String cashierName, String shiftSchedule) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO cashier_tb (cashier_name, shift_schedule) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cashierName);
            pstmt.setString(2, shiftSchedule);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding cashier: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean updateCashierStatus(int cashierId, boolean isActive) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE cashier_tb SET is_active = ? WHERE cashier_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isActive);
            pstmt.setInt(2, cashierId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating cashier status: " + e.getMessage());
        }
        return false;
    }
    
        public static int getCashierIdFromUserId(int userId) {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT cashier_id FROM cashier_tb WHERE user_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    return rs.getInt("cashier_id");
                }
            } catch (SQLException e) {
                System.err.println("Error getting cashier ID: " + e.getMessage());
            }
            return -1; // Or handle this case appropriately
        }
    }