package com.sys.model;


//  * Librarian manages books, borrowing, returns, and fines.
 

public class Librarian extends Person {
    private String staffNumber;

    public Librarian(
        String firstName, 
        String lastName,
        String email, 
        String password,
        String staffNumber,
        int roleId,
        String identificationNumber,
        String phoneNumber
        
        ) 
        {
        super(firstName, lastName, email, identificationNumber, phoneNumber, roleId, password);
        this.staffNumber = staffNumber;
    }

    public String getStaffNumber() {
    return staffNumber;

    }

}
