package com.sys.dao;

import com.sys.model.CourseOffering;
import com.sys.utilities.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseOfferingDao {

    public boolean saveCourseOffering(CourseOffering offering) {
        String sql = "INSERT INTO course_offerings (course_id, lecturer_id, semester_id, programme_id) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setInt(1, offering.getCourseId());
            cursor.setInt(2, offering.getLecturerId());
            cursor.setInt(3, offering.getSemesterId());
            cursor.setInt(4, offering.getProgrammeId());

            int rows = cursor.executeUpdate();

            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                offering.setOfferingId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CourseOffering> findAllOfferings() {
        List<CourseOffering> offerings = new ArrayList<>();
        String sql = "SELECT * FROM course_offerings";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                offerings.add(new CourseOffering(
                    rs.getInt("offering_id"),
                    rs.getInt("course_id"),
                    rs.getInt("lecturer_id"),
                    rs.getInt("semester_id"),
                    rs.getInt("programme_id")
                ));
            }
            return offerings;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all offerings for a specific programme and semester
    // This is what students see when enrolling
    public List<CourseOffering> findOfferingsByProgrammeAndSemester(int programmeId, int semesterId) {
        List<CourseOffering> offerings = new ArrayList<>();
        String sql = "SELECT co.offering_id, co.course_id, co.lecturer_id, " +
                     "co.semester_id, co.programme_id, " +
                     "c.title AS course_title, c.course_code, c.credit_hours, " +
                     "l.first_name, l.last_name, l.title AS lecturer_title " +
                     "FROM course_offerings co " +
                     "JOIN courses c   ON co.course_id  = c.course_id " +
                     "JOIN lecturers l ON co.lecturer_id = l.lecturer_id " +
                     "WHERE co.programme_id = ? AND co.semester_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, programmeId);
            cursor.setInt(2, semesterId);
            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                CourseOffering offering = new CourseOffering(
                    rs.getInt("offering_id"),
                    rs.getInt("course_id"),
                    rs.getInt("lecturer_id"),
                    rs.getInt("semester_id"),
                    rs.getInt("programme_id")
                );
                // set display fields from JOIN
                offering.setCourseTitle(rs.getString("course_title"));
                offering.setCourseCode(rs.getString("course_code"));
                offering.setCreditHours(rs.getInt("credit_hours"));
                offering.setLecturerName(rs.getString("lecturer_title") + " " +
                                         rs.getString("first_name") + " " +
                                         rs.getString("last_name"));
                offerings.add(offering);
            }
            return offerings;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all offerings assigned to a specific lecturer in a semester
    // This is what lecturers see when they log in
    public List<CourseOffering> findOfferingsByLecturer(int lecturerId, int semesterId) {
        List<CourseOffering> offerings = new ArrayList<>();
        String sql = "SELECT co.offering_id, co.course_id, co.lecturer_id, " +
                     "co.semester_id, co.programme_id, " +
                     "c.title AS course_title, c.course_code, c.credit_hours, " +
                     "COUNT(e.enrollment_id) AS enrolled_count " +
                     "FROM course_offerings co " +
                     "JOIN courses c ON co.course_id = c.course_id " +
                     "LEFT JOIN enrollments e ON e.offering_id = co.offering_id AND e.is_active = TRUE " +
                     "WHERE co.lecturer_id = ? AND co.semester_id = ? " +
                     "GROUP BY co.offering_id";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, lecturerId);
            cursor.setInt(2, semesterId);
            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                CourseOffering offering = new CourseOffering(
                    rs.getInt("offering_id"),
                    rs.getInt("course_id"),
                    rs.getInt("lecturer_id"),
                    rs.getInt("semester_id"),
                    rs.getInt("programme_id")
                );
                offering.setCourseTitle(rs.getString("course_title"));
                offering.setCourseCode(rs.getString("course_code"));
                offering.setCreditHours(rs.getInt("credit_hours"));
                offering.setEnrolledStudentsCount(rs.getInt("enrolled_count"));
                offerings.add(offering);
            }
            return offerings;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public CourseOffering findOfferingById(int offeringId) {
        String sql = "SELECT * FROM course_offerings WHERE offering_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, offeringId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return new CourseOffering(
                    rs.getInt("offering_id"),
                    rs.getInt("course_id"),
                    rs.getInt("lecturer_id"),
                    rs.getInt("semester_id"),
                    rs.getInt("programme_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCourseOffering(CourseOffering offering) {
        String sql = "UPDATE course_offerings SET course_id = ?, lecturer_id = ?, " +
                     "semester_id = ?, programme_id = ? WHERE offering_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, offering.getCourseId());
            cursor.setInt(2, offering.getLecturerId());
            cursor.setInt(3, offering.getSemesterId());
            cursor.setInt(4, offering.getProgrammeId());
            cursor.setInt(5, offering.getOfferingId());

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourseOffering(int offeringId) {
        String sql = "DELETE FROM course_offerings WHERE offering_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, offeringId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Prevent assigning the same course to the same lecturer in the same semester twice
    public boolean offeringExists(int courseId, int lecturerId, int semesterId) {
        String sql = "SELECT COUNT(*) FROM course_offerings " +
                     "WHERE course_id = ? AND lecturer_id = ? AND semester_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, courseId);
            cursor.setInt(2, lecturerId);
            cursor.setInt(3, semesterId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean idExistsOffering(int offeringId) {
        String sql = "SELECT COUNT(*) FROM course_offerings WHERE offering_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, offeringId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
