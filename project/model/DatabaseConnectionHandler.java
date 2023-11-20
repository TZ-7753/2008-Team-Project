package project.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionHandler {
    private static final String DB_URL = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team013";
    private static final String DB_USER = "team013";
    private static final String DB_PASSWORD = "Aig8thoo8";

    // Define the connection as a class member to share it across the application.
    private Connection connection = null;

    public void openConnection() throws SQLException {
        // Load the JDBC driver and open the connection
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        // Close the connection in a separate method to ensure proper resource
        // management.
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

}
