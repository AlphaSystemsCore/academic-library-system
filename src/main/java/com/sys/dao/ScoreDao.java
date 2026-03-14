package com.sys.dao;

import com.sys.model.Score;
import com.sys.utilities.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScoreDao {

    public boolean saveScore(Score score) {
        String sql = "INSERT INTO scores (enrollment_id, cat_score, exam_score, total_score, " +
                     "letter_grade, grade_point, academic_year, submitted_by, submitted_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setInt(1, score.getEnrollmentId());
            cursor.setFloat(2, score.getCatScore());
            cursor.setFloat(3, score.getExamScore());
            cursor.setFloat(4, score.getTotalScore());
            cursor.setString(5, String.valueOf(score.getLetterGrade())); // converting >>> String for CHAR(1)
            cursor.setFloat(6, score.getGradePoint());
            cursor.setInt(7, score.getAcademicYear());
            cursor.setInt(8, score.getSubmittedBy());
            cursor.setDate(9, Date.valueOf(score.getSubmittedDate()));

            int rows = cursor.executeUpdate();

            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                score.setScoreId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Score> findAllScores() {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT * FROM scores";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                scores.add(mapRow(rs));
            }
            return scores;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get score for a specific enrollment
    public Score findScoreByEnrollment(int enrollmentId) {
        String sql = "SELECT * FROM scores WHERE enrollment_id = ?";

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

    // Get all scores for a student across all enrollments — used for GPA calculation
    public List<Score> findScoresByStudent(int studentId) {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT s.* FROM scores s " +
                     "JOIN enrollments e ON s.enrollment_id = e.enrollment_id " +
                     "WHERE e.student_id = ? AND e.is_counted_in_gpa = TRUE";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                scores.add(mapRow(rs));
            }
            return scores;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all scores for a specific course offering — used by lecturer
    public List<Score> findScoresByOffering(int offeringId) {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT s.* FROM scores s " +
                     "JOIN enrollments e ON s.enrollment_id = e.enrollment_id " +
                     "WHERE e.offering_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, offeringId);
            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                scores.add(mapRow(rs));
            }
            return scores;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Score findScoreById(int scoreId) {
        String sql = "SELECT * FROM scores WHERE score_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, scoreId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lecturer updates a score they already submitted
    public boolean updateScore(int scoreId, float catScore,float totalScore, float examScore, char letterGrade, float gradePoint, LocalDate submittedDate, int submittedBy) {
        String sql = "UPDATE scores SET cat_score = ?, exam_score = ?, total_score = ?, " +
                     "letter_grade = ?, grade_point = ?, submitted_by = ?, submitted_date = ? " +
                     "WHERE score_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setFloat(1, catScore);
            cursor.setFloat(2, examScore);
            cursor.setFloat(3, totalScore);
            cursor.setString(4, String.valueOf(letterGrade));
            cursor.setFloat(5, gradePoint);
            cursor.setInt(6, submittedBy);
            cursor.setDate(7, Date.valueOf(submittedDate));
            cursor.setInt(8, scoreId);

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteScore(int scoreId) {
        String sql = "DELETE FROM scores WHERE score_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, scoreId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if score already exists for an enrollment — prevent duplicate submission
    public boolean scoreExistsForEnrollment(int enrollmentId) {
        String sql = "SELECT COUNT(*) FROM scores WHERE enrollment_id = ?";

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

    private Score mapRow(ResultSet rs) throws SQLException {
        Score score = new Score(
            rs.getInt("score_id"),
            rs.getInt("enrollment_id"),
            rs.getFloat("cat_score"),
            rs.getFloat("exam_score"),
            rs.getInt("academic_year"),
            rs.getInt("submitted_by")
        );
        // Override submitted date with what's stored in DB
        score.setSubmittedDate(rs.getDate("submitted_date").toLocalDate());
        return score;
    }
}
