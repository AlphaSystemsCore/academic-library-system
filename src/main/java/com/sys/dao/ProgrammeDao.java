package com.sys.dao;

import com.sys.model.Programme;
import com.sys.utilities.DatabaseConnection;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class ProgrammeDao{
    public void addProgramme(Programme programme){
        String sql = """
                INSERT INTO programmes (name, code,level, duration_years, department_id)
                VALUES(?,?,?,?,?);
                """;
        try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
                cursor.setString(1, programme.getName());
                cursor.setString(2, programme.getProgrammeCode());
                cursor.setString(3, programme.getLevel().name());
                cursor.setInt(4, programme.getDurationYears());
                cursor.setInt(5,programme.getDepartmentId());
                cursor.executeUpdate();  
        } catch (Exception e) {
            System.out.println("Error saving program: " + e.getMessage());
        }
    };
    public boolean
    public void viewProgrammeOne(){};
    public void viewProgrammeAll(){};
    public void deleteProgramme(){};
    public void updateProgramme(){};
}