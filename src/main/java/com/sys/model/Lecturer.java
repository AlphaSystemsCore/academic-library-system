package com.sys.model;


public class Lecturer extends Person {
    private int lecturerId;
    private String staffNumber;     
    private String title;           
    private int departmentId;

    public Lecturer(
        String firstName,
        String lastName,
        String email,
        String title,
        String identificationNumber,
        String phoneNumber,
        String staff_number,
        int departmentId,
        int roleId,
        String password
        
    ) {
        super(firstName, lastName, email, identificationNumber, phoneNumber, roleId, password);
        this.title = title;
        this.staffNumber = staffNumber;
        this.departmentId = departmentId;
    }


    public int getLecturerId(){
        return lecturerId; 
    }
    public String getStaffNumber(){
        return staffNumber; 
    }
    public String getTitle(){ 
        return title; 
    }
    public int getDepartmentId(){ 
        return departmentId; 
    }

    public void setLecturerId(int lecturerId){ 
        this.lecturerId = lecturerId; 
    }
    public void setTitle(String title){ 
        this.title = title; 
    }
    public void setDepartmentId(int deptId){ 
        this.departmentId = deptId; 
    }

    public String getDisplayName() {
        return title + " " + getFullName();
    }

}
