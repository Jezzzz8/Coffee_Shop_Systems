package services;

import database.DatabaseConnection;
import objects.Payment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentManager {
    public static List<Payment> getAllPaymentMethods() {
        List<Payment> paymentMethods = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM payment_method_tb ORDER BY payment_method_id";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment(
                    rs.getInt("payment_method_id"),
                    rs.getString("method_name"),
                    rs.getBoolean("is_available")
                );
                paymentMethods.add(payment);
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment methods: " + e.getMessage());
        }
        return paymentMethods;
    }
    
    public static List<Payment> getAvailablePaymentMethods() {
        List<Payment> paymentMethods = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM payment_method_tb WHERE is_available = 1 ORDER BY payment_method_id";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment(
                    rs.getInt("payment_method_id"),
                    rs.getString("method_name"),
                    true
                );
                paymentMethods.add(payment);
            }
        } catch (SQLException e) {
            System.err.println("Error getting available payment methods: " + e.getMessage());
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
    
    public static Payment getPaymentMethodByName(String methodName) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM payment_method_tb WHERE method_name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, methodName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Payment(
                    rs.getInt("payment_method_id"),
                    rs.getString("method_name"),
                    rs.getBoolean("is_available")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment method by name: " + e.getMessage());
        }
        return null;
    }
    
    public static Payment getPaymentMethodById(int paymentMethodId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM payment_method_tb WHERE payment_method_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, paymentMethodId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Payment(
                    rs.getInt("payment_method_id"),
                    rs.getString("method_name"),
                    rs.getBoolean("is_available")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment method by ID: " + e.getMessage());
        }
        return null;
    }
    
    public static String getPaymentMethodName(int paymentMethodId) {
        Payment payment = getPaymentMethodById(paymentMethodId);
        if (payment != null) {
            return payment.getMethodName();
        }
        
        switch (paymentMethodId) {
            case 1: 
                return "Cash";
            case 2: 
                return "GCash";
            default: 
                return "Cash";
        }
    }
}
