package objects;

public class Payment {
    private int paymentMethodId;
    private String methodName;
    private boolean isAvailable;
    
    public Payment(int paymentMethodId, String methodName, boolean isAvailable) {
        this.paymentMethodId = paymentMethodId;
        this.methodName = methodName;
        this.isAvailable = isAvailable;
    }
    
    // Getters and setters
    public int getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(int paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    
    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
}
