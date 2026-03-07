package com.sys.model;


//  * Librarian manages books, borrowing, returns, and fines.
 

public class Librarian extends Person {
    private int librarianId;
    private String staffNumber;

    public Librarian(
        int librarianId, 
        String firstName, 
        String lastName,
        String email, 
        String identificationNumber,
        String phoneNumber, int roleId, String password,
        String staffNumber) 
        {
        super(firstName, lastName, email, identificationNumber, phoneNumber, roleId, password);
        this.librarianId = librarianId;
        this.staffNumber = staffNumber;
    }

    public Librarian(
        String firstName, 
        String lastName, 
        String email,
        String identificationNumber, 
        String phoneNumber,
        int roleId, 
        String password, 
        String staffNumber) {
        this(0, firstName, lastName, email, identificationNumber,
             phoneNumber, roleId, password, staffNumber);
    }

    public int getLibrarianId() 
    { return librarianId; 

    }
    public String getStaffNumber()
    { return staffNumber;

     }

    public void setLibrarianId(int id){ 
        this.librarianId = id; 
    }


}
