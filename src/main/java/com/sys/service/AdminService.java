package com.sys.service;

import java.util.List;
import com.sys.dao.AdminDao;
import com.sys.dao.RolesDao;
import com.sys.model.Admin;

public class AdminService {

    private final AdminDao adminDao = new AdminDao();
    private final RolesDao rolesDao = new RolesDao();

    public void addAdmin(String firstName, String lastName, String email,
                         String identificationNumber, String phoneNumber,
                         int roleId, String password, String adminNumber) { //frontend parameters

        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be blank.");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be blank.");
        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");
        if (identificationNumber == null || identificationNumber.isBlank() || identificationNumber.length() > 10)
            throw new IllegalArgumentException("Invalid identification number.");
        if (phoneNumber == null || phoneNumber.isBlank() || phoneNumber.length() > 16)
            throw new IllegalArgumentException("Invalid phone number.");
        if (adminNumber == null || adminNumber.isBlank())
            throw new IllegalArgumentException("Admin number cannot be blank.");

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{16,}$";
        if (password == null || !password.matches(passwordPattern))
            throw new IllegalArgumentException("Password must be at least 16 characters with uppercase, lowercase, number and special character.");

        if (roleId < 1)
            throw new IllegalArgumentException("Role ID must be a positive number.");
        if (!rolesDao.idExistsRole(roleId))
            throw new IllegalArgumentException("Role does not exist.");
        if (adminDao.emailExistsAdmin(email))
            throw new IllegalArgumentException("Email is already taken.");

        Admin admin = new Admin(firstName, lastName, email, identificationNumber,
                phoneNumber, roleId, password, adminNumber);

        boolean saved = adminDao.saveAdmin(admin);
        if (!saved)
            throw new RuntimeException("Failed to save admin. Please try again.");
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = adminDao.findAllAdmins();
        if (admins.isEmpty())
            throw new IllegalStateException("No admins found.");
        return admins;
    }

    public Admin getAdminById(int adminId) {  //frontend parameters
        if (adminId < 1)
            throw new IllegalArgumentException("Admin ID must be a positive number.");
        Admin admin = adminDao.findAdminById(adminId);
        if (admin == null)
            throw new IllegalStateException("Admin not found for ID: " + adminId);
        return admin;
    }

    public Admin getAdminByEmail(String email) {  //frontend parameters
        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");
        Admin admin = adminDao.findAdminByEmail(email);
        if (admin == null)
            throw new IllegalStateException("No admin found for email: " + email);
        return admin;
    }

    public void updateAdmin(int adminId, String firstName, String lastName, String email,  //frontend parameters
                            String identificationNumber, String phoneNumber) {

        if (adminId < 1)
            throw new IllegalArgumentException("Admin ID must be a positive number.");
        if (!adminDao.idExistsAdmin(adminId))
            throw new IllegalArgumentException("Admin not found.");
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be blank.");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be blank.");
        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");
        if (identificationNumber == null || identificationNumber.isBlank() || identificationNumber.length() > 10)
            throw new IllegalArgumentException("Invalid identification number.");
        if (phoneNumber == null || phoneNumber.isBlank() || phoneNumber.length() > 16)
            throw new IllegalArgumentException("Invalid phone number.");

        Admin existingByEmail = adminDao.findAdminByEmail(email);
        if (existingByEmail != null && existingByEmail.getAdminId() != adminId)
            throw new IllegalArgumentException("Email is already taken by another admin.");

        boolean updated = adminDao.updateAdmin(adminId, firstName, lastName, email, identificationNumber, phoneNumber);

        
        if (!updated)
            throw new RuntimeException("Failed to update admin. Please try again.");
    }

    public void deleteAdmin(int adminId) {  //frontend parameters
        if (adminId < 1)
            throw new IllegalArgumentException("Admin ID must be a positive number.");
        if (!adminDao.idExistsAdmin(adminId))
            throw new IllegalArgumentException("Admin not found.");
        boolean deleted = adminDao.deleteAdmin(adminId);
        if (!deleted)
            throw new RuntimeException("Failed to delete admin. Please try again.");
    }
}
