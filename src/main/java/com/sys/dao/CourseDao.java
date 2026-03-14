package com.sys.dao;

import com.sys.model.Course;
import com.sys.utilities.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    public boolean saveCourse(Course course) {
        String sql = "INSERT INTO courses (course_code, title, credit_hours, programme_id) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setString(1, course.getCourseCode());
            cursor.setString(2, course.getTitle());
            cursor.setInt(3, course.getCreditHours());
            cursor.setInt(4, course.getProgrammeId());

            int rows = cursor.executeUpdate();

            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                course.setCourseId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Course> findAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                courses.add(new Course(
                    rs.getInt("course_id"),
                    rs.getString("course_code"),
                    rs.getString("title"),
                    rs.getInt("credit_hours"),
                    rs.getInt("programme_id")
                ));
            }
            return courses;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all courses belonging to a specific programme
    public List<Course> findCoursesByProgramme(int programmeId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE programme_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, programmeId);
            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                courses.add(new Course(
                    rs.getInt("course_id"),
                    rs.getString("course_code"),
                    rs.getString("title"),
                    rs.getInt("credit_hours"),
                    rs.getInt("programme_id")
                ));
            }
            return courses;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Course findCourseById(int courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, courseId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return new Course(
                    rs.getInt("course_id"),
                    rs.getString("course_code"),
                    rs.getString("title"),
                    rs.getInt("credit_hours"),
                    rs.getInt("programme_id")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCourse(int courseId, String courseCode, String title,
                             int creditHours, int programmeId) {
        String sql = "UPDATE courses SET course_code = ?, title = ?, credit_hours = ?, " +
                     "programme_id = ? WHERE course_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, courseCode);
            cursor.setString(2, title);
            cursor.setInt(3, creditHours);
            cursor.setInt(4, programmeId);
            cursor.setInt(5, courseId);

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, courseId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean courseCodeExists(String courseCode) {
        String sql = "SELECT COUNT(*) FROM courses WHERE course_code = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, courseCode);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean idExistsCourse(int courseId) {
        String sql = "SELECT COUNT(*) FROM courses WHERE course_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, courseId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean hasProgramme(int programmeId){
        String sql = "SELECT COUNT(*) FROM courses WHERE programme_id = ? ";
        try (Connection conn = DatabaseConnection.createConnection();
            PreparedStatement cursor = conn.prepareStatement(sql)) {
                cursor.setInt(1, programmeId);
                ResultSet rs = cursor.executeQuery();
                if(rs.next() )
                    return rs.getInt(1) > 0;

            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

