package com.sys.model;


//  * A course belongs to a programme. 
// * It is offered each semester via CourseOffering.

public class Course {
    private String courseCode;  
    private String title;       
    private int creditHours;
    private int programmeId;

    public Course(String courseCode, String title, int creditHours, int programmeId) {
        this.courseCode = courseCode;
        this.title = title;
        this.creditHours = creditHours;
        this.programmeId = programmeId;
    }

    public String getCourseCode(){ 
        return courseCode; 
    }

    public String getTitle(){ 
        return title; 
    }

    public int getCreditHours(){ 
        return creditHours; 
    }

    public int getProgrammeId(){ 
        return programmeId; 
    }

    public void setCourseCode(String code){ 
        this.courseCode = code; 
    }

    public void setTitle(String title){
        this.title = title; 
    }

    public void setCreditHours(int hours){ 
        this.creditHours = hours; 
    }

    public void setProgrammeId(int programmeId){ 
        this.programmeId = programmeId; 
    }

}
