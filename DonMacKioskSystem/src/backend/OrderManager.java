// OrderManager.java
package backend;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    
    // In OrderManager.java
    public static int createNewOrder(int customerId, int cashierId, String paymentMethod) {
        Connection conn = DatabaseConnection.getConnection();

        try {
            System.out.println("Creating order with customerId: " + customerId + ", cashierId: " + cashierId + ", paymentMethod: " + paymentMethod);

            String sql = "INSERT INTO order_tb (customer_id, cashier_id, payment_method, status, total_amount) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, customerId);

            // Use NULL for kiosk orders (cashierId = 0)
            if (cashierId == 0) {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(2, cashierId);
            }

            pstmt.setString(3, paymentMethod);
            pstmt.setString(4, "Pending");
            pstmt.setDouble(5, 0.0);

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
    
    // In OrderManager.java
    public static boolean processNextOrder(int cashierId) {
        Integer nextOrderId = QueueManager.getNextOrder();

        if (nextOrderId == null) {
            return false; // No orders in queue
        }

        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE order_tb SET status = 'Processing', cashier_id = ? WHERE order_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cashierId);
            pstmt.setInt(2, nextOrderId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error processing next order: " + e.getMessage());
            // Put the order back in queue if processing fails
            QueueManager.addToQueue(nextOrderId);
            return false;
        }
    }
    
    public static boolean assignOrderToCashier(int orderId, int cashierId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE order_tb SET cashier_id = ?, status = 'Processing' WHERE order_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cashierId);
            pstmt.setInt(2, orderId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning order to cashier: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean addOrderItem(int orderId, int productId, int quantity, boolean skipStockCheck) {
        Connection conn = DatabaseConnection.getConnection();

        try {
            // Stock validation (only if not skipped)
            if (!skipStockCheck) {
                int currentStock = InventoryManager.getStockQuantity(productId);
                if (currentStock < quantity) {
                    throw new SQLException("Insufficient stock available");
                }
            }

            // Rest of the existing addOrderItem logic...
            // First, get the product price
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

            // Calculate subtotal
            double subtotal = productPrice * quantity;

            // Add the order item
            String sql = "INSERT INTO order_item_tb (order_id, product_id, quantity, subtotal) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, orderId);
                pstmt.setInt(2, productId);
                pstmt.setInt(3, quantity);
                pstmt.setDouble(4, subtotal);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Update the order total amount
                    updateOrderTotal(orderId);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding order item: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteOrderItem(int orderId, int productId) {
        String sql = "DELETE FROM order_item_tb WHERE order_id = ? AND product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateOrderTotal(int orderId) {
        String sql = "UPDATE order_tb SET total_amount = " +
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
    
    public static List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM order_tb WHERE status = ? ORDER BY order_date DESC LIMIT 10";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("order_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("cashier_id"),
                    rs.getTimestamp("order_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_method"),
                    rs.getString("status")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting orders by status: " + e.getMessage());
        }
        return orders;
    }
    
    public static List<Order> getOrdersByDateRange(java.sql.Date startDate, java.sql.Date endDate) {
        List<Order> orders = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM order_tb WHERE DATE(order_date) BETWEEN ? AND ? ORDER BY order_date DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("order_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("cashier_id"),
                    rs.getTimestamp("order_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_method"),
                    rs.getString("status")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error getting orders by date range: " + e.getMessage());
        }
        return orders;
    }
    
    public static Order getOrderById(int orderId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM order_tb WHERE order_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Order(
                    rs.getInt("order_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("cashier_id"),
                    rs.getTimestamp("order_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_method"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting order by ID: " + e.getMessage());
        }
        return null;
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
    
    public static boolean updateOrderStatus(int orderId, String status) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE order_tb SET status = ? WHERE order_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateOrderTotalAmount(int orderId, double newTotalAmount) {
        String sql = "UPDATE order_tb SET total_amount = ? WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newTotalAmount);
            pstmt.setInt(2, orderId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<SalesView> getOrdersWithDetails() {
        return SalesViewManager.getOrderDetails(); // Use the view manager
    }

    public static boolean updateOrderStatusWithValidation(int orderId, String newStatus) {
        // Add validation logic for status transitions
        // Pending -> Preparing -> Ready -> Completed
        // Can also go to Cancelled from any state
        return updateOrderStatus(orderId, newStatus);
    }
    
    public static boolean deleteOrder(int orderId) {
        Connection conn = DatabaseConnection.getConnection();

        try {
            // First delete all order items to maintain referential integrity
            String deleteItemsSql = "DELETE FROM order_item_tb WHERE order_id = ?";
            try (PreparedStatement itemsStmt = conn.prepareStatement(deleteItemsSql)) {
                itemsStmt.setInt(1, orderId);
                itemsStmt.executeUpdate();
            }

            // Then delete the order itself
            String deleteOrderSql = "DELETE FROM order_tb WHERE order_id = ?";
            try (PreparedStatement orderStmt = conn.prepareStatement(deleteOrderSql)) {
                orderStmt.setInt(1, orderId);
                int rowsAffected = orderStmt.executeUpdate();
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateOrderItemQuantity(int orderId, int productId, int newQuantity) {
        String sql = "UPDATE order_items SET quantity = ? WHERE order_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, orderId);
            pstmt.setInt(3, productId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}