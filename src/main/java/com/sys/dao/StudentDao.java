package com.sys.dao;


import com.sys.model.Student;
import com.sys.utilities.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    public boolean saveStudent(Student student) {
        String sql = "INSERT INTO students (first_name, last_name, email, phone_number, ID_NO, " +
                     "password, programme_id, admission_number, role_id, registered_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setString(1, student.getFirstName());
            cursor.setString(2, student.getLastName());
            cursor.setString(3, student.getEmail());
            cursor.setString(4, student.getPhoneNumber());
            cursor.setString(5, student.getIdentificationNumber());
            cursor.setString(6, student.getPassword());
            cursor.setInt(7, student.getProgrammeId());
            cursor.setString(8, student.getAdmissionNumber());
            cursor.setInt(9, student.getRoleId());
            cursor.setDate(10, Date.valueOf(student.getRegisteredDate()));

            int rows = cursor.executeUpdate();

            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                student.setStudentId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Student> findAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                students.add(mapRow(rs));
            }
            return students;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all students in a specific programme
    public List<Student> findStudentsByProgramme(int programmeId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE programme_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, programmeId);
            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                students.add(mapRow(rs));
            }
            return students;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Student findStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Student findStudentByEmail(String email) {
        String sql = "SELECT * FROM students WHERE email = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, email);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Student findStudentByAdmissionNumber(String admissionNumber) {
        String sql = "SELECT * FROM students WHERE admission_number = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, admissionNumber);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, " +
                     "phone_number = ?, ID_NO = ?, programme_id = ? " +
                     "WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, student.getFirstName());
            cursor.setString(2, student.getLastName());
            cursor.setString(3, student.getEmail());
            cursor.setString(4, student.getPhoneNumber());
            cursor.setString(5, student.getIdentificationNumber());
            cursor.setInt(6, student.getProgrammeId());
            cursor.setInt(7, student.getStudentId());

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExistsStudent(String email) {
        String sql = "SELECT COUNT(*) FROM students WHERE email = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, email);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean idNoExistsStudent(String idNo) {
        String sql = "SELECT COUNT(*) FROM students WHERE ID_NO = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, idNo);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean idExistsStudent(int studentId) {
        String sql = "SELECT COUNT(*) FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Used to generate next admission number — returns total student count
    public int countStudents() {
        String sql = "SELECT COUNT(*) FROM students";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean hasStudentProgramme(int programmeId){
        String sql = " SELECT COUNT(*) FROM students WHERE programme_id = ? ";
        try (Connection conn = DatabaseConnection.createConnection();
        PreparedStatement cursor = conn.prepareStatement(sql)) {
            cursor.setInt(1, programmeId);
            ResultSet rs = cursor.executeQuery();
            if(rs.next())
                return rs.getInt(1) > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt("student_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("phone_number"),
            rs.getString("ID_NO"),
            rs.getString("password"),
            rs.getInt("programme_id"),
            rs.getString("admission_number"),
            rs.getInt("role_id"),
            rs.getDate("registered_date").toLocalDate()
        );
    }
}
