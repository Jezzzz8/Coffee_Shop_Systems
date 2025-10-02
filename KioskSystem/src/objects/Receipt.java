package objects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import services.*;

public class Receipt {
    private int orderId;
    private String customerName;
    private String cashierName;
    private LocalDateTime orderDateTime;
    private LocalDateTime paymentDateTime;
    private List<OrderItem> items;
    private double totalAmount;
    private double paymentReceived;
    private double change;
    private String paymentMethod;
    private String storeInfo;
    
    public Receipt(int orderId, 
                  LocalDateTime orderDateTime, LocalDateTime paymentDateTime,
                  List<OrderItem> items, double totalAmount, 
                  double paymentReceived, double change, String paymentMethod) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.cashierName = cashierName;
        this.orderDateTime = orderDateTime;
        this.paymentDateTime = paymentDateTime;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentReceived = paymentReceived;
        this.change = change;
        this.paymentMethod = paymentMethod;
        this.storeInfo = "Don Macchiatto\nMax Suniel St, Carmen,\nCagayan de Oro, Philippines.\nContact: (555) 123-4567";
    }
    
    public String formatReceipt() {
        StringBuilder receipt = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        receipt.append("================================\n");
        receipt.append("           RECEIPT             \n");
        receipt.append("================================\n");
        receipt.append(storeInfo).append("\n");
        receipt.append("--------------------------------\n");
        receipt.append(String.format("Order ID: %d\n", orderId));
        receipt.append(String.format("Order Date: %s\n", orderDateTime.format(formatter)));
        receipt.append(String.format("Payment Date: %s\n", paymentDateTime.format(formatter)));
        receipt.append("--------------------------------\n");
        receipt.append("ITEMS:\n");
        receipt.append("--------------------------------\n");
        
        for (OrderItem item : items) {
            Product product = ProductManager.getProductById(item.getProductId());
            if (product != null) {
                double subtotal = product.getPrice() * item.getQuantity();
                receipt.append(String.format("%-20s ₱%7.2f\n", product.getName(), product.getPrice()));
                receipt.append(String.format("  Qty: %-3d        ₱%7.2f\n", item.getQuantity(), subtotal));
            }
        }
        
        receipt.append("--------------------------------\n");
        receipt.append(String.format("SUBTOTAL:        ₱%8.2f\n", totalAmount));
        receipt.append(String.format("PAYMENT:         ₱%8.2f\n", paymentReceived));
        receipt.append(String.format("CHANGE:          ₱%8.2f\n", change));
        receipt.append("--------------------------------\n");
        receipt.append(String.format("Payment Method: %s\n", paymentMethod));
        receipt.append("================================\n");
        receipt.append("        THANK YOU!             \n");
        receipt.append("   PLEASE COME AGAIN!          \n");
        receipt.append("================================\n");
        
        return receipt.toString();
    }
    
    
    public int getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getCashierName() { return cashierName; }
    public LocalDateTime getOrderDateTime() { return orderDateTime; }
    public LocalDateTime getPaymentDateTime() { return paymentDateTime; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public double getPaymentReceived() { return paymentReceived; }
    public double getChange() { return change; }
    public String getPaymentMethod() { return paymentMethod; }
}