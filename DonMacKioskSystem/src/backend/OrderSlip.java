package backend;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderSlip {
    private int orderId;
    private String customerName;
    private String cashierName;
    private Timestamp orderDateTime;
    private List<OrderItem> items;
    private double totalAmount;
    private String paymentMethod;
    private String status;
    
    public OrderSlip(int orderId, String customerName, String cashierName, 
                    Timestamp orderDateTime, List<OrderItem> items, 
                    double totalAmount, String paymentMethod, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.cashierName = cashierName;
        this.orderDateTime = orderDateTime;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }
    
    // Getters
    public int getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getCashierName() { return cashierName; }
    public Timestamp getOrderDateTime() { return orderDateTime; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }

    // Format order slip for printing/display
    public String formatOrderSlip() {
        StringBuilder slip = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        slip.append("================================\n");
        slip.append("           ORDER SLIP          \n");
        slip.append("================================\n");
        slip.append(String.format("Order ID: %d\n", orderId));
        // Convert Timestamp to LocalDateTime for formatting
        slip.append(String.format("Date: %s\n", orderDateTime.toLocalDateTime().format(formatter)));
        slip.append(String.format("Customer: %s\n", customerName));
        slip.append(String.format("Cashier: %s\n", cashierName));
        slip.append(String.format("Status: %s\n", status));
        slip.append("--------------------------------\n");
        slip.append("ITEMS:\n");

        for (OrderItem item : items) {
            Product product = ProductManager.getProductById(item.getProductId());
            if (product != null) {
                slip.append(String.format("%-20s ₱%7.2f x %d\n", 
                    product.getName(), product.getPrice(), item.getQuantity()));
            }
        }

        slip.append("--------------------------------\n");
        slip.append(String.format("TOTAL: ₱%.2f\n", totalAmount));
        slip.append(String.format("Payment Method: %s\n", paymentMethod));
        slip.append("================================\n");
        slip.append("Please proceed to counter for payment\n");
        slip.append("================================\n");

        return slip.toString();
    }
}