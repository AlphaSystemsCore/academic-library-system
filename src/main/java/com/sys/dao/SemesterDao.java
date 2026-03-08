package com.sys.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import com.sys.utilities.DatabaseConnection;
import com.sys.model.Semester;

public class SemesterDao {

    public boolean saveSemester(Semester semester) {
        String sql = "INSERT INTO semesters (academic_year, semester_number, start_date, end_date, is_current) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setInt(1, semester.getAcademicYear());
            cursor.setInt(2, semester.getSemesterNumber());
            cursor.setDate(3, Date.valueOf(semester.getStartDate()));
            cursor.setDate(4, Date.valueOf(semester.getEndDate()));
            cursor.setBoolean(5, semester.getCurrent());

            int rows = cursor.executeUpdate();

            
            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                semester.setSemesterId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Semester> findAllSemester() {
        List<Semester> semesters = new ArrayList<>();
        String sql = "SELECT * FROM semesters";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                semesters.add(mapRow(rs));
            }
            return semesters;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Semester findByIdSemester(int semesterId) {
        String sql = "SELECT * FROM semesters WHERE semester_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, semesterId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Semester findCurrentSemester() {
        String sql = "SELECT * FROM semesters WHERE is_current = TRUE LIMIT 1";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateSemester(Semester semester) {
        String sql = "UPDATE semesters SET academic_year = ?, semester_number = ?, " +
                     "start_date = ?, end_date = ?, is_current = ? " +
                     "WHERE semester_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, semester.getAcademicYear());
            cursor.setInt(2, semester.getSemesterNumber());
            cursor.setDate(3, Date.valueOf(semester.getStartDate()));
            cursor.setDate(4, Date.valueOf(semester.getEndDate()));
            cursor.setBoolean(5, semester.getCurrent());
            cursor.setInt(6, semester.getSemesterId());

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean setCurrentSemester(int semesterId) {
        String resetSql = "UPDATE semesters SET is_current = FALSE";
    
        String activateSql = "UPDATE semesters SET is_current = TRUE WHERE semester_id = ?";

        try (Connection conn = DatabaseConnection.createConnection()) {
            conn.setAutoCommit(false); 

            try (Statement resetCursor = conn.createStatement();
                 PreparedStatement activateCursor = conn.prepareStatement(activateSql)) {

                resetCursor.executeUpdate(resetSql);
                activateCursor.setInt(1, semesterId);
                activateCursor.executeUpdate();

                conn.commit(); 
                return true;

            } catch (SQLException e) {
                conn.rollback(); 
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSemester(int semesterId) {
        String sql = "DELETE FROM semesters WHERE semester_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, semesterId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean idExistsSemester(int semesterId) {
        String sql = "SELECT COUNT(*) FROM semesters WHERE semester_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, semesterId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    private Semester mapRow(ResultSet rs) throws SQLException {
        return new Semester(
            rs.getInt("semester_id"),
            rs.getInt("academic_year"),
            rs.getInt("semester_number"),
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("end_date").toLocalDate(),
            rs.getBoolean("is_current")
        );
    }
}