package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesViewManager {
    
    public static List<SalesView> getOrderDetails() {
        List<SalesView> orders = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM order_details_vw ORDER BY order_date DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SalesView order = new SalesView(
                    rs.getInt("order_id"),
                    rs.getTimestamp("order_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_method"),
                    rs.getString("status"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("contact_no"),
                    rs.getString("email"),
                    rs.getString("cashier_name"),
                    rs.getString("shift_schedule")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting order details: " + e.getMessage());
        }
        return orders;
    }
}