package com.sys.dao;

import com.sys.model.Programme;
import com.sys.model.Programme.LevelEnum;
import com.sys.utilities.DatabaseConnection;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class ProgrammeDao {

    public boolean saveProgramme(Programme programme) {
        String sql = "INSERT INTO programmes (name, code, level, duration_years, department_id) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setString(1, programme.getName());
            cursor.setString(2, programme.getProgrammeCode());
            cursor.setString(3, programme.getLevel().name());
            cursor.setInt(4, programme.getDurationYears());
            cursor.setInt(5, programme.getDepartmentId());

            int rows = cursor.executeUpdate();

       
            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                programme.setProgrammeId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Programme> findAllProgrammes() {
        List<Programme> programmes = new ArrayList<>();
        String sql = "SELECT * FROM programmes";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                programmes.add(mapRow(rs));
            }
            return programmes;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Programme findProgrammeById(int programmeId) {
        String sql = "SELECT * FROM programmes WHERE programme_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, programmeId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateProgramme(Programme programme) {
        String sql = "UPDATE programmes SET name = ?, code = ?, level = ?, " +
                     "duration_years = ?, department_id = ? " +
                     "WHERE programme_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, programme.getName());
            cursor.setString(2, programme.getProgrammeCode());
            cursor.setString(3, programme.getLevel().name());
            cursor.setInt(4, programme.getDurationYears());
            cursor.setInt(5, programme.getDepartmentId());
            cursor.setInt(6, programme.getProgrammeId()); 

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProgramme(int programmeId) {
        String sql = "DELETE FROM programmes WHERE programme_id = ?"; 

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, programmeId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean nameExistsProgramme(String name) {
        String sql = "SELECT COUNT(*) FROM programmes WHERE name = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, name);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean idExistsProgramme(int programmeId) {
        String sql = "SELECT COUNT(*) FROM programmes WHERE programme_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, programmeId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //  row mapper
    private Programme mapRow(ResultSet rs) throws SQLException {
        return new Programme(
            rs.getInt("programme_id"),
            rs.getString("name"),
            rs.getString("code"),
            LevelEnum.valueOf(rs.getString("level")),
            rs.getInt("duration_years"),
            rs.getInt("department_id")
        );
    }
}