package com.sys.dao;

import com.sys.model.Department;
import com.sys.model.Programme;
import com.sys.model.Programme.LevelEnum;
import com.sys.utilities.DatabaseConnection;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class ProgrammeDao{
    public boolean saveProgramme(Programme programme){
        String sql = "INSERT INTO programmes (name, code, level, duration_years, department_id)"+
        "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn =  DatabaseConnection.createConnection();
        PreparedStatement cursor = conn.prepareStatement(sql)) {
            cursor.setString(1, programme.getName());
            cursor.setString(2, programme.getProgrammeCode());
            cursor.setString(3, programme.getLevel().name());
            cursor.setInt(4, programme.getDurationYears());
            cursor.setInt(5, programme.getDepartmentId());

            int rows = cursor.executeUpdate();
            return rows > 0 ;
            
            
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            return false;
        }
        
    }
    public List<Programme> findAllProgrammes(){
        String sql = "SELECT * FROM programmes ";
        try (Connection conn = DatabaseConnection.createConnection();
            Statement cursor = conn.createStatement()) {
                List <Programme> programmes = new ArrayList<>();
            ResultSet rs = cursor.executeQuery(sql);
            while(rs.next()){
                Programme programme = new Programme(rs,getInt("programme_id")rs.getString("name"), rs.getString("code"), LevelEnum.valueOf(rs.getString("level")),rs.getInt("duration_years"), rs.getInt("department_id"));
                programmes.add(programme);      
            }
            return programmes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
    }
}

    public Programme findById(int ProgrammeId){
        String sql = "SELECT * FROM programmes WHERE programme_id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
                cursor.setInt(1, ProgrammeId);
                ResultSet rs = cursor.executeQuery();
                if(rs.next()){
                    return new Programme(
                    rs.getInt("programme_id"),
                    rs.getString("name"),
                    rs.getString("code"),
                    LevelEnum.valueOf(rs.getString("level")),
                    rs.getInt("duration_years"),
                    rs.getInt("department_id")

                    );

                }            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    

    // helperrsssss
    // public boolean save-(Programme programme){

    // }
    // public List<-> findAllProgrammes(){

    // } 
    // public Department findById--(int department_id){

    // }
    // public boolean update----(Department dept, int department_id){

    //     }
    // public boolean delete---(int department_id){

    // }
    // public boolean nameExistsDepartment(String name){

    // }
    // public boolean idExistsDeapartment(int department_id){

    // }

}