// SessionManager.java
package backend;

public class SessionManager {
    private static String currentUsername;
    private static String currentRole;
    private static boolean isLoggedIn = false;
    
    public static void login(String username, String role) {
        currentUsername = username;
        currentRole = role;
        isLoggedIn = true;
    }
    
    public static void logout() {
        currentUsername = null;
        currentRole = null;
        isLoggedIn = false;
    }
    
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    public static String getCurrentUsername() {
        return currentUsername;
    }
    
    public static String getCurrentRole() {
        return currentRole;
    }
}