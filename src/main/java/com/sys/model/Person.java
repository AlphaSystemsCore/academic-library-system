package com.sys.model;


public abstract class Person {
    private String firstName;
    private String lastName;
    private String email;
    private String identificationNumber;
    private String phoneNumber;
    private int roleId;
    private String password;

    public Person(
        String firstName,
        String lastName,
        String email,
        String identificationNumber,
        String phoneNumber,
        int roleId,
        String password
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.identificationNumber = identificationNumber;
        this.phoneNumber = phoneNumber;
        this.roleId = roleId;
        this.password = password;
    }

    
    public String getFirstName(){ return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName(){ return firstName + " " + lastName; }
    public String getEmail(){ return email; }
    public String getIdentificationNumber(){ return identificationNumber; }
    public String getPhoneNumber(){ return phoneNumber; }
    public int getRoleId(){ return roleId; }
    public String getPassword(){ return password; }

    
    public void setFirstName(String firstName){ this.firstName = firstName; }
    public void setLastName(String lastName){ this.lastName = lastName; }
    public void setPhoneNumber(String phoneNumber){ this.phoneNumber = phoneNumber; }
    public void setRoleId(int roleId){ this.roleId = roleId; }
    public void setPassword(String password){ this.password = password; }

}
