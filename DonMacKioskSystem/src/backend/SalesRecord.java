// SalesRecord.java
package backend;

import java.sql.Timestamp;

public class SalesRecord {
    private int orderId;
    private Timestamp date;
    private String productName;
    private int quantity;
    private double price;
    private double subtotal;
    
    public SalesRecord(int orderId, Timestamp date, String productName, int quantity, double price, double subtotal) {
        this.orderId = orderId;
        this.date = date;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }
    
    // Getters and setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}