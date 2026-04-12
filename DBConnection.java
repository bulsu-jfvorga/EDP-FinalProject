package agapay;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // SQL Server connection info
    private static final String URL =
        "jdbc:sqlserver://localhost:1433;databaseName=agapay_system;trustServerCertificate=true";
    private static final String USER = "agapay_admin";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() {
    try {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connected to database!");
        return conn;
    } catch (Exception e) {
        System.out.println("DATABASE CONNECTION FAILED:");
        e.printStackTrace(); 
        return null;
    }
}
}
