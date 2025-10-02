
package services;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;
import database.DatabaseConnection;

public class QueueManager {
    
    private static Queue<Integer> orderQueue = new LinkedList<>();
    
    
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
    
    
    public static void addToQueue(int orderId) {
        orderQueue.add(orderId);
    }
    
    
    public static Integer getNextOrder() {
        return orderQueue.poll(); 
    }
    
    
    public static Integer peekNextOrder() {
        return orderQueue.peek();
    }
    
    
    public static int getQueueSize() {
        return orderQueue.size();
    }
    
    
    public static boolean isQueueEmpty() {
        return orderQueue.isEmpty();
    }
    
    
    public static Queue<Integer> getQueue() {
        return new LinkedList<>(orderQueue); 
    }
}