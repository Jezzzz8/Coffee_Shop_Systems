// DatabaseConnection.java - MySQL version
package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // MySQL database connection
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/donmac_kiosk?zeroDateTimeBehavior=CONVERT_TO_NULL", 
                    "root", 
                    "password"
                );
                
                // Optional: Initialize database if it doesn't exist
                initializeDatabase();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    private static void initializeDatabase() {
        // You can keep this method for additional initialization
        // or remove it if you've already created the database manually
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}