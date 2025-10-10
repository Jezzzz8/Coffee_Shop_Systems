package services;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;
import database.DatabaseConnection;

public class QueueManager {
    
    private static Queue<Integer> orderQueue = new LinkedList<>();
    
    public static void initializeQueue() {
        orderQueue.clear();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT order_id FROM customer_order_tb ORDER BY order_datetime ASC";
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                orderQueue.add(rs.getInt("order_id"));
            }
            
            System.out.println("Queue initialized with " + orderQueue.size() + " orders");
            
        } catch (SQLException e) {
            System.err.println("Error initializing queue: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in finally block
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    public static void addToQueue(int orderId) {
        if (!orderQueue.contains(orderId)) {
            orderQueue.add(orderId);
            System.out.println("Order " + orderId + " added to queue. Queue size: " + orderQueue.size());
        } else {
            System.out.println("Order " + orderId + " already exists in queue");
        }
    }
    
    public static boolean removeFromQueue(int orderId) {
        boolean removed = orderQueue.remove(orderId);
        if (removed) {
            System.out.println("Order " + orderId + " removed from queue. Queue size: " + orderQueue.size());
        }
        return removed;
    }

    public static boolean containsOrder(int orderId) {
        return orderQueue.contains(orderId);
    }
    
    public static Integer getNextOrder() {
        Integer nextOrder = orderQueue.poll();
        if (nextOrder != null) {
            System.out.println("Processing next order: " + nextOrder + ". Queue size: " + orderQueue.size());
        }
        return nextOrder;
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
    
    public static void refreshQueueFromDatabase() {
        initializeQueue();
    }
    
    public static String getQueueStatus() {
        if (orderQueue.isEmpty()) {
            return "Queue is empty";
        } else {
            return "Queue size: " + orderQueue.size() + ", Next order: " + peekNextOrder();
        }
    }
    
    public static void clearQueue() {
        orderQueue.clear();
        System.out.println("Queue cleared");
    }
}