// Order.java
package backend;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int customerId;
    private int cashierId;
    private Timestamp orderDate;
    private double totalAmount;
    private String paymentMethod;
    private String status;
    
    public Order(int orderId, int customerId, int cashierId, Timestamp orderDate, double totalAmount, String paymentMethod, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.cashierId = cashierId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }
    
    // Getters and setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public int getCashierId() { return cashierId; }
    public void setCashierId(int cashierId) { this.cashierId = cashierId; }
    
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}