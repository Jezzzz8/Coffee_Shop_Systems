package backend;

import java.sql.Timestamp;

public class ActivityLog {
    private int logId;
    private int userId;
    private String username;
    private String activityType;
    private String description;
    private Timestamp timestamp;
    
    public ActivityLog(int logId, int userId, String username, String activityType, String description, Timestamp timestamp) {
        this.logId = logId;
        this.userId = userId;
        this.username = username;
        this.activityType = activityType;
        this.description = description;
        this.timestamp = timestamp;
    }
    
    // Getters
    public int getLogId() { return logId; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getActivityType() { return activityType; }
    public String getDescription() { return description; }
    public Timestamp getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s - %s", 
            timestamp.toString(), username, activityType, description);
    }
}