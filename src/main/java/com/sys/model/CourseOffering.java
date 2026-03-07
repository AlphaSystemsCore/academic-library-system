package com.sys.model;

/** * specific semester by a specific lecturer.
 * GLUE
 * One offering = one course + one lecturer + one semester + one programme
 * Students enroll into offerings.
 * Lecturers see their students through their offerings.
 *  */
public class CourseOffering {
    private int courseId;
    private int lecturerId;
    private int semesterId;
    private int programmeId;

    //  populated by JOIN queries for display purposes
    private String courseTitle;
    private String courseCode;
    private String lecturerName;
    private int creditHours;
    private int enrolledStudentsCount;

    public CourseOffering(
        int courseId,
        int lecturerId,
        int semesterId,
        int programmeId
    ) {
        
        this.courseId = courseId;
        this.lecturerId = lecturerId;
        this.semesterId = semesterId;
        this.programmeId = programmeId;
    }

    

    
    public int getCourseId() {
         return courseId;
    }
    public int getLecturerId(){ 
        return lecturerId;
    
    }
    public int getSemesterId(){ 
        return semesterId; 
    }
    public int getProgrammeId(){ 
        return programmeId; 
    }

    public void setCourseId(int courseId){ 
        this.courseId = courseId; 
    }
    public void setLecturerId(int lecturerId){ 
        this.lecturerId = lecturerId; 
    }
    public void setSemesterId(int semesterId){ 
        this.semesterId = semesterId; 
    }
    public void setProgrammeId(int programmeId) { 
        this.programmeId = programmeId; 
    }

    // Display fields OBTAINED FROM SERVICE LAYER
    public String getCourseTitle(){ 
        return courseTitle; 
    }
    public String getCourseCode(){ 
        return courseCode; 
    }
    public String getLecturerName(){ 
        return lecturerName; 
    }
    public int getCreditHours(){ 
        return creditHours; 
    }
    public int getEnrolledStudentsCount(){ 
        return enrolledStudentsCount; 
    }

    public void setCourseTitle(String courseTitle){ 
        this.courseTitle = courseTitle; 
    }
    public void setCourseCode(String courseCode){ 
        this.courseCode = courseCode; 
    }
    public void setLecturerName(String lecturerName){ 
        this.lecturerName = lecturerName; 
    }
    public void setCreditHours(int creditHours){ 
        this.creditHours = creditHours; 
    }
    public void setEnrolledStudentsCount(int count){ 
        this.enrolledStudentsCount = count; 
    }

    
}
