package com.sys.model;

//  Admin manages programmes, courses, semesters, lecturers, and reports.

public class Admin extends Person {
    private int adminId;
    private String adminNumber;

    // Constructor without ID 
    public Admin(String firstName, String lastName, String email,
                 String identificationNumber, String phoneNumber,
                 int roleId, String password, String adminNumber) {
        super(firstName, lastName, email, identificationNumber, phoneNumber, roleId, password);
        this.adminId = 0;
        this.adminNumber = adminNumber;
    }

    
    public Admin(int adminId, String firstName, String lastName, String email,
                 String identificationNumber, String phoneNumber,
                 int roleId, String password, String adminNumber) {
        super(firstName, lastName, email, identificationNumber, phoneNumber, roleId, password);
        this.adminId = adminId;
        this.adminNumber = adminNumber;
    }

    public int getAdminId(){ 
        return adminId; 
    }

    public String getAdminNumber(){ 
        return adminNumber; 
    }

    public void setAdminId(int adminId){ 
        this.adminId = adminId; 
    }

    public void setAdminNumber(String adminNumber){ 
        this.adminNumber = adminNumber;
     }
}
