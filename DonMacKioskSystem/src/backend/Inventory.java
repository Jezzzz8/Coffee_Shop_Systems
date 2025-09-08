// Inventory.java
package backend;

import java.sql.Timestamp;
import java.util.Objects;

public class Inventory {
    private int inventoryId;
    private int productId;
    private int stockQuantity;
    private Timestamp lastUpdated;
    
    // Default constructor
    public Inventory() {
    }
    
    // Constructor for new inventory (without ID)
    public Inventory(int productId, int stockQuantity, Timestamp lastUpdated) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.lastUpdated = lastUpdated;
    }
    
    // Full constructor
    public Inventory(int inventoryId, int productId, int stockQuantity, Timestamp lastUpdated) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.lastUpdated = lastUpdated;
    }
    
    // Getters and setters
    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }
    
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { 
        this.stockQuantity = Math.max(0, stockQuantity); // Prevent negative stock
        this.lastUpdated = new Timestamp(System.currentTimeMillis());
    }
    
    public Timestamp getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }
    
    // Business logic methods
    public boolean isLowStock(int threshold) {
        return stockQuantity <= threshold;
    }
    
    public boolean isOutOfStock() {
        return stockQuantity <= 0;
    }
    
    public boolean hasSufficientStock(int requiredQuantity) {
        return stockQuantity >= requiredQuantity;
    }
    
    public void adjustStock(int quantityChange) {
        this.stockQuantity = Math.max(0, this.stockQuantity + quantityChange);
        this.lastUpdated = new Timestamp(System.currentTimeMillis());
    }
    
    public int calculateReorderQuantity(int reorderLevel, int reorderQuantity) {
        if (stockQuantity <= reorderLevel) {
            return Math.max(reorderQuantity - stockQuantity, 0);
        }
        return 0;
    }
    
    // toString for debugging
    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", productId=" + productId +
                ", stockQuantity=" + stockQuantity +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
    
    // equals and hashCode for proper object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Inventory inventory = (Inventory) o;
        return inventoryId == inventory.inventoryId &&
                productId == inventory.productId &&
                stockQuantity == inventory.stockQuantity &&
                Objects.equals(lastUpdated, inventory.lastUpdated);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(inventoryId, productId, stockQuantity, lastUpdated);
    }
}