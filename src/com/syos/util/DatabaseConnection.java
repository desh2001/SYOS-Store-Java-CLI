package com.syos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Database විස්තර
    private String url = "jdbc:mysql://localhost:3306/syos_db";
    private String username = "root";
    private String password = ""; // XAMPP නම් සාමාන්‍යයෙන් හිස්ව තබන්න

    // Private Constructor (Singleton Pattern සඳහා)
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Database Driver එක සොයාගත නොහැක: " + e.getMessage());
        }
    }

    // එකම instance එක ලබාගැනීම
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}