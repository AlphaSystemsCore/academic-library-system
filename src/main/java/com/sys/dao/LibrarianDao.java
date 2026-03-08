package com.sys.dao;

import com.sys.model.Librarian;
import com.sys.utilities.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibrarianDao {

    public boolean saveLibrarian(Librarian librarian) {
        String sql = "INSERT INTO librarians (first_name, last_name, email, password, " +
                     "staff_number, role_id, identification_number, phone_number) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setString(1, librarian.getFirstName());
            cursor.setString(2, librarian.getLastName());
            cursor.setString(3, librarian.getEmail());
            cursor.setString(4, librarian.getPassword());
            cursor.setString(5, librarian.getStaffNumber());
            cursor.setInt(6, librarian.getRoleId());
            cursor.setString(7, librarian.getIdentificationNumber());
            cursor.setString(8, librarian.getPhoneNumber());

            int rows = cursor.executeUpdate();

            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                librarian.setLibrarianId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Librarian> findAllLibrarians() {
        List<Librarian> librarians = new ArrayList<>();
        String sql = "SELECT * FROM librarians";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                librarians.add(mapRow(rs));
            }
            return librarians;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Librarian findLibrarianById(int librarianId) {
        String sql = "SELECT * FROM librarians WHERE librarian_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, librarianId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Librarian findLibrarianByEmail(String email) {
        String sql = "SELECT * FROM librarians WHERE email = ?";

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

    public boolean updateLibrarian(Librarian librarian) {
        String sql = "UPDATE librarians SET first_name = ?, last_name = ?, email = ?, " +
                     "staff_number = ?, identification_number = ?, phone_number = ? " +
                     "WHERE librarian_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, librarian.getFirstName());
            cursor.setString(2, librarian.getLastName());
            cursor.setString(3, librarian.getEmail());
            cursor.setString(4, librarian.getStaffNumber());
            cursor.setString(5, librarian.getIdentificationNumber());
            cursor.setString(6, librarian.getPhoneNumber());
            cursor.setInt(7, librarian.getLibrarianId());

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLibrarian(int librarianId) {
        String sql = "DELETE FROM librarians WHERE librarian_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, librarianId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExistsLibrarian(String email) {
        String sql = "SELECT COUNT(*) FROM librarians WHERE email = ?";

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

    public boolean staffNumberExistsLibrarian(String staffNumber) {
        String sql = "SELECT COUNT(*) FROM librarians WHERE staff_number = ?";

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

    public boolean idExistsLibrarian(int librarianId) {
        String sql = "SELECT COUNT(*) FROM librarians WHERE librarian_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, librarianId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Librarian mapRow(ResultSet rs) throws SQLException {
        return new Librarian(
            rs.getInt("librarian_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("staff_number"),
            rs.getInt("role_id"),
            rs.getString("identification_number"),
            rs.getString("phone_number")
        );
    }
}
