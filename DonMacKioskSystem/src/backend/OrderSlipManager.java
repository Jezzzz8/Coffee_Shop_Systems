package backend;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class OrderSlipManager {
    
    public static OrderSlip generateOrderSlip(int orderId) {
        Order order = OrderManager.getOrderById(orderId);
        if (order == null) return null;
        
        List<OrderItem> items = OrderManager.getOrderItems(orderId);
        Customer customer = CustomerManager.getCustomerById(order.getCustomerId());
        User cashier = UserAuthentication.getUserById(order.getCashierId());
        
        String customerName = (customer != null) ? customer.getFullName() : "Walk-in Customer";
        String cashierName = (cashier != null) ? cashier.getUsername() : "System";
        
        return new OrderSlip(
            orderId,
            customerName,
            cashierName,
            order.getOrderDate(),
            items,
            order.getTotalAmount(),
            order.getPaymentMethod(),
            order.getStatus()
        );
    }
    
    public static Receipt generateReceipt(int orderId, double paymentReceived, double change) {
        Order order = OrderManager.getOrderById(orderId);
        if (order == null) return null;
        
        List<OrderItem> items = OrderManager.getOrderItems(orderId);
        Customer customer = CustomerManager.getCustomerById(order.getCustomerId());
        User cashier = UserAuthentication.getUserById(order.getCashierId());
        
        String customerName = (customer != null) ? customer.getFullName() : "Walk-in Customer";
        String cashierName = (cashier != null) ? cashier.getUsername() : "System";
        
        return new Receipt(
            orderId,
            customerName,
            cashierName,
            order.getOrderDate().toLocalDateTime(),
            LocalDateTime.now(),
            items,
            order.getTotalAmount(),
            paymentReceived,
            change,
            order.getPaymentMethod()
        );
    }
}