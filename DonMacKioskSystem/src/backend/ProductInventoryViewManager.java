package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductInventoryViewManager {
    
    public static List<ProductInventoryView> getAllProductInventory() {
        List<ProductInventoryView> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_inventory_vw ORDER BY product_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProductInventoryView product = new ProductInventoryView(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getInt("stock_quantity"),
                    rs.getTimestamp("last_updated")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting product inventory: " + e.getMessage());
        }
        return products;
    }
}