package com.sys.model;

//  * Librarian manages books, borrowing, returns, and fines.

public class Librarian extends Person {
    private int librarianId;
    private String staffNumber;

    public Librarian(String firstName, String lastName, String email,
                     String password, String staffNumber, int roleId,
                     String identificationNumber, String phoneNumber) {
        super(firstName, lastName, email, identificationNumber, phoneNumber, roleId, password);
        this.librarianId = 0;
        this.staffNumber = staffNumber;
    }

    public Librarian(int librarianId, String firstName, String lastName, String email,
                     String password, String staffNumber, int roleId,
                     String identificationNumber, String phoneNumber) {
        super(firstName, lastName, email, identificationNumber, phoneNumber, roleId, password);
        this.librarianId = librarianId;
        this.staffNumber = staffNumber;
    }

    public int getLibrarianId() { 
        return librarianId; 
    }
    public String getStaffNumber() { 
        return staffNumber; 
    }

    public void setLibrarianId(int librarianId) 
    { this.librarianId = librarianId; 

    }
}
