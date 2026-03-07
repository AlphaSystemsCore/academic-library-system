package com.sys.model;

import java.time.LocalDate;


public class Student extends Person {

    private int programmeId;
    private String admissionNumber;       //  ACSC-2026-0001 — auto
    private LocalDate registeredDate;

    public Student(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String identificationNumber,
        String password,
        int programmeId,
        String admissionNumber,
        int roleId,
        LocalDate registeredDate
        
    ) {
        super(firstName, lastName, email, identificationNumber, phoneNumber, roleId, password);
        this.programmeId = programmeId;
        this.admissionNumber = admissionNumber;
        this.registeredDate = registeredDate;
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
