package com.sys.dao;
import com.sys.utilities.Table;

import com.sys.utilities.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class CreateTablesDao {
    
    public static void createTables() {
        String sql = Table.tableCreation();
        String [] createTableStatements = sql.split(";[\\s]");


        try (Connection conn = DatabaseConnection.createConnection();
            Statement stmt = conn.createStatement()) {
        int count = 0;
          for(String row: createTableStatements){
            row.trim();
            if(!row.isBlank()){
                stmt.executeUpdate(row);
                count ++;
                System.out.println("Statement "+ count+" Executed!");
            }
        }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
      
     
         

    }
}