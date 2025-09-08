// CustomerManager.java
package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerManager {
    
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM customer_tb ORDER BY first_name, last_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("contact_no"),
                    rs.getString("email"),
                    rs.getTimestamp("created_at")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Error getting customers: " + e.getMessage());
        }
        return customers;
    }
    
    public static boolean addCustomer(String firstName, String lastName, String contactNo, String email) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO customer_tb (first_name, last_name, contact_no, email) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, contactNo);
            pstmt.setString(4, email);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
        return false;
    }
    
    // NEW METHOD: Add customer and return the generated ID
    public static int addCustomerReturnId(String firstName, String lastName, String contactNo, String email) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO customer_tb (first_name, last_name, contact_no, email) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, contactNo);
            pstmt.setString(4, email);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
        return -1;
    }
    
    public static Customer getCustomerById(int customerId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM customer_tb WHERE customer_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("contact_no"),
                    rs.getString("email"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting customer by ID: " + e.getMessage());
        }
        return null;
    }
    
    // NEW METHOD: Get customer by phone number
    public static Customer getCustomerByPhone(String phoneNumber) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM customer_tb WHERE contact_no = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("contact_no"),
                    rs.getString("email"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting customer by phone: " + e.getMessage());
        }
        return null;
    }
    
    // NEW METHOD: Get customer (alias for getCustomerById for consistency)
    public static Customer getCustomer(int customerId) {
        return getCustomerById(customerId);
    }
    
    public static List<Customer> searchCustomers(String searchTerm) {
        List<Customer> results = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM customer_tb WHERE first_name LIKE ? OR last_name LIKE ? OR contact_no LIKE ? OR email LIKE ? ORDER BY first_name, last_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String likeTerm = "%" + searchTerm + "%";
            pstmt.setString(1, likeTerm);
            pstmt.setString(2, likeTerm);
            pstmt.setString(3, likeTerm);
            pstmt.setString(4, likeTerm);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("contact_no"),
                    rs.getString("email"),
                    rs.getTimestamp("created_at")
                );
                results.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Error searching customers: " + e.getMessage());
        }
        return results;
    }
}