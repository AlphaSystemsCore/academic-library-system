package com.sys.model;

import java.time.LocalDate;

/**
 * One student can have multiple enrollments one per course per semester.
 * Max 5 enrollments per student per semester — enforced in EnrollmentService.
 *
 * 
 * 
 * attemptNumber tracks retakes:
 *   1 = Normal (first attempt)
 *   2 = Retake
 *   3+ = Exam Special
 */
public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int offeringId;
    private int attemptNumber;
    private StatusEnum status;
    private boolean isCountedInGpa;
    private boolean isActive;
    private LocalDate enrolledDate;

    //populated by JOIN queries for display
    private String studentName;
    private String admissionNumber;
    private String courseTitle;
    private String courseCode;

    public enum StatusEnum {
        NORMAL, RETAKE, EXAM_SPECIAL, DROPPED, COMPLETED
    }

  
    public Enrollment(int studentId, int offeringId,
                      int attemptNumber, LocalDate enrolledDate) {
        this.enrollmentId = 0;
        this.studentId = studentId;
        this.offeringId = offeringId;
        this.attemptNumber = attemptNumber;
        this.enrolledDate = enrolledDate;
        this.isCountedInGpa = true;
        this.isActive = true;
        this.status = resolveStatus(attemptNumber);
    }


    public Enrollment(int enrollmentId, int studentId, int offeringId,
                      int attemptNumber, LocalDate enrolledDate) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.offeringId = offeringId;
        this.attemptNumber = attemptNumber;
        this.enrolledDate = enrolledDate;
        this.isCountedInGpa = true;
        this.isActive = true;
        this.status = resolveStatus(attemptNumber);
    }

    private StatusEnum resolveStatus(int attempt) {
        if (attempt >= 3) return StatusEnum.EXAM_SPECIAL;
        if (attempt == 2) return StatusEnum.RETAKE;
        return StatusEnum.NORMAL;
    }

    public int getEnrollmentId(){ 
        return enrollmentId; 
    }

    public int getStudentId() { 
        return studentId; 
    }

    public int getOfferingId() { 
        return offeringId; 
    }

    public int getAttemptNumber(){ 
        return attemptNumber; 
    }

    public StatusEnum getStatus() { 
        return status; 
    }

    public boolean isCountedInGpa(){ 
        return isCountedInGpa; 
    }

    public boolean isActive(){
         return isActive; 
    }

    public LocalDate getEnrolledDate(){
         return enrolledDate; 
    }

    public void setEnrollmentId(int enrollmentId){ 
        this.enrollmentId = enrollmentId;
    }

    public void setStatus(StatusEnum status) { 
        this.status = status; 
    }

    public void setCountedInGpa(boolean counted){ 
        this.isCountedInGpa = counted; 
    }

    public void setActive(boolean active){ 
        this.isActive = active; 
    }

    public void drop() {
        this.isActive = false;
        this.status = StatusEnum.DROPPED;
        this.isCountedInGpa = false;
    }

    // Display fields — set by service layer when doing JOIN queries
     public String getStudentName(){ 
        return studentName; 
    }

     public String getAdmissionNumber(){ 
        return admissionNumber; 
    }

     public String getCourseTitle() {
         return courseTitle; 
    }

     public String getCourseCode(){ 
        return courseCode;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName; 
    }

     public void setAdmissionNumber(String admissionNumber){ 
        this.admissionNumber = admissionNumber;
    }

    public void setCourseTitle(String courseTitle){ 
        this.courseTitle = courseTitle; 
    }

     public void setCourseCode(String courseCode) { 
        this.courseCode = courseCode; 
     }
}
