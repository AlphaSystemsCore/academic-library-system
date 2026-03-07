package com.sys.model;

public class Programme {
    private int programmeId;
    private String name;
    private String programmeCode;   
    private LevelEnum level;
    private int durationYears;
    private int departmentId;

    public enum LevelEnum {
        CERTIFICATE, DIPLOMA, DEGREE, MASTERS, PHD
    }

    public Programme(
        int programmeId,
        String name,
        String programmeCode,
        LevelEnum level,
        int durationYears,
        int departmentId
    ) {
        this.programmeId = programmeId;
        this.name = name;
        this.programmeCode = programmeCode;
        this.level = level;
        this.durationYears = durationYears;
        this.departmentId = departmentId;
    }

    
    public Programme(String name, String programmeCode, LevelEnum level, int durationYears, int departmentId) {
        this(0, name, programmeCode, level, durationYears, departmentId);
    }

    public int getProgrammeId(){ 
        return programmeId; 
    }
    public String getName(){ 
        return name; 
    }
    public String getProgrammeCode(){ 
        return programmeCode; 
    }
    public LevelEnum getLevel() {
         return level; 
        }
    public int getDurationYears() { 
        return durationYears; 
    }
    public int getDepartmentId() { 
        return departmentId; 
    }

    public void setProgrammeId(int programmeId){ 
        this.programmeId = programmeId; 
    }
    public void setName(String name){ 
        this.name = name; 
    }
    public void setProgrammeCode(String code) { 
        this.programmeCode = code; 
    }
    public void setLevel(LevelEnum level){ 
        this.level = level; 
    }
    public void setDurationYears(int years){
         this.durationYears = years; 
        }
    public void setDepartmentId(int departmentId){ 
        this.departmentId = departmentId; 
    }

  
}
