package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SalesReportManager {
    
    public static List<SalesRecord> getSalesByPeriod(String period) {
        List<SalesRecord> sales = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();

        String sql = "";
        switch (period.toLowerCase()) {
            case "today":
                sql = "SELECT o.order_id, o.order_date, p.product_name, p.price, od.quantity, od.subtotal " +  // Changed od.price to p.price
                      "FROM order_tb o " +
                      "JOIN order_item_tb od ON o.order_id = od.order_id " +
                      "JOIN product_tb p ON od.product_id = p.product_id " +
                      "WHERE DATE(o.order_date) = CURDATE() AND o.status = 'Completed' " +
                      "ORDER BY o.order_date DESC";
                break;
            case "this week":
                sql = "SELECT o.order_id, o.order_date, p.product_name, p.price, od.quantity, od.subtotal " +  // Changed od.price to p.price
                      "FROM order_tb o " +
                      "JOIN order_item_tb od ON o.order_id = od.order_id " +
                      "JOIN product_tb p ON od.product_id = p.product_id " +
                      "WHERE YEARWEEK(o.order_date) = YEARWEEK(CURDATE()) AND o.status = 'Completed' " +
                      "ORDER BY o.order_date DESC";
                break;
            case "monthly":
                sql = "SELECT o.order_id, o.order_date, p.product_name, p.price, od.quantity, od.subtotal " +  // Changed od.price to p.price
                      "FROM order_tb o " +
                      "JOIN order_item_tb od ON o.order_id = od.order_id " +
                      "JOIN product_tb p ON od.product_id = p.product_id " +
                      "WHERE MONTH(o.order_date) = MONTH(CURDATE()) AND YEAR(o.order_date) = YEAR(CURDATE()) " +
                      "ORDER BY o.order_date DESC";
                break;
            case "annual":
                sql = "SELECT o.order_id, o.order_date, p.product_name, od.price, od.quantity, od.subtotal " +
                      "FROM order_tb o " +
                      "JOIN order_item_tb od ON o.order_id = od.order_id " +
                      "JOIN product_tb p ON od.product_id = p.product_id " +
                      "WHERE YEAR(o.order_date) = YEAR(CURDATE()) AND o.status = 'Completed' " +
                      "ORDER BY o.order_date DESC";
                break;
                
            default:
                sql = "SELECT o.order_id, o.order_date, p.product_name, od.price, od.quantity, od.subtotal " +
                      "FROM order_tb o " +
                      "JOIN order_item_tb od ON o.order_id = od.order_id " +
                      "JOIN product_tb p ON od.product_id = p.product_id " +
                      "WHERE DATE(o.order_date) = CURDATE() AND o.status = 'Completed' " +
                      "ORDER BY o.order_date DESC";
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SalesRecord record = new SalesRecord(
                    rs.getInt("order_id"),
                    rs.getTimestamp("order_date"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getDouble("subtotal")
                );
                sales.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error getting sales by period: " + e.getMessage());
            e.printStackTrace();
        }
        return sales;
    }
    
    public static List<SalesRecord> getSalesByDateRange(Timestamp startDate, Timestamp endDate) {
        List<SalesRecord> sales = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT o.order_id, o.order_date, p.product_name, od.price, od.quantity, od.subtotal " +
                   "FROM order_tb o " +
                   "JOIN order_item_tb od ON o.order_id = od.order_id " +
                   "JOIN product_tb p ON od.product_id = p.product_id " +
                   "WHERE o.order_date BETWEEN ? AND ? AND o.status = 'Completed' " +
                   "ORDER BY o.order_date DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, startDate);
            pstmt.setTimestamp(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SalesRecord record = new SalesRecord(
                    rs.getInt("order_id"),
                    rs.getTimestamp("order_date"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getDouble("subtotal")
                );
                sales.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error getting sales by date range: " + e.getMessage());
            e.printStackTrace();
        }
        return sales;
    }
    
    public static List<SalesRecord> getBestSellingProducts() {
        List<SalesRecord> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();

        // CORRECTED QUERY - using proper table and column names
        String sql = "SELECT p.product_name, SUM(od.quantity) as total_quantity, " +
                    "SUM(od.subtotal) as total_revenue " +  // Changed from od.quantity * od.price to od.subtotal
                    "FROM order_item_tb od " +
                    "JOIN product_tb p ON od.product_id = p.product_id " +
                    "JOIN order_tb o ON od.order_id = o.order_id " +
                    "WHERE o.status = 'Completed' " +
                    "GROUP BY p.product_id, p.product_name " +  // Added product_name to GROUP BY
                    "ORDER BY total_quantity DESC " +
                    "LIMIT 10";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SalesRecord record = new SalesRecord(
                    null, // date not needed
                    rs.getString("product_name"),
                    rs.getInt("total_quantity"),
                    0, // individual price not needed
                    rs.getDouble("total_revenue")
                );
                products.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error getting best selling products: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }
    
    public static double getTotalRevenue() {
        Connection conn = DatabaseConnection.getConnection();
        // Get sum of all completed order_tb' total_amount
        String sql = "SELECT SUM(total_amount) as total FROM order_tb WHERE status = 'Completed'";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public static int getTotalOrders() {
        Connection conn = DatabaseConnection.getConnection();
        // Count completed order_tb
        String sql = "SELECT COUNT(*) as count FROM order_tb WHERE status = 'Completed'";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total orders: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public static List<SalesRecord> getTodaySales() {
        return getSalesByPeriod("today");
    }
    
    public static List<SalesRecord> getThisWeekSales() {
        return getSalesByPeriod("this week");
    }
    
    public static List<SalesRecord> getMonthlySales() {
        return getSalesByPeriod("monthly");
    }
    
    public static List<SalesRecord> getAnnualSales() {
        return getSalesByPeriod("annual");
    }
}