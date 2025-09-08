package backend;

import java.sql.Timestamp;

public class ActivityLogReport {
    private int userId;
    private String activityType;
    private String description;
    private Timestamp activityDate;
    
    public ActivityLogReport(int userId, String activityType, String description, Timestamp activityDate) {
        this.userId = userId;
        this.activityType = activityType;
        this.description = description;
        this.activityDate = activityDate;
    }
    
    // Getters
    public int getUserId() { return userId; }
    public String getActivityType() { return activityType; }
    public String getDescription() { return description; }
    public Timestamp getActivityDate() { return activityDate; }
    
    // Formatted getters for UI display
    public String getFormattedDate() {
        return activityDate != null ? activityDate.toString() : "N/A";
    }
}