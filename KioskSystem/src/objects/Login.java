package objects;

public class Login {
    private static String currentUsername;
    private static boolean loggedIn = false;
    
    public static void login(String username) {
        currentUsername = username;
        loggedIn = true;
    }
    
    public static void logout() {
        currentUsername = null;
        loggedIn = false;
    }
    
    public static boolean isLoggedIn() {
        return loggedIn;
    }
    
    public static String getCurrentUsername() {
        return currentUsername;
    }
}