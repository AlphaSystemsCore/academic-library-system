package com.sys.service;

import java.util.List;
import java.time.LocalDate;
import com.sys.dao.ProgrammeDao;
import com.sys.dao.RolesDao;
import com.sys.dao.StudentDao;
import com.sys.model.Programme;
import com.sys.model.Student;


public class StudentService {
    ProgrammeDao programmeDao = new ProgrammeDao();
    RolesDao rolesDao = new RolesDao();
    StudentDao studentDao = new StudentDao();
    public void  addStudent(String firstName, String lastName, String email, String phoneNumber, String identificationNumber, String password, int programmeId,  int roleId ){ // i'll generate in the code LocalDate registeredDate String admissionNumber,
        if(firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First Name can not  be blank");

        if(lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last Name can not be blank");

        if(email == null || email.isBlank() ||!email.matches("^[A-Za-z0-9+_.-]+@(.+)$") )
            throw new IllegalArgumentException("Invalid email");

        if(phoneNumber == null || phoneNumber.isBlank() || phoneNumber.length()>16 )
            throw new IllegalArgumentException("Invalid phone number");

        if( identificationNumber == null || identificationNumber.isBlank() || identificationNumber.length() > 10 )
            throw new IllegalArgumentException("Invalid identification number");

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{16,}$";
        if(password == null || !password.matches(passwordPattern))
            throw new IllegalArgumentException("Password must be at least 16 characters with uppercase, lowercase, number and special character.");
        
        if(programmeId <1)
            throw new IllegalArgumentException("Programme Id must be a positive number");

        if(roleId < 1)
            throw new IllegalArgumentException("Role Id must be a positive number");

        if(!rolesDao.idExistsRole(roleId))
            throw new IllegalArgumentException("Role id don't exists");

        if(!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programm Id dont exists");

        if(studentDao.emailExistsStudent(email))
            throw new IllegalArgumentException("Email already taken");

        if(studentDao.idNoExistsStudent(identificationNumber))
            throw new IllegalArgumentException("Identification number exists");

        if(studentDao.existsPhoneNumber(phoneNumber))
            throw new IllegalArgumentException("Phone number already exists.");

        


        
        //?? generate date automatically
        LocalDate registerDate = LocalDate.now();
         // generate admission number
        Programme programme = programmeDao.findProgrammeById(programmeId);
        String code = programme.getProgrammeCode();
        int count = studentDao.countStudents();
        String admissionNumber = code+
                                    "/"+String.valueOf(LocalDate.now().getYear())+
                                    "/"+String.format("%04d", count +1);
        Student student = new Student(firstName, lastName, email, phoneNumber, identificationNumber, password, programmeId, admissionNumber, roleId, registerDate);
        boolean saved = studentDao.saveStudent(student);
        if(!saved)
            throw new RuntimeException("Failed to save, please try again");
    }

    public List<Student> getAllSudents(){
        List<Student> students = studentDao.findAllStudents();
        if(students.isEmpty())
            throw new IllegalStateException("Not found");
        return students;

    }

    public List<Student> getByProgrammeStudents(int programmeId){
        if(programmeId < 1)
            throw new IllegalArgumentException("Programm id must be a positive number.");

        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme does not exist.");

        List<Student> students = studentDao.findStudentsByProgramme(programmeId);

        if(students.isEmpty())
            throw new IllegalStateException("No student found for this programme");
        return students;
    }

    public Student getByIdStudent(int studentId){
        if(studentId < 1)
            throw new IllegalArgumentException("Student id must be a positive integer");
        Student student = studentDao.findStudentById(studentId);
        if (student == null)
            throw new IllegalStateException("Student not found for the Student id" + studentId);
        return student;

    } 
    public Student getByAdmissionNumber(String admissionNumber){
        if(admissionNumber == null || admissionNumber.isBlank())
            throw new IllegalArgumentException("Admission number can't be null");
       Student student =  studentDao.findStudentByAdmissionNumber(admissionNumber);
       if(student == null)
        throw new IllegalStateException("No student found for the admission" + admissionNumber);
             return student;
    }

    // public void updateStudent(String firstName, String lastName, String email, String phoneNumber, String identificationNumber, int programme_id){
    //     if(firstName == null || firstName.isBlank())
    //         throw new IllegalArgumentException("Firstname can't be null");

    //     if (lastName == null || lastName.isBlank())
    //         throw new IllegalArgumentException("Lastname can't be null");

    //     if(email == null || email.isBlank())
    //         throw new IllegalArgumentException("Email can be null");

    //     if(phoneNumber == null || phoneNumber.isBlank() )
    //         throw new IllegalArgumentException("Phone number can't be empty");

    //     if(phoneNumber.length() > 14)
    //         throw new IllegalArgumentException("Maximum length is 14 charaters");

    //     if(identificationNumber == null || identificationNumber.isBlank())
    //         throw new IllegalArgumentException("Identification number can be null");


    //     if(studentDao.emailExistsStudent(email))
    //         throw new IllegalArgumentException("Email already taken");
    //     if(studentDao.idNoExistsStudent(identificationNumber))
    //         throw new IllegalArgumentException("Identification number already exists.");
    //     if(studentDao.existsPhoneNumber(phoneNumber))
    //         throw new IllegalArgumentException("Phone number already registered");
    //     Student student = new Student(studentId, firstName, lastName, email, phoneNumber, identificationNumber, p);
    //     studentDao.updateStudent(null);


                    
    // }
    public void deleteStudent(int studentId){
        // check for any relatioship 
        if(studentId <0)
            throw new IllegalArgumentException("Student id must be a positive number.");
        if(!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student id not found.");
        // continue from here
        boolean delete = studentDao.deleteStudent(studentId);
        // if(!delete)
        //     throw new IllegalArgumentException()

    }

    

}
