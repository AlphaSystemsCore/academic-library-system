package com.sys.model;

import java.time.LocalDate;

// academic calendar.
// isCurrent = true
// Admin controls which semester is active.

public class Semester {
    private int semesterId;
    private int academicYear;
    private int semesterNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrent;      // only one semester is active at a time

  
    public Semester(int academicYear, int semesterNumber,
                    LocalDate startDate, LocalDate endDate, boolean isCurrent) {
        this.semesterId = 0;
        this.academicYear = academicYear;
        this.semesterNumber = semesterNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrent = isCurrent;
    }

    public Semester(int semesterId, int academicYear, int semesterNumber,
                    LocalDate startDate, LocalDate endDate, boolean isCurrent) {
        this.semesterId = semesterId;
        this.academicYear = academicYear;
        this.semesterNumber = semesterNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrent = isCurrent;
    }

    public int getSemesterId(){
         return semesterId; 
    }
    public int getAcademicYear(){ 
        return academicYear; 
    }
    public int getSemesterNumber(){ 
        return semesterNumber; 
    }
    public LocalDate getStartDate(){ 
        return startDate; 
    }
    public LocalDate getEndDate(){ 
        return endDate; 
    }
    public boolean isCurrent(){ 
        return isCurrent; 
    }

    public void setSemesterId(int semesterId){
         this.semesterId = semesterId; 
    }
    public void setAcademicYear(int academicYear){ 
        this.academicYear = academicYear; 
    }
    public void setSemesterNumber(int number){ 
        this.semesterNumber = number; 
    }
    public void setStartDate(LocalDate startDate){ 
        this.startDate = startDate; 
    }
    public void setEndDate(LocalDate endDate){ 
        this.endDate = endDate; 
    }
    public void setCurrent(boolean isCurrent){ 
        this.isCurrent = isCurrent; 
    }

    public String getLabel() {
        return "Semester " + semesterNumber + " - " + academicYear;
    }
}
