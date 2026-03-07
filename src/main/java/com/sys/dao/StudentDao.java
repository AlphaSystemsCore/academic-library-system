package com.sys.dao;
import com.sys.utilities.DatabaseConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentDao {
    public void addStudent(){
        

    }
    public void viewStudent(String admissionNumber){

    }
    public void updateStudentDetails(String admissionNumber){

    }
    public void deleteStudent(){

    }
    public void viewAllStudent(){
        String sql = "SELECT * FROM student";
            try(Connection conn = DatabaseConnection.createConnection() ;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.getResultSet()){
            while(rs.next()){
                System.out.println("1");
            }
                System.out.println("All Students");
            } catch (Exception e) {
                e.printStackTrace();
            }
        


    }
}
