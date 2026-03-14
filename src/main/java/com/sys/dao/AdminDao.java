package com.sys.dao;

import com.sys.model.Admin;
import com.sys.utilities.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {

    public boolean saveAdmin(Admin admin) {
        String sql = "INSERT INTO admins (first_name, last_name, email, identification_number, " +
                     "phone_number, role_id, password, adminNumber) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cursor.setString(1, admin.getFirstName());
            cursor.setString(2, admin.getLastName());
            cursor.setString(3, admin.getEmail());
            cursor.setString(4, admin.getIdentificationNumber());
            cursor.setString(5, admin.getPhoneNumber());
            cursor.setInt(6, admin.getRoleId());
            cursor.setString(7, admin.getPassword());
            cursor.setString(8, admin.getAdminNumber());

            int rows = cursor.executeUpdate();

            ResultSet keys = cursor.getGeneratedKeys();
            if (keys.next()) {
                admin.setAdminId(keys.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Admin> findAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM admins";

        try (Connection conn = DatabaseConnection.createConnection();
             Statement cursor = conn.createStatement()) {

            ResultSet rs = cursor.executeQuery(sql);
            while (rs.next()) {
                admins.add(mapRow(rs));
            }
            return admins;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Admin findAdminById(int adminId) { 
        String sql = "SELECT * FROM admins WHERE admin_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, adminId);
            ResultSet rs = cursor.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Admin findAdminByEmail(String email) {
        String sql = "SELECT * FROM admins WHERE email = ?";

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

    public boolean updateAdmin(int adminId, String firstName, String lastName, String email,
                            String identificationNumber, String phoneNumber) {
        String sql = "UPDATE admins SET first_name = ?, last_name = ?, email = ?, " +
                     "identification_number = ?, phone_number = ? WHERE admin_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setString(1, firstName);
            cursor.setString(2, lastName);
            cursor.setString(3, email);
            cursor.setString(4, identificationNumber);
            cursor.setString(5, phoneNumber);
            cursor.setInt(6, adminId);

            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAdmin(int adminId) {
        String sql = "DELETE FROM admins WHERE admin_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, adminId);
            int rows = cursor.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExistsAdmin(String email) {
        String sql = "SELECT COUNT(*) FROM admins WHERE email = ?";

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

    public boolean idExistsAdmin(int adminId) {
        String sql = "SELECT COUNT(*) FROM admins WHERE admin_id = ?";

        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement cursor = conn.prepareStatement(sql)) {

            cursor.setInt(1, adminId);
            ResultSet rs = cursor.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Admin mapRow(ResultSet rs) throws SQLException {
        return new Admin(
            rs.getInt("admin_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("identification_number"),
            rs.getString("phone_number"),
            rs.getInt("role_id"),
            rs.getString("password"),
            rs.getString("adminNumber")
        );
    }
}
