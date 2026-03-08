package com.sys.dao;

import com.sys.model.Enrollment;
import com.sys.model.Enrollment.StatusEnum;
import com.sys.utilities.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {

    public boolean saveEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, offering_id, attempt_number, " +
                     "status, is_counted_in_gpa, is_active, enrolled_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setInt(1, enrollment.getStudentId());
            cursor.setInt(2, enrollment.getOfferingId());
            cursor.setInt(3, enrollment.getAttemptNumber());
            cursor.setString(4, enrollment.getStatus().name());
            cursor.setBoolean(5, enrollment.isCountedInGpa());
            cursor.setBoolean(6, enrollment.isActive());
            cursor.setDate(7, Date.valueOf(enrollment.getEnrolledDate()));

            int rows = cursor.executeUpdate();

            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                enrollment.setEnrollmentId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Enrollment> findAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                enrollments.add(mapRow(rs));
            }
            return enrollments;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all active enrollments for a student in the current semester
    public List<Enrollment> findEnrollmentsByStudent(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id = ? AND is_active = TRUE";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                enrollments.add(mapRow(rs));
            }
            return enrollments;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all active enrollments for a specific course offering (for lecturer to see students)
    public List<Enrollment> findEnrollmentsByOffering(int offeringId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE offering_id = ? AND is_active = TRUE";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, offeringId);
            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                enrollments.add(mapRow(rs));
            }
            return enrollments;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Enrollment findEnrollmentById(int enrollmentId) {
        String sql = "SELECT * FROM enrollments WHERE enrollment_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, enrollmentId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update status, isActive, isCountedInGpa — used when dropping or completing a course
    public boolean updateEnrollmentStatus(Enrollment enrollment) {
        String sql = "UPDATE enrollments SET status = ?, is_active = ?, is_counted_in_gpa = ? " +
                     "WHERE enrollment_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, enrollment.getStatus().name());
            cursor.setBoolean(2, enrollment.isActive());
            cursor.setBoolean(3, enrollment.isCountedInGpa());
            cursor.setInt(4, enrollment.getEnrollmentId());

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Count how many active courses a student has in the current semester — enforces max 5
    public int countActiveEnrollmentsInCurrentSemester(int studentId) {
        String sql = "SELECT COUNT(*) FROM enrollments e " +
                     "JOIN course_offerings co ON e.offering_id = co.offering_id " +
                     "WHERE e.student_id = ? AND e.is_active = TRUE " +
                     "AND co.semester_id = (SELECT semester_id FROM semesters WHERE is_current = TRUE)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Check if student is already enrolled in a specific offering
    public boolean enrollmentExists(int studentId, int offeringId) {
        String sql = "SELECT COUNT(*) FROM enrollments " +
                     "WHERE student_id = ? AND offering_id = ? AND is_active = TRUE";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            cursor.setInt(2, offeringId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get attempt number for a student retaking a course
    public int getAttemptNumber(int studentId, int offeringId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND offering_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            cursor.setInt(2, offeringId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) + 1; // existing attempts + 1 = new attempt number
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public boolean idExistsEnrollment(int enrollmentId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE enrollment_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, enrollmentId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Enrollment mapRow(ResultSet rs) throws SQLException {
        return new Enrollment(
            rs.getInt("enrollment_id"),
            rs.getInt("student_id"),
            rs.getInt("offering_id"),
            rs.getInt("attempt_number"),
            rs.getDate("enrolled_date").toLocalDate()
        );
    }
}
