package com.sys.model;

public class Roles {
    private int roleId;
    private String name;

    public enum RoleName {
        ADMIN, STUDENT, LECTURER, LIBRARIAN, ADMIN2
    }

  
    public Roles(String name) {
        this.roleId = 0;
        this.name = name;
    }

   
    public Roles(int roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    public int getRoleId(){ 
        return roleId; 
    }

    public String getName(){
         return name; 
    }

    public void setRoleId(int roleId) { 
        this.roleId = roleId; 
    }

    public void setName(String name) { 
        this.name = name; 
    }
}
