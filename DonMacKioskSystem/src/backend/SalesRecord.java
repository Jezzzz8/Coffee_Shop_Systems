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
    
    // Additional constructor without orderId for compatibility
    public SalesRecord(Timestamp date, String productName, int quantity, double price, double subtotal) {
        this(0, date, productName, quantity, price, subtotal);
    }
    
    // Getters
    public int getOrderId() { return orderId; }
    public Timestamp getDate() { return date; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getSubtotal() { return subtotal; }
    
    // Formatted getters for UI display
    public String getFormattedDate() {
        return date != null ? date.toString() : "N/A";
    }
    
    public String getFormattedPrice() {
        return String.format("₱%.2f", price);
    }
    
    public String getFormattedSubtotal() {
        return String.format("₱%.2f", subtotal);
    }
}