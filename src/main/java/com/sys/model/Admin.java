package com.sys.model;



//  Admin manages programmes, courses, semesters, lecturers, and reports.
 
public class Admin extends Person {
    private String adminNumber;

    public Admin(String firstName, String lastName,
                 String email, String identificationNumber,
                 String phoneNumber, int roleId, String password, String adminNumber) {
        super(firstName, lastName, email, identificationNumber, phoneNumber, roleId, password);

        this.adminNumber= adminNumber;
    }

   

    public String getAdminNumber() {
        return adminNumber; 
    }
    public void setAdminNumber(String adminNumber) { 
        this.adminNumber = adminNumber;
    }

}