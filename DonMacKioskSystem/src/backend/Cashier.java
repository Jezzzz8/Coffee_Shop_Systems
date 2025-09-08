// Cashier.java
package backend;

import java.sql.Timestamp;

public class Cashier {
    private int cashierId;
    private String cashierName;
    private String shiftSchedule;
    private boolean isActive;
    private Timestamp createdAt;
    
    public Cashier(int cashierId, String cashierName, String shiftSchedule, boolean isActive, Timestamp createdAt) {
        this.cashierId = cashierId;
        this.cashierName = cashierName;
        this.shiftSchedule = shiftSchedule;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    
    // Getters and setters
    public int getCashierId() { return cashierId; }
    public void setCashierId(int cashierId) { this.cashierId = cashierId; }
    
    public String getCashierName() { return cashierName; }
    public void setCashierName(String cashierName) { this.cashierName = cashierName; }
    
    public String getShiftSchedule() { return shiftSchedule; }
    public void setShiftSchedule(String shiftSchedule) { this.shiftSchedule = shiftSchedule; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}