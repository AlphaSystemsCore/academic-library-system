package com.sys.service;

import java.util.List;
import com.sys.dao.LibrarianDao;
import com.sys.dao.RolesDao;
import com.sys.model.Librarian;

public class LibrarianService {

    private final LibrarianDao librarianDao = new LibrarianDao();
    private final RolesDao rolesDao = new RolesDao();


    //  ADD LIBRARIAN
  
    public void addLibrarian(String firstName, String lastName, String email,
                             String password, String staffNumber, int roleId,
                             String identificationNumber, String phoneNumber) {

        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be blank.");

        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be blank.");

        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");

        if (staffNumber == null || staffNumber.isBlank())
            throw new IllegalArgumentException("Staff number cannot be blank.");

        if (identificationNumber == null || identificationNumber.isBlank() || identificationNumber.length() > 10)
            throw new IllegalArgumentException("Invalid identification number.");

        if (phoneNumber == null || phoneNumber.isBlank() || phoneNumber.length() > 16)
            throw new IllegalArgumentException("Invalid phone number.");

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{16,}$";
        if (password == null || !password.matches(passwordPattern))
            throw new IllegalArgumentException("Password must be at least 16 characters with uppercase, lowercase, number and special character.");

        if (roleId < 1)
            throw new IllegalArgumentException("Role ID must be a positive number.");

        if (!rolesDao.idExistsRole(roleId))
            throw new IllegalArgumentException("Role does not exist.");

        if (librarianDao.emailExistsLibrarian(email))
            throw new IllegalArgumentException("Email is already taken.");

        if (librarianDao.staffNumberExistsLibrarian(staffNumber))
            throw new IllegalArgumentException("Staff number already exists.");

        Librarian librarian = new Librarian(firstName, lastName, email, password,
                staffNumber, roleId, identificationNumber, phoneNumber);

        boolean saved = librarianDao.saveLibrarian(librarian);
        if (!saved)
            throw new RuntimeException("Failed to save librarian. Please try again.");
    }


    //  GET ALL LIBRARIANS

    public List<Librarian> getAllLibrarians() {
        List<Librarian> librarians = librarianDao.findAllLibrarians();
        if (librarians.isEmpty())
            throw new IllegalStateException("No librarians found.");
        return librarians;
    }


    //  GET BY ID

    public Librarian getLibrarianById(int librarianId) {
        if (librarianId < 1)
            throw new IllegalArgumentException("Librarian ID must be a positive number.");

        Librarian librarian = librarianDao.findLibrarianById(librarianId);
        if (librarian == null)
            throw new IllegalStateException("Librarian not found for ID: " + librarianId);
        return librarian;
    }

  
    //  GET BY EMAIL

    public Librarian getLibrarianByEmail(String email) {
        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");

        Librarian librarian = librarianDao.findLibrarianByEmail(email);
        if (librarian == null)
            throw new IllegalStateException("No librarian found for email: " + email);
        return librarian;
    }


    //  UPDATE LIBRARIAN

    public void updateLibrarian(int librarianId, String firstName, String lastName,
                                String email, String staffNumber,
                                String identificationNumber, String phoneNumber) {

        if (librarianId < 1)
            throw new IllegalArgumentException("Librarian ID must be a positive number.");

        if (!librarianDao.idExistsLibrarian(librarianId))
            throw new IllegalArgumentException("Librarian not found.");

        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be blank.");

        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be blank.");

        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");

        if (staffNumber == null || staffNumber.isBlank())
            throw new IllegalArgumentException("Staff number cannot be blank.");

        if (identificationNumber == null || identificationNumber.isBlank() || identificationNumber.length() > 10)
            throw new IllegalArgumentException("Invalid identification number.");

        if (phoneNumber == null || phoneNumber.isBlank() || phoneNumber.length() > 16)
            throw new IllegalArgumentException("Invalid phone number.");

        // Allow same email only if it belongs to THIS librarian
        Librarian existingByEmail = librarianDao.findLibrarianByEmail(email);
        if (existingByEmail != null && existingByEmail.getLibrarianId() != librarianId)
            throw new IllegalArgumentException("Email is already taken by another librarian.");

        Librarian librarian = librarianDao.findLibrarianById(librarianId);
        librarian.setFirstName(firstName);
        librarian.setLastName(lastName);
        librarian.setEmail(email);
        librarian.setStaffNumber(staffNumber);
        librarian.setIdentificationNumber(identificationNumber);
        librarian.setPhoneNumber(phoneNumber);

        boolean updated = librarianDao.updateLibrarian(librarian);
        if (!updated)
            throw new RuntimeException("Failed to update librarian. Please try again.");
    }


    //  DELETE LIBRARIAN

    public void deleteLibrarian(int librarianId) {
        if (librarianId < 1)
            throw new IllegalArgumentException("Librarian ID must be a positive number.");

        if (!librarianDao.idExistsLibrarian(librarianId))
            throw new IllegalArgumentException("Librarian not found.");

        boolean deleted = librarianDao.deleteLibrarian(librarianId);
        if (!deleted)
            throw new RuntimeException("Failed to delete librarian. Please try again.");
    }
}
