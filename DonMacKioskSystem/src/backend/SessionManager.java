package backend;

public class SessionManager {
    private static int currentUserId;
    private static String currentUsername;
    private static String currentRole;
    private static boolean loggedIn = false;
    
    public static void login(String username) {
        currentUsername = username;
        loggedIn = true;
    }
    
    public static void logout() {
        currentUsername = null;
        currentRole = null;
        currentUserId = 0;
        loggedIn = false;
    }
    
    public static boolean isLoggedIn() {
        return loggedIn;
    }
    
    public static String getCurrentUsername() {
        return currentUsername;
    }
    
    public static String getCurrentRole() {
        return currentRole;
    }
    
    public static int getCurrentUserId() {
        return currentUserId;
    }
    
    // Add setter methods
    public static void setCurrentRole(String role) {
        currentRole = role;
    }
    
    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }
}