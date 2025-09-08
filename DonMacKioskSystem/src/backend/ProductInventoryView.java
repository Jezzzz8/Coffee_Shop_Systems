package backend;

import java.sql.Timestamp;

public class ProductInventoryView {
    private int productId;
    private String productName;
    private String category;
    private double price;
    private String description;
    private int stockQuantity;
    private Timestamp lastUpdated;
    
    public ProductInventoryView(int productId, String productName, String category, double price, 
                               String description, int stockQuantity, Timestamp lastUpdated) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.lastUpdated = lastUpdated;
    }
    
    // Getters and setters for all fields
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public Timestamp getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }
}