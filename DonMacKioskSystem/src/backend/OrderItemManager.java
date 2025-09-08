package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemManager {
    
    public static List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM order_item_tb WHERE order_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                OrderItem item = new OrderItem(
                    rs.getInt("order_item_id"),
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("subtotal")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error getting order items: " + e.getMessage());
        }
        return items;
    }
    
    public static boolean updateOrderItemQuantity(int orderItemId, int quantity) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE order_item_tb SET quantity = ?, subtotal = (SELECT price FROM product_tb WHERE product_id = (SELECT product_id FROM order_item_tb WHERE order_item_id = ?)) * ? WHERE order_item_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, orderItemId);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, orderItemId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order item quantity: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean deleteOrderItem(int orderItemId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM order_item_tb WHERE order_item_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderItemId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting order item: " + e.getMessage());
        }
        return false;
    }
}