package com.sys.dao;

import com.sys.model.Reservation;
import com.sys.model.Reservation.ReservationStatus;
import com.sys.utilities.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {

    // my helper raw mapper, i will move to utils
    private Reservation mapRow(ResultSet rs) throws SQLException {
        return new Reservation(
            rs.getInt("reservation_id"),
            rs.getInt("student_id"),
            rs.getInt("book_id"),
            rs.getDate("reserved_date").toLocalDate(),
            ReservationStatus.valueOf(rs.getString("status"))
        );
    }

    
    public boolean saveReservation(Reservation reservation) {//Save
        String sql = "INSERT INTO reservations (student_id, book_id, reserved_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setInt(1, reservation.getStudentId());
            cursor.setInt(2, reservation.getBookId());
            cursor.setDate(3, Date.valueOf(reservation.getReservedDate()));
            cursor.setString(4, reservation.getStatus().name());

            int rows = cursor.executeUpdate();
            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                reservation.setReservationId(keys.getInt(1));
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    
    public List<Reservation> findAllReservations() { //Find All
        String sql = "SELECT * FROM reservations";
        List<Reservation> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            ResultSet rs = cursor.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
        return list;
    }

    // Find By Student
    public List<Reservation> findReservationsByStudent(int studentId) {
        String sql = "SELECT * FROM reservations WHERE student_id = ?";
        List<Reservation> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            ResultSet rs = cursor.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
        return list;
    }

    // Find Pending By Book 
    // Called when a book is returned this query will help us to find  who is waiting check who is waiting
    // 
    public List<Reservation> findPendingReservationsByBook(int bookId) {
        String sql = "SELECT * FROM reservations WHERE book_id = ? AND status = 'PENDING' ORDER BY reserved_date ASC";
        List<Reservation> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, bookId);
            ResultSet rs = cursor.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
        return list;
    }

    // Find By ID
    public Reservation findReservationById(int reservationId) {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, reservationId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
        return null;
    }

    // Update Status 
    public boolean updateReservationStatus(Reservation reservation) {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, reservation.getStatus().name());
            cursor.setInt(2, reservation.getReservationId());

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    //  Delete
    public boolean deleteReservation(int reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, reservationId);
            return cursor.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    // Exists Checks reserve
    public boolean reservationExists(int studentId, int bookId) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE student_id = ? AND book_id = ? AND status = 'PENDING'";
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            cursor.setInt(2, bookId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean idExistsReservation(int reservationId) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, reservationId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
        return false;
    }
}