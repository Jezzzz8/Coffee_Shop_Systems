package services;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import objects.OrderItem;
import objects.OrderSlip;
import objects.Receipt;

public class OrderSlipManager {
    
    public static OrderSlip generateOrderSlip(int orderId) {
        
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM customer_order_tb WHERE order_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Timestamp orderDateTime = rs.getTimestamp("order_datetime");
                double totalAmount = rs.getDouble("total_amount");
                int paymentMethodId = rs.getInt("payment_method_id");
                
                
                String paymentMethod = OrderManager.getPaymentMethodFromDB(paymentMethodId);
                
                List<OrderItem> items = OrderManager.getOrderItems(orderId);
                
                return new OrderSlip(
                    orderId,
                    orderDateTime,
                    items,
                    totalAmount,
                    paymentMethod,
                    "Pending"
                );
            }
        } catch (SQLException e) {
            System.err.println("Error generating order slip: " + e.getMessage());
        }
        return null;
    }
    
    
    
    public static Receipt generateReceipt(int orderId, double paymentReceived, double change) {
        OrderSlip orderSlip = generateOrderSlip(orderId);
        if (orderSlip == null) return null;
        
        return new Receipt(
            orderId,
            orderSlip.getOrderDateTime().toLocalDateTime(),
            LocalDateTime.now(),
            orderSlip.getItems(),
            orderSlip.getTotalAmount(),
            paymentReceived,
            change,
            orderSlip.getPaymentMethod()
        );
    }
}