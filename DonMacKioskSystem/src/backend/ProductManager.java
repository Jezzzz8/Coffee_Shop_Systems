package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_tb ORDER BY product_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products: " + e.getMessage());
        }
        return products;
    }
    
    public static List<Product> searchProductsByName(String searchTerm) {
        List<Product> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_tb WHERE product_name LIKE ? ORDER BY product_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }
        return products;
    }
    
    public static List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_tb WHERE category = ? ORDER BY product_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products by category: " + e.getMessage());
        }
        return products;
    }
    
    public static List<Product> searchProducts(String name) {
        List<Product> results = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        // Use correct table and column names
        String sql = "SELECT * FROM product_tb WHERE product_name LIKE ? AND is_active = true ORDER BY product_name";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                );
                results.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }
        return results;
    }
    
    public static Product getProductById(int productId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_tb WHERE product_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting product by ID: " + e.getMessage());
        }
        return null;
    }
    
    public static boolean addProduct(Product product) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO product_tb (product_id, product_name, category, price, description, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, product.getId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getCategory());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setString(5, product.getDescription());
            pstmt.setString(6, product.getImagePath());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean updateProduct(Product product) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE product_tb SET product_name = ?, category = ?, price = ?, description = ?, image_path = ? WHERE product_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setString(4, product.getDescription());
            pstmt.setString(5, product.getImagePath());
            pstmt.setInt(6, product.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean updateProductImage(int productId, String imagePath) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE product_tb SET image_path = ? WHERE product_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, imagePath);
            pstmt.setInt(2, productId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product image: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean deleteProduct(int productId) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "DELETE FROM product_tb WHERE product_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean updateStock(int productId, int quantity) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "UPDATE inventory_tb SET stock_quantity = stock_quantity + ? WHERE product_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
        }
        return false;
    }
    
    public static int getTotalProducts() {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT COUNT(*) as count FROM product_tb";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total products: " + e.getMessage());
        }
        return 0;
    }
    
    public static boolean setProductActiveStatus(int productId, boolean isActive) {
        Connection conn = DatabaseConnection.getConnection();
        // Use correct table and column names
        String sql = "UPDATE product_tb SET is_active = ? WHERE product_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isActive);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error setting product active status: " + e.getMessage());
        }
        return false;
    }
    
    public static Product getProductByName(String productName) {
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_tb WHERE product_name = ? AND is_active = true";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting product by name: " + e.getMessage());
        }
        return null;
    }
    
    // Add to ProductManager.java
    public static List<Product> getProductsByCategoryAndSearch(String category, String searchTerm) {
        List<Product> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT * FROM product_tb WHERE category = ? AND product_name LIKE ? ORDER BY product_name";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("image_path")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products by category and search: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    public static List<ProductWithQuantity> getProductsWithQuantity() {
        List<ProductWithQuantity> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = """
            SELECT p.*, i.stock_quantity 
            FROM products_tb p 
            LEFT JOIN inventory_tb i ON p.product_id = i.product_id 
            ORDER BY p.product_id
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProductWithQuantity product = new ProductWithQuantity(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock_quantity"),
                    rs.getString("image_path")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products with quantity: " + e.getMessage());
        }
        return products;
    }

    public static List<ProductWithQuantity> searchProductsByNameWithQuantity(String searchTerm) {
        List<ProductWithQuantity> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = """
            SELECT p.*, i.stock_quantity 
            FROM products_tb p 
            LEFT JOIN inventory_tb i ON p.product_id = i.product_id 
            WHERE p.product_name LIKE ? 
            ORDER BY p.product_id
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProductWithQuantity product = new ProductWithQuantity(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock_quantity"),
                    rs.getString("image_path")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }
        return products;
    }

    public static List<ProductWithQuantity> getProductsByCategoryWithQuantity(String category) {
        List<ProductWithQuantity> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = """
            SELECT p.*, i.stock_quantity 
            FROM products_tb p 
            LEFT JOIN inventory_tb i ON p.product_id = i.product_id 
            WHERE p.category = ? 
            ORDER BY p.product_id
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProductWithQuantity product = new ProductWithQuantity(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock_quantity"),
                    rs.getString("image_path")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products by category: " + e.getMessage());
        }
        return products;
    }

    public static List<ProductWithQuantity> getProductsByCategoryAndSearchWithQuantity(String category, String searchTerm) {
        List<ProductWithQuantity> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        String sql = """
            SELECT p.*, i.stock_quantity 
            FROM products_tb p 
            LEFT JOIN inventory_tb i ON p.product_id = i.product_id 
            WHERE p.category = ? AND p.product_name LIKE ? 
            ORDER BY p.product_id
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProductWithQuantity product = new ProductWithQuantity(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock_quantity"),
                    rs.getString("image_path")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error getting products by category and search: " + e.getMessage());
        }
        return products;
    }
}