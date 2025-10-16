package services;

import java.util.ArrayList;
import java.util.List;

public class UserAuthentication {
    
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    
    public static class AuthResult {
        public final boolean success;
        public final String errorMessage;
        public final String errorType;

        public AuthResult(boolean success, String errorMessage, String errorType) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.errorType = errorType;
        }
        
        public boolean isSuccess() { 
            return success; 
        }
        
        public String getErrorMessage() { 
            return errorMessage; 
        }
        
        public String getErrorType() { 
            return errorType; 
        }
    }
    
    public static boolean authenticate(String username, String password) {
        AuthResult result = authenticateWithDetails(username, password);
        return result.isSuccess();
    }
    
    public static AuthResult authenticateWithDetails(String username, String password) {
        
        if (!ADMIN_USERNAME.equals(username)) {
            return new AuthResult(false, "Invalid username", "USERNAME_ERROR");
        }
        
        if (!ADMIN_PASSWORD.equals(password)) {
            return new AuthResult(false, "Invalid password", "PASSWORD_ERROR");
        }
        
        return new AuthResult(true, null, null);
    }
}