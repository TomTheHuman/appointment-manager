package util;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = env.get('DB_URL');

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    // Driver interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    // Database username and password
    private static final String username = env.get('DB_USER');
    private static final String password = env.get('DB_PASS');

    /**
     * This method starts the database connection for use in the application
     *
     * @return
     */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successful!");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return conn;
    }

    /**
     * This method closes the database connection when the application is closed
     */
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed!");
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
