package com.sys.model;

import java.time.LocalDate;

/**
 *
 * Grading scale:
 *   70+ = A (4.0)
 *   60-69 = B (3.0)
 *   50-59 = C (2.0)
 *   40-49 = D (1.0)
 *   <40   = F (0.0)
 *
 */
public class Score {
    private int scoreId;
    private int enrollmentId;
    private float catScore;
    private float examScore;
    private float totalScore;
    private char letterGrade;
    private float gradePoint;
    private int academicYear;
    private int submittedBy;
    private LocalDate submittedDate;

    // populated by JOIN
    private String studentName;
    private String admissionNumber;
    private String courseTitle;

   
    public Score(int enrollmentId, float catScore, float examScore,
                 int academicYear, int submittedBy) {
        this.scoreId = 0;
        this.enrollmentId = enrollmentId;
        this.academicYear = academicYear;
        this.submittedBy = submittedBy;
        this.submittedDate = LocalDate.now();
        setScores(catScore, examScore);
    }

   
    public Score(int scoreId, int enrollmentId, float catScore, float examScore,
                 int academicYear, int submittedBy) {
        this.scoreId = scoreId;
        this.enrollmentId = enrollmentId;
        this.academicYear = academicYear;
        this.submittedBy = submittedBy;
        this.submittedDate = LocalDate.now();
        setScores(catScore, examScore);
    }

    public void setScores(float catScore, float examScore) {
        if (catScore < 0 || catScore > 30)
            throw new IllegalArgumentException("CAT score must be between 0 and 30");
        if (examScore < 0 || examScore > 70)
            throw new IllegalArgumentException("Exam score must be between 0 and 70");

        this.catScore = catScore;
        this.examScore = examScore;
        this.totalScore = catScore + examScore;
        calculateGrade();
    }

    private void calculateGrade() {
        if (totalScore >= 70){
            letterGrade = 'A'; gradePoint = 4.0f; 
        }
        else if (totalScore >= 60){ 
            letterGrade = 'B'; gradePoint = 3.0f; 
        }
        else if (totalScore >= 50){ 
            letterGrade = 'C'; gradePoint = 2.0f; 
        }
        else if (totalScore >= 40){ 
            letterGrade = 'D'; gradePoint = 1.0f; 
        }
        else{ 
            letterGrade = 'F'; gradePoint = 0.0f; 
        }
    }

    public boolean isPassing(){ 
        return totalScore >= 40; 
    }

    public int getScoreId() { 
        return scoreId; 
    }

    public int getEnrollmentId() { 
        return enrollmentId; 
    }

    public float getCatScore()  { 
        return catScore; 
    }

    public float getExamScore() { 
        return examScore; 
    }

    public float getTotalScore()   { 
        return totalScore; 
    }

    public char getLetterGrade()  { 
        return letterGrade; 
    }

    public float getGradePoint() { 
        return gradePoint; 
    }

    public int getAcademicYear() { 
        return academicYear; 
    }

    public int getSubmittedBy() { 
        return submittedBy; 
    }

    public LocalDate getSubmittedDate(){ 
        return submittedDate; 
    }

    public void setScoreId(int scoreId) { 
        this.scoreId = scoreId; 
    }

    public void setSubmittedDate(LocalDate date)    { 
        this.submittedDate = date; 
    }

    public String getStudentName(){ 
        return studentName; 
    }
    public String getAdmissionNumber(){ 
        return admissionNumber; 
    }
    public String getCourseTitle(){ 
        return courseTitle; 
    }

    public void setStudentName(String name){ 
        this.studentName = name; 
    }

    public void setAdmissionNumber(String number){ 
        this.admissionNumber = number; 
    }

    public void setCourseTitle(String title) { 
        this.courseTitle = title; 
    }
    
}
