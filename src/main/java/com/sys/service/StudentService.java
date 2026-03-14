package com.sys.service;

import java.util.List;
import java.time.LocalDate;
import com.sys.dao.ProgrammeDao;
import com.sys.dao.RolesDao;
import com.sys.dao.StudentDao;
import com.sys.model.Programme;
import com.sys.model.Student;

public class StudentService {

    private final ProgrammeDao programmeDao = new ProgrammeDao();
    private final RolesDao rolesDao = new RolesDao();
    private final StudentDao studentDao = new StudentDao();

    public void addStudent(String firstName, String lastName, String email,
                           String phoneNumber, String identificationNumber,
                           String password, int programmeId, int roleId) {

        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be blank.");

        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be blank.");

        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");

        if (phoneNumber == null || phoneNumber.isBlank() || phoneNumber.length() > 16)
            throw new IllegalArgumentException("Invalid phone number.");

        if (identificationNumber == null || identificationNumber.isBlank() || identificationNumber.length() > 10)
            throw new IllegalArgumentException("Invalid identification number.");

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{16,}$";
        if (password == null || !password.matches(passwordPattern))
            throw new IllegalArgumentException("Password must be at least 16 characters with uppercase, lowercase, number and special character.");

        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");

        if (roleId < 1)
            throw new IllegalArgumentException("Role ID must be a positive number.");

        if (!rolesDao.idExistsRole(roleId))
            throw new IllegalArgumentException("Role ID does not exist.");

        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme ID does not exist.");

        if (studentDao.emailExistsStudent(email))
            throw new IllegalArgumentException("Email is already taken.");

        if (studentDao.idNoExistsStudent(identificationNumber))
            throw new IllegalArgumentException("Identification number already exists.");

        if (studentDao.existsPhoneNumber(phoneNumber))
            throw new IllegalArgumentException("Phone number already exists.");

        
        LocalDate registerDate = LocalDate.now();

        
        Programme programme = programmeDao.findProgrammeById(programmeId);
        String code = programme.getProgrammeCode();
        int count = studentDao.countStudents();
        String admissionNumber = code + "/" + LocalDate.now().getYear() + "/" + String.format("%04d", count + 1);

        Student student = new Student(firstName, lastName, email, phoneNumber,
                identificationNumber, password, programmeId, admissionNumber, roleId, registerDate);

        boolean saved = studentDao.saveStudent(student);
        if (!saved)
            throw new RuntimeException("Failed to save student. Please try again.");
    }

    
    //  GET ALL STUDENTS
  
    public List<Student> getAllStudents() {
        List<Student> students = studentDao.findAllStudents();
        if (students.isEmpty())
            throw new IllegalStateException("No students found.");
        return students;
    }

   
    //  GET BY PROGRAMME
    
    public List<Student> getStudentsByProgramme(int programmeId) {
        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");

        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme does not exist.");

        List<Student> students = studentDao.findStudentsByProgramme(programmeId);
        if (students.isEmpty())
            throw new IllegalStateException("No students found for this programme.");
        return students;
    }

    
    //  GET BY ID
 
    public Student getStudentById(int studentId) {
        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");

        Student student = studentDao.findStudentById(studentId);
        if (student == null)
            throw new IllegalStateException("Student not found for ID: " + studentId);
        return student;
    }

   
    //  GET BY ADMISSION NUMBER
   
    public Student getStudentByAdmissionNumber(String admissionNumber) {
        if (admissionNumber == null || admissionNumber.isBlank())
            throw new IllegalArgumentException("Admission number cannot be blank.");

        Student student = studentDao.findStudentByAdmissionNumber(admissionNumber);
        if (student == null)
            throw new IllegalStateException("No student found for admission number: " + admissionNumber);
        return student;
    }

   
    //  GET BY EMAIL
  
    public Student getStudentByEmail(String email) {
        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");

        Student student = studentDao.findStudentByEmail(email);
        if (student == null)
            throw new IllegalStateException("No student found for email: " + email);
        return student;
    }

  
    //  UPDATE STUDENT
   
    public void updateStudent(int studentId, String firstName, String lastName,
                              String email, String phoneNumber,
                              String identificationNumber, int programmeId) {

        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");

        if (!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student not found.");

        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be blank.");

        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be blank.");

        if (email == null || email.isBlank() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email address.");

        if (phoneNumber == null || phoneNumber.isBlank() || phoneNumber.length() > 16)
            throw new IllegalArgumentException("Invalid phone number.");

        if (identificationNumber == null || identificationNumber.isBlank() || identificationNumber.length() > 10)
            throw new IllegalArgumentException("Invalid identification number.");

        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");

        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme does not exist.");

        // Allow same email only if it belongs to THIS student
        Student existingByEmail = studentDao.findStudentByEmail(email);
        if (existingByEmail != null && existingByEmail.getStudentId() != studentId)
            throw new IllegalArgumentException("Email is already taken by another student.");

        Student student = studentDao.findStudentById(studentId);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhoneNumber(phoneNumber);
        student.setIdentificationNumber(identificationNumber);
        student.setProgrammeId(programmeId);

        boolean updated = studentDao.updateStudent(student);
        if (!updated)
            throw new RuntimeException("Failed to update student. Please try again.");
    }

 
    //  DELETE STUDENT
    
    public void deleteStudent(int studentId) {
        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");

        if (!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student not found.");

        boolean deleted = studentDao.deleteStudent(studentId);
        if (!deleted)
            throw new RuntimeException("Failed to delete student. Please try again.");
    }
}
