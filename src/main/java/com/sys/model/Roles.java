package com.sys.model;

public class Roles {
    private String name;

    public enum RoleName {
        ADMIN, STUDENT, LECTURER, LIBRARIAN
    }

    public Roles(String name) {
        this.name = name;
    }

    public String getName() {
    return name; 
    }

    public void setName(String name){ 
        this.name = name; 
    }

}
