package com.sys.dao;

import com.sys.model.Borrowing;
import com.sys.model.Borrowing.StatusEnum;
import com.sys.utilities.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDao {

    public boolean saveBorrowing(Borrowing borrowing) {
        String sql = "INSERT INTO borrowings (student_id, book_id, librarian_id, " +
                     "borrow_date, due_date, return_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setInt(1, borrowing.getStudentId());
            cursor.setInt(2, borrowing.getBookId());
            cursor.setInt(3, borrowing.getLibrarianId());
            cursor.setDate(4, Date.valueOf(borrowing.getBorrowDate()));
            cursor.setDate(5, Date.valueOf(borrowing.getDueDate()));
            // return_date is null when borrowing — handle null safely
            if (borrowing.getReturnDate() != null) {
                cursor.setDate(6, Date.valueOf(borrowing.getReturnDate()));
            } else {
                cursor.setNull(6, Types.DATE);
            }
            cursor.setString(7, borrowing.getStatus().name());

            int rows = cursor.executeUpdate();

            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                borrowing.setBorrowingId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Borrowing> findAllBorrowings() {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT * FROM borrowings";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                borrowings.add(mapRow(rs));
            }
            return borrowings;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all borrowings for a specific student
    public List<Borrowing> findBorrowingsByStudent(int studentId) {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT * FROM borrowings WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            ResultSet rs = cursor.executeQuery();

            while (rs.next()) {
                borrowings.add(mapRow(rs));
            }
            return borrowings;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all currently borrowed (not yet returned) books
    public List<Borrowing> findActiveBorrowings() {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT * FROM borrowings WHERE status = 'BORROWED' OR status = 'OVERDUE'";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                borrowings.add(mapRow(rs));
            }
            return borrowings;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Get all overdue borrowings
    public List<Borrowing> findOverdueBorrowings() {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT * FROM borrowings WHERE status = 'BORROWED' AND due_date < CURDATE()";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                borrowings.add(mapRow(rs));
            }
            return borrowings;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Borrowing findBorrowingById(int borrowingId) {
        String sql = "SELECT * FROM borrowings WHERE borrowing_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, borrowingId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Called when a book is returned — updates return_date and status
    public boolean updateBorrowingOnReturn(Borrowing borrowing) {
        String sql = "UPDATE borrowings SET return_date = ?, status = ? WHERE borrowing_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setDate(1, Date.valueOf(borrowing.getReturnDate()));
            cursor.setString(2, borrowing.getStatus().name());
            cursor.setInt(3, borrowing.getBorrowingId());

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mark all overdue borrowings — run daily or on login
    public boolean markOverdueBorrowings() {
        String sql = "UPDATE borrowings SET status = 'OVERDUE' " +
                     "WHERE status = 'BORROWED' AND due_date < CURDATE()";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            cursor.executeUpdate(sql);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBorrowing(int borrowingId) {
        String sql = "DELETE FROM borrowings WHERE borrowing_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, borrowingId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean idExistsBorrowing(int borrowingId) {
        String sql = "SELECT COUNT(*) FROM borrowings WHERE borrowing_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, borrowingId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Borrowing mapRow(ResultSet rs) throws SQLException {
        Borrowing borrowing = new Borrowing(
            rs.getInt("borrowing_id"),
            rs.getInt("student_id"),
            rs.getInt("book_id"),
            rs.getInt("librarian_id"),
            rs.getDate("borrow_date").toLocalDate()
        );
        // Override status from DB
        borrowing.setStatus(StatusEnum.valueOf(rs.getString("status")));
        // return_date may be null if not yet returned
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            borrowing.setReturnDate(returnDate.toLocalDate());
        }
        return borrowing;
    }
}
