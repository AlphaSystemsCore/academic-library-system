package com.sys.utilities;

import com.sys.Config.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
            dbConfig.URL, 
            dbConfig.USER, 
            dbConfig.PASSWORD
        );
    }
}