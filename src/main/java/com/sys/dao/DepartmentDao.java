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
            System.out.println("Error saving department: " + e.getMessage());
            return false;
        }

    }
    public List<Department> findAllDepartments(){
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Connection conn = DatabaseConnection.createConnection();
            Statement cursor = conn.createStatement();
            ResultSet rs = cursor.executeQuery(sql)){
                while(rs.next()){
                   Department dept = new Department(rs.getString(2), rs.getString(3) );
                   departments.add(dept);
                }
        } catch (SQLException e) {
            System.out.println("Error fetching departments: " + e.getMessage());
        }
        return departments;    
    }
    public Department findByIdDepartment(int department_id){
        String sql = "SELECT * FROM departments WHERE department_id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
                cursor.setInt(1, department_id);
                ResultSet rs = cursor.executeQuery();
                if(rs.next()){
                    Department dept = new Department(rs.getString(1), rs.getString(2));
                    return dept;
                }
            
        } catch (SQLException e) {
            System.out.print("Error: " +e.getMessage());
        }
        return null;
    }
    public boolean updateDeparment(Department dept, int department_id){
        String sql = "UPDATE departments SET name = ?, description = ?"+
                      "WHERE department_id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
                cursor.setString(1, dept.getName());
                cursor.setString(2, dept.getDescription());
                cursor.setInt(3, department_id);
                int rows = cursor.executeUpdate();
                return rows > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
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
            System.out.println("Error: " + e.getMessage());
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
            System.out.println("Error checking name: " + e.getMessage());
            return false; 
        }  
    }
        
    public boolean idExistsDeapartment(int department_id){
            String sql = "SELECT COUNT(*) FROM departments WHERE department_id = ?";
            try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
            cursor.setInt(1, department_id);
            ResultSet rs = cursor.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;    
            } catch (SQLException e) {
             System.out.println("Error: " + e.getMessage());
             return false;   
            }
    }

    private Department mapRow(ResultSet rs) throws SQLException {
        return new Department(
            rs.getString("name"),
            rs.getString("description")
        );
    }

    
    

    
}
