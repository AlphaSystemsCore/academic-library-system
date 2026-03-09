package com.sys.dao;

import com.sys.utilities.DatabaseConnection;
import com.sys.model.Department;
import java.util.List;

import java.util.ArrayList;
import java.sql.*;

public class DepartmentDao {
    // insert dept
    public boolean saveDepartment(Department dept){
        String sql = "INSERT INTO departments (name, description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
                cursor.setString(1, dept.getName());
                cursor.setString(2,dept.getDescription());
                int rows = cursor.executeUpdate();
                return rows > 0;
        } catch (SQLException e) {
e.printStackTrace();            return false;
        }

    }
    public List<Department> findAllDepartments(){
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Connection conn = DatabaseConnection.createConnection();
            Statement cursor = conn.createStatement();
            ResultSet rs = cursor.executeQuery(sql)){
                while(rs.next()){
                   Department dept = new Department(rs.getInt("department_id"),rs.getString("name"), rs.getString("description") );
                   departments.add(dept);
                }
                return departments; 
        } catch (SQLException e) {
e.printStackTrace();            return new ArrayList<>();
        }
           
    }
    public Department findByIdDepartment(int department_id){
        String sql = "SELECT * FROM departments WHERE department_id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
                cursor.setInt(1, department_id);
                ResultSet rs = cursor.executeQuery();
                if(rs.next()){
                    Department dept = new Department(rs.getInt("department_id"), rs.getString(1), rs.getString(2));
                    return dept;
                }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isUpdateDeparment(Department dept){
        String sql = "UPDATE departments SET name = ?, description = ?"+
                      "WHERE department_id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
                cursor.setString(1, dept.getName());
                cursor.setString(2, dept.getDescription());
                cursor.setInt(3, dept.getDepartmentId());
                int rows = cursor.executeUpdate();
                return rows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean deleteDepartment(int department_id){
        String sql = "DELETE FROM department WHERE department_id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
                cursor.setInt(1,department_id);
                int rows = cursor.executeUpdate();
                return rows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
        //helper
    public boolean nameExistsDepartment(String name){
        String sql = "SELECT COUNT(*) FROM departments WHERE name = ?";
        try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
                cursor.setString(1, name);
                ResultSet rs = cursor.executeQuery();
                rs.next();
                return rs.getInt(1) > 0;
        } catch (Exception e) {
           e.printStackTrace();
            return false; 
        }  
        // ??
    }
        
    public boolean idExistsDepartment(int department_id){
            String sql = "SELECT COUNT(*) FROM departments WHERE department_id = ?";
            try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
            cursor.setInt(1, department_id);
            ResultSet rs = cursor.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;    
            } catch (SQLException e) {
            e.printStackTrace();
             return false;   
            }
    }

    // private Department mapRow(ResultSet rs) throws SQLException {
    //     return new Department(
    //         rs.getString("name"),
    //         rs.getString("description")
    //     );
    // }

    
    

    
}
