package com.sys.dao;

import com.sys.model.Fine;
import com.sys.utilities.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FineDao {

    public boolean saveFine(Fine fine) {
        String sql = "INSERT INTO fines (borrowing_id, student_id, amount, is_paid, created_date, paid_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setInt(1, fine.getBorrowingId());
            cursor.setInt(2, fine.getStudentId());
            cursor.setFloat(3, fine.getAmount());
            cursor.setBoolean(4, fine.isPaid());
            cursor.setDate(5, Date.valueOf(fine.getCreatedDate()));
            if (fine.getPaidDate() != null) {
                cursor.setDate(6, Date.valueOf(fine.getPaidDate()));
            } else {
                cursor.setNull(6, Types.DATE);
            }

            int rows = cursor.executeUpdate();
            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                fine.setFineId(keys.getInt(1));
            }
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Fine> findAllFines() {
        List<Fine> fines = new ArrayList<>();
        String sql = "SELECT * FROM fines";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                Fine fine = new Fine(
                    rs.getInt("fine_id"),
                    rs.getInt("borrowing_id"),
                    rs.getInt("student_id"),
                    rs.getFloat("amount"),
                    rs.getBoolean("is_paid"),
                    rs.getDate("created_date").toLocalDate()  // no trailing comma
                );
                Date paidDate = rs.getDate("paid_date");
                if (paidDate != null) {
                    fine.setPaidDate(paidDate.toLocalDate()); //fixed capital P
                }
                fines.add(fine);
            }
            return fines;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Fine> findFinesByStudent(int studentId) {
        List<Fine> fines = new ArrayList<>();
        String sql = "SELECT * FROM fines WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            ResultSet rs = cursor.executeQuery();
            while (rs.next()) {
                Fine fine = new Fine(
                    rs.getInt("fine_id"),
                    rs.getInt("borrowing_id"),
                    rs.getInt("student_id"),
                    rs.getFloat("amount"),
                    rs.getBoolean("is_paid"),
                    rs.getDate("created_date").toLocalDate() );
                
                Date paidDate = rs.getDate("paid_date");
                if (paidDate != null) {
                    fine.setPaidDate(paidDate.toLocalDate());
                }
           
                fines.add(fine);
            }
            return fines;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Fine> findUnpaidFinesByStudent(int studentId) {
        List<Fine> fines = new ArrayList<>();
        String sql = "SELECT * FROM fines WHERE student_id = ? AND is_paid = FALSE";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, studentId);
            ResultSet rs = cursor.executeQuery();
            while (rs.next()) {
                Fine fine = new Fine(
                    rs.getInt("fine_id"),
                    rs.getInt("borrowing_id"),
                    rs.getInt("student_id"),
                    rs.getFloat("amount"),
                    rs.getBoolean("is_paid"),
                    rs.getDate("created_date").toLocalDate()
                );
                Date paidDate = rs.getDate("paid_date");
                if (paidDate != null) {
                    fine.setPaidDate(paidDate.toLocalDate());
                }
                fines.add(fine);
            }
            return fines;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Fine findFineById(int fineId) {
        String sql = "SELECT * FROM fines WHERE fine_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, fineId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                Fine fine = new Fine(
                    rs.getInt("fine_id"),
                    rs.getInt("borrowing_id"),
                    rs.getInt("student_id"),
                    rs.getFloat("amount"),
                    rs.getBoolean("is_paid"),
                    rs.getDate("created_date").toLocalDate()
                );
                Date paidDate = rs.getDate("paid_date");
                if (paidDate != null) {
                    fine.setPaidDate(paidDate.toLocalDate());
                }
                return fine;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Fine findFineByBorrowing(int borrowingId) {
        String sql = "SELECT * FROM fines WHERE borrowing_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, borrowingId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                Fine fine = new Fine(
                    rs.getInt("fine_id"),
                    rs.getInt("borrowing_id"),
                    rs.getInt("student_id"),
                    rs.getFloat("amount"),
                    rs.getBoolean("is_paid"),
                    rs.getDate("created_date").toLocalDate()
                );
                Date paidDate = rs.getDate("paid_date");
                if (paidDate != null) {
                    fine.setPaidDate(paidDate.toLocalDate());
                }
                return fine;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean markFineAsPaid(Fine fine) {
        String sql = "UPDATE fines SET is_paid = TRUE, paid_date = ? WHERE fine_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setDate(1, Date.valueOf(fine.getPaidDate()));
            cursor.setInt(2, fine.getFineId());
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFine(int fineId) {
        String sql = "DELETE FROM fines WHERE fine_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, fineId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean fineExistsForBorrowing(int borrowingId) {
        String sql = "SELECT COUNT(*) FROM fines WHERE borrowing_id = ?";

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

    public boolean idExistsFine(int fineId) {
        String sql = "SELECT COUNT(*) FROM fines WHERE fine_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, fineId);
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