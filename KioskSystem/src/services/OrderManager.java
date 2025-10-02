package services;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import objects.Order;
import objects.OrderItem;

public class OrderManager {
    
    public static int createNewOrder(double totalAmount, String paymentMethod) {
        Connection conn = DatabaseConnection.getConnection();

        try {
            System.out.println("Creating order with total: " + totalAmount + ", payment: " + paymentMethod);

            String sql = "INSERT INTO customer_order_tb (order_datetime, total_amount, payment_method_id) VALUES (NOW(), ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setDouble(1, totalAmount);
            
            
            int paymentMethodId = getPaymentMethodId(paymentMethod);
            pstmt.setInt(2, paymentMethodId);

            int affectedRows = pstmt.executeUpdate();
            System.out.println("Rows affected: " + affectedRows);

            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    System.out.println("Generated order ID: " + orderId);
                    return orderId;
                } else {
                    System.out.println("No generated keys returned");
                }
            }
            return -1;
        } catch (SQLException e) {
            System.err.println("SQL Error creating new order: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            System.err.println("General Error creating new order: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    
    private static int getPaymentMethodId(String paymentMethod) {
        
        switch (paymentMethod.toLowerCase()) {
            case "cash": 
                return 1;
            case "gcash": 
            case "online":
                return 2;
            default: 
                return 1; 
        }
    }
    
    public static String getPaymentMethodName(int paymentMethodId) {
        
        switch (paymentMethodId) {
            case 1: 
                return "Cash";
            case 2: 
                return "GCash";
            default: 
                return "Cash";
        }
    }
    
    
    public static String getPaymentMethodFromDB(int paymentMethodId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT method_name FROM payment_method_tb WHERE payment_method_id = ? AND is_available = 1";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, paymentMethodId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("method_name");
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment method from DB: " + e.getMessage());
        }
        
        
        return getPaymentMethodName(paymentMethodId);
    }
    
    
    public static List<String> getAvailablePaymentMethods() {
        List<String> paymentMethods = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT method_name FROM payment_method_tb WHERE is_available = 1 ORDER BY payment_method_id";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                paymentMethods.add(rs.getString("method_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting available payment methods: " + e.getMessage());
            
            paymentMethods.add("Cash");
            paymentMethods.add("GCash");
        }
        return paymentMethods;
    }
    
    public static boolean isPaymentMethodAvailable(String paymentMethod) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT is_available FROM payment_method_tb WHERE method_name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, paymentMethod);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("is_available");
            }
        } catch (SQLException e) {
            System.err.println("Error checking payment method availability: " + e.getMessage());
        }

        
        return true;
    }
    
    public static boolean addOrderItem(int orderId, int productId, int quantity) {
        Connection conn = DatabaseConnection.getConnection();

        try {
            
            double productPrice = 0.0;
            String priceSql = "SELECT price FROM product_tb WHERE product_id = ?";
            try (PreparedStatement priceStmt = conn.prepareStatement(priceSql)) {
                priceStmt.setInt(1, productId);
                ResultSet rs = priceStmt.executeQuery();
                if (rs.next()) {
                    productPrice = rs.getDouble("price");
                } else {
                    System.err.println("Product not found with ID: " + productId);
                    return false;
                }
            }

            
            double subtotal = productPrice * quantity;

            
            String sql = "INSERT INTO order_item_tb (order_id, product_id, quantity, subtotal) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, orderId);
                pstmt.setInt(2, productId);
                pstmt.setInt(3, quantity);
                pstmt.setDouble(4, subtotal);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error adding order item: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<OrderItem> getOrderItems(int orderId) {
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
    
    public static boolean updateOrderTotal(int orderId) {
        String sql = "UPDATE customer_order_tb SET total_amount = " +
                     "(SELECT COALESCE(SUM(subtotal), 0) FROM order_item_tb WHERE order_id = ?) " +
                     "WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            pstmt.setInt(2, orderId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}