package com.sys.model;

public class Department {
    private int departmentId;
    private String name;
    private String description;

    
    public Department(String name, String description) {
        this.departmentId = 0;
        this.name = name;
        this.description = description;
    }

    
    public Department(int departmentId, String name, String description) {
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
    }

    public int getDepartmentId(){ 
        return departmentId; 
    }
    
    public String getName(){ 
        return name; 
    }

    public String getDescription(){ 
        return description; 
    }

    public void setDepartmentId(int departmentId){ 
        this.departmentId = departmentId; 
    }

    public 
    void setName(String name){ 
        this.name = name; 
    }

    public void setDescription(String description){ 
        this.description = description; 
    }
}
