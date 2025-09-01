// SalesReportManager.java
package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalesReportManager {
    
    public static List<SalesRecord> getTodaySales() {
        return getSalesByDateRange(LocalDate.now(), LocalDate.now());
    }
    
    public static List<SalesRecord> getThisWeekSales() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        return getSalesByDateRange(startOfWeek, today);
    }
    
    public static List<SalesRecord> getMonthlySales() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        return getSalesByDateRange(startOfMonth, today);
    }
    
    public static List<SalesRecord> getAnnualSales() {
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.withDayOfYear(1);
        return getSalesByDateRange(startOfYear, today);
    }
    
    private static List<SalesRecord> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<SalesRecord> sales = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT o.id as order_id, o.created_at, p.name as product_name, " +
                    "oi.quantity, oi.price, (oi.quantity * oi.price) as subtotal " +
                    "FROM orders o " +
                    "JOIN order_items oi ON o.id = oi.order_id " +
                    "JOIN products p ON oi.product_id = p.id " +
                    "WHERE DATE(o.created_at) BETWEEN ? AND ? " +
                    "ORDER BY o.created_at DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SalesRecord record = new SalesRecord(
                    rs.getInt("order_id"),
                    rs.getTimestamp("created_at"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getDouble("subtotal")
                );
                sales.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }
    
    public static double getTotalSalesAmount(List<SalesRecord> sales) {
        double total = 0;
        for (SalesRecord record : sales) {
            total += record.getSubtotal();
        }
        return total;
    }
}