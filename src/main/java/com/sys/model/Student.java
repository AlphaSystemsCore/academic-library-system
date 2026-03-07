package com.sys.model;

import java.time.LocalDate;


public class Student extends Person {
    private int studentId;
    private int programmeId;
    private String admissionNumber;       //  ACSC-2026-0001 — auto
    private LocalDate registeredDate;

    public Student(
        int studentId,
        String firstName,
        String lastName,
        String email,
        String identificationNumber,
        String phoneNumber,
        int roleId,
        String password,
        int programmeId,
        String admissionNumber,
        LocalDate registeredDate
    ) {
        super(firstName, lastName, email, identificationNumber, phoneNumber, roleId, password);
        this.studentId = studentId;
        this.programmeId = programmeId;
        this.admissionNumber = admissionNumber;
        this.registeredDate = registeredDate;
    }


    public Student(
        String firstName,
        String lastName,
        String email,
        String identificationNumber,
        String phoneNumber,
        int roleId,
        String password,
        int programmeId
    ) {
        this(0, firstName, lastName, email, identificationNumber,
             phoneNumber, roleId, password, programmeId, null,  LocalDate.now());
    }

    public int getStudentId(){ 
        return studentId;
     }
    public int getProgrammeId() { 
        return programmeId;
     }
    public String getAdmissionNumber()  {
         return admissionNumber; 
        }
    public LocalDate getRegisteredDate(){
         return registeredDate; 
        }

    public void setStudentId(int studentId){ 
        this.studentId = studentId;
     }
    public void setProgrammeId(int programmeId) { 
        this.programmeId = programmeId; 
    }
    public void setAdmissionNumber(String admissionNumber){ 
        this.admissionNumber = admissionNumber;
     }
    public void setRegisteredDate(LocalDate registeredDate){
         this.registeredDate = registeredDate;
         }

    
}
