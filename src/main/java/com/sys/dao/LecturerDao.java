package com.sys.dao;

import com.sys.model.Lecturer;
import com.sys.utilities.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerDao {

    public boolean saveLecturer(Lecturer lecturer) {
        String sql = "INSERT INTO lecturers (first_name, last_name, email, title, ID_NO, " +
                     "phone_number, staff_number, department_id, role_id, password) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setString(1, lecturer.getFirstName());
            cursor.setString(2, lecturer.getLastName());
            cursor.setString(3, lecturer.getEmail());
            cursor.setString(4, lecturer.getTitle());
            cursor.setString(5, lecturer.getIdentificationNumber());
            cursor.setString(6, lecturer.getPhoneNumber());
            cursor.setString(7, lecturer.getStaffNumber());
            cursor.setInt(8, lecturer.getDepartmentId());
            cursor.setInt(9, lecturer.getRoleId());
            cursor.setString(10, lecturer.getPassword());

            int rows = cursor.executeUpdate();

            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                lecturer.setLecturerId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Lecturer> findAllLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT * FROM lecturers";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                lecturers.add(mapRow(rs));
            }
            return lecturers;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all lecturers in a specific department
    public List<Lecturer> findLecturersByDepartment(int departmentId) {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT * FROM lecturers WHERE department_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, departmentId);
            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                lecturers.add(mapRow(rs));
            }
            return lecturers;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Lecturer findLecturerById(int lecturerId) {
        String sql = "SELECT * FROM lecturers WHERE lecturer_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, lecturerId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Lecturer findLecturerByEmail(String email) {
        String sql = "SELECT * FROM lecturers WHERE email = ?";

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

    public boolean updateLecturer(int lecturerId, String firstName, String lastName,
                               String email, String title, String identificationNumber,
                               String phoneNumber, String staffNumber, int departmentId) {
        String sql = "UPDATE lecturers SET first_name = ?, last_name = ?, email = ?, " +
                     "title = ?, ID_NO = ?, phone_number = ?, staff_number = ?, " +
                     "department_id = ? WHERE lecturer_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, firstName);
            cursor.setString(2, lastName);
            cursor.setString(3, email);
            cursor.setString(4, title);
            cursor.setString(5, identificationNumber);
            cursor.setString(6, phoneNumber);
            cursor.setString(7, staffNumber);
            cursor.setInt(8, departmentId);
            cursor.setInt(9, lecturerId);

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLecturer(int lecturerId) {
        String sql = "DELETE FROM lecturers WHERE lecturer_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, lecturerId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExistsLecturer(String email) {
        String sql = "SELECT COUNT(*) FROM lecturers WHERE email = ?";

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

    public boolean staffNumberExistsLecturer(String staffNumber) {
        String sql = "SELECT COUNT(*) FROM lecturers WHERE staff_number = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, staffNumber);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean idExistsLecturer(int lecturerId) {
        String sql = "SELECT COUNT(*) FROM lecturers WHERE lecturer_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, lecturerId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Lecturer mapRow(ResultSet rs) throws SQLException {
        return new Lecturer(
            rs.getInt("lecturer_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("title"),
            rs.getString("ID_NO"),
            rs.getString("phone_number"),
            rs.getString("staff_number"),
            rs.getInt("department_id"),
            rs.getInt("role_id"),
            rs.getString("password")
        );
    }
}
