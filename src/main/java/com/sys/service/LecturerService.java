package com.sys.service;

import java.util.List;
import com.sys.dao.DepartmentDao;
import com.sys.dao.LecturerDao;
import com.sys.dao.RolesDao;
import com.sys.model.Lecturer;

public class LecturerService {

    private final LecturerDao lecturerDao = new LecturerDao();
    private final DepartmentDao departmentDao = new DepartmentDao();
    private final RolesDao rolesDao = new RolesDao();


    //  ADD LECTURER
  
    public void addLecturer(String firstName, String lastName, String email,
                            String title, String identificationNumber,
                            String phoneNumber, String staffNumber,
                            int departmentId, int roleId, String password) { //fronend

        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be blank.");

        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be blank.");

        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");

        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title cannot be blank (e.g. Dr, Prof, Mr).");

        if (identificationNumber == null || identificationNumber.isBlank() || identificationNumber.length() > 10)
            throw new IllegalArgumentException("Invalid identification number.");

        if (phoneNumber == null || phoneNumber.isBlank() || phoneNumber.length() > 16)
            throw new IllegalArgumentException("Invalid phone number.");

        if (staffNumber == null || staffNumber.isBlank())
            throw new IllegalArgumentException("Staff number cannot be blank.");

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{16,}$";
        if (password == null || !password.matches(passwordPattern))
            throw new IllegalArgumentException("Password must be at least 16 characters with uppercase, lowercase, number and special character.");

        if (departmentId < 1)
            throw new IllegalArgumentException("Department ID must be a positive number.");

        if (roleId < 1)
            throw new IllegalArgumentException("Role ID must be a positive number.");

        if (!departmentDao.idExistsDepartment(departmentId))
            throw new IllegalArgumentException("Department does not exist.");

        if (!rolesDao.idExistsRole(roleId))
            throw new IllegalArgumentException("Role does not exist.");

        if (lecturerDao.emailExistsLecturer(email))
            throw new IllegalArgumentException("Email is already taken.");

        if (lecturerDao.staffNumberExistsLecturer(staffNumber))
            throw new IllegalArgumentException("Staff number already exists.");

        Lecturer lecturer = new Lecturer(firstName, lastName, email, title,
                identificationNumber, phoneNumber, staffNumber, departmentId, roleId, password);

        boolean saved = lecturerDao.saveLecturer(lecturer);
        if (!saved)
            throw new RuntimeException("Failed to save lecturer. Please try again.");
    }

    //  GET ALL LECTURERS frontend
  
    public List<Lecturer> getAllLecturers() {
        List<Lecturer> lecturers = lecturerDao.findAllLecturers();
        if (lecturers.isEmpty())
            throw new IllegalStateException("No lecturers found.");
        return lecturers;
    }


    //  GET BY DEPARTMENT
  
    public List<Lecturer> getLecturersByDepartment(int departmentId) { // frontend
        if (departmentId < 1)
            throw new IllegalArgumentException("Department ID must be a positive number.");

        if (!departmentDao.idExistsDepartment(departmentId))
            throw new IllegalArgumentException("Department does not exist.");

        List<Lecturer> lecturers = lecturerDao.findLecturersByDepartment(departmentId);
        if (lecturers.isEmpty())
            throw new IllegalStateException("No lecturers found in this department.");
        return lecturers;
    }

 
    //  GET BY ID
    
    public Lecturer getLecturerById(int lecturerId) { // frontend
        if (lecturerId < 1)
            throw new IllegalArgumentException("Lecturer ID must be a positive number.");

        Lecturer lecturer = lecturerDao.findLecturerById(lecturerId);
        if (lecturer == null)
            throw new IllegalStateException("Lecturer not found for ID: " + lecturerId);
        return lecturer;
    }

   
    //  GET BY EMAIL  -
    
    public Lecturer getLecturerByEmail(String email) { //--login frontend
        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");

        Lecturer lecturer = lecturerDao.findLecturerByEmail(email);
        if (lecturer == null)
            throw new IllegalStateException("No lecturer found for email: " + email);
        return lecturer;
    }

  
    //  UPDATE LECTURER
   
    public void updateLecturer(int lecturerId, String firstName, String lastName,
                               String email, String title, String identificationNumber,
                               String phoneNumber, String staffNumber, int departmentId) { //frontend admin

        if (lecturerId < 1)
            throw new IllegalArgumentException("Lecturer ID must be a positive number.");

        if (!lecturerDao.idExistsLecturer(lecturerId))
            throw new IllegalArgumentException("Lecturer not found.");

        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be blank.");

        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be blank.");

        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");

        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title cannot be blank.");

        if (identificationNumber == null || identificationNumber.isBlank() || identificationNumber.length() > 10)
            throw new IllegalArgumentException("Invalid identification number.");

        if (phoneNumber == null || phoneNumber.isBlank() || phoneNumber.length() > 16)
            throw new IllegalArgumentException("Invalid phone number.");

        if (staffNumber == null || staffNumber.isBlank())
            throw new IllegalArgumentException("Staff number cannot be blank.");

        if (departmentId < 1)
            throw new IllegalArgumentException("Department ID must be a positive number.");

        if (!departmentDao.idExistsDepartment(departmentId))
            throw new IllegalArgumentException("Department does not exist.");

        // Allows same email only if it belongs to THIS lecturer
        Lecturer existingByEmail = lecturerDao.findLecturerByEmail(email);
        if (existingByEmail != null && existingByEmail.getLecturerId() != lecturerId)
            throw new IllegalArgumentException("Email is already taken by another lecturer.");

        // Lecturer lecturer = lecturerDao.findLecturerById(lecturerId);

        boolean updated = lecturerDao.updateLecturer(lecturerId, firstName, lastName, email, title, identificationNumber, phoneNumber, staffNumber, departmentId);
        if (!updated)
            throw new RuntimeException("Failed to update lecturer. Please try again.");
    }

  
    //  DELETE LECTURER
    
    public void deleteLecturer(int lecturerId) {
        if (lecturerId < 1)
            throw new IllegalArgumentException("Lecturer ID must be a positive number.");

        if (!lecturerDao.idExistsLecturer(lecturerId))
            throw new IllegalArgumentException("Lecturer not found.");

        boolean deleted = lecturerDao.deleteLecturer(lecturerId);
        if (!deleted)
            throw new RuntimeException("Failed to delete lecturer. Please try again.");
    }
}
