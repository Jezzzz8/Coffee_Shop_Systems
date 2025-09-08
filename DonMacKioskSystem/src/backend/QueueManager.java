// QueueManager.java
package backend;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class QueueManager {
    
    private static Queue<Integer> orderQueue = new LinkedList<>();
    
    // Initialize the queue with pending orders from database
    public static void initializeQueue() {
        orderQueue.clear();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT order_id FROM order_tb WHERE status = 'Pending' ORDER BY order_date ASC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                orderQueue.add(rs.getInt("order_id"));
            }
        } catch (SQLException e) {
            System.err.println("Error initializing queue: " + e.getMessage());
        }
    }
    
    // Add new order to the queue
    public static void addToQueue(int orderId) {
        orderQueue.add(orderId);
    }
    
    // Get the next order in queue (FIFO)
    public static Integer getNextOrder() {
        return orderQueue.poll(); // Returns and removes the head of the queue
    }
    
    // Peek at the next order without removing it
    public static Integer peekNextOrder() {
        return orderQueue.peek();
    }
    
    // Get queue size
    public static int getQueueSize() {
        return orderQueue.size();
    }
    
    // Check if queue is empty
    public static boolean isQueueEmpty() {
        return orderQueue.isEmpty();
    }
    
    // Get all orders in queue (for display purposes)
    public static Queue<Integer> getQueue() {
        return new LinkedList<>(orderQueue); // Return a copy
    }
}