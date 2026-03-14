package com.sys.service;

import java.time.LocalDate;
import java.util.List;
import com.sys.dao.EnrollmentDao;
import com.sys.dao.LecturerDao;
import com.sys.dao.ScoreDao;
import com.sys.model.Score;

public class ScoreService {

    private final ScoreDao scoreDao = new ScoreDao();
    private final EnrollmentDao enrollmentDao = new EnrollmentDao();
    private final LecturerDao lecturerDao = new LecturerDao();
    

    private static final float MAX_CAT = 30.0f;
    private static final float MAX_EXAM = 70.0f;


    //  SUBMIT SCORE
  
    public void submitScore(int enrollmentId, float catScore, float examScore,
                            int academicYear, int submittedBy) {

        if (enrollmentId < 1)
            throw new IllegalArgumentException("Enrollment ID must be a positive number.");
        if (!enrollmentDao.idExistsEnrollment(enrollmentId))
            throw new IllegalArgumentException("Enrollment not found.");
        if (catScore < 0 || catScore > MAX_CAT)
            throw new IllegalArgumentException("CAT score must be between 0 and " + MAX_CAT + ".");
        if (examScore < 0 || examScore > MAX_EXAM)
            throw new IllegalArgumentException("Exam score must be between 0 and " + MAX_EXAM + ".");
        if (academicYear < 2000 || academicYear > LocalDate.now().getYear())
            throw new IllegalArgumentException("Invalid academic year.");
        if (submittedBy < 1)
            throw new IllegalArgumentException("Submitted by (Lecturer ID) must be a positive number.");
        if (!lecturerDao.idExistsLecturer(submittedBy))
            throw new IllegalArgumentException("Lecturer not found.");
        if (scoreDao.scoreExistsForEnrollment(enrollmentId))
            throw new IllegalStateException("A score has already been submitted for this enrollment. Use update instead.");

        // float totalScore = catScore + examScore;
       

        Score score = new Score(enrollmentId, catScore, examScore, academicYear, submittedBy);
        
        // score.setGradePoint(gradePoint);
        score.setSubmittedDate(LocalDate.now());

        boolean saved = scoreDao.saveScore(score);
        if (!saved)
            throw new RuntimeException("Failed to submit score. Please try again.");
    }


    //  UPDATE SCORE
   
    public void updateScore(int scoreId, float catScore, float examScore, int submittedBy) {

        if (scoreId < 1)
            throw new IllegalArgumentException("Score ID must be a positive number.");
        Score existing = scoreDao.findScoreById(scoreId);
        if (existing == null)
            throw new IllegalArgumentException("Score not found.");
        if (catScore < 0 || catScore > MAX_CAT)
            throw new IllegalArgumentException("CAT score must be between 0 and " + MAX_CAT + ".");
        if (examScore < 0 || examScore > MAX_EXAM)
            throw new IllegalArgumentException("Exam score must be between 0 and " + MAX_EXAM + ".");
        if (submittedBy < 1)
            throw new IllegalArgumentException("Submitted by (Lecturer ID) must be a positive number.");
        if (!lecturerDao.idExistsLecturer(submittedBy))
            throw new IllegalArgumentException("Lecturer not found.");

        float totalScore = catScore + examScore;
        char letterGrade = computeLetterGrade(totalScore);
        float gradePoint = computeGradePoint(totalScore);
        LocalDate submittedDate = LocalDate.now();

      
        

       

        boolean updated = scoreDao.updateScore(scoreId, catScore, totalScore, examScore, letterGrade, gradePoint, submittedDate, submittedBy);
        if (!updated)
            throw new RuntimeException("Failed to update score. Please try again.");
    }

    
    //  GET ALL SCORES
  
    public List<Score> getAllScores() {
        List<Score> scores = scoreDao.findAllScores();
        if (scores.isEmpty())
            throw new IllegalStateException("No scores found.");
        return scores;
    }

    //  GET SCORE BY ENROLLMENT
  
    public Score getScoreByEnrollment(int enrollmentId) {
        if (enrollmentId < 1)
            throw new IllegalArgumentException("Enrollment ID must be a positive number.");
        if (!enrollmentDao.idExistsEnrollment(enrollmentId))
            throw new IllegalArgumentException("Enrollment not found.");
        Score score = scoreDao.findScoreByEnrollment(enrollmentId);
        if (score == null)
            throw new IllegalStateException("No score found for enrollment ID: " + enrollmentId);
        return score;
    }

    
    //  GET SCORES BY STUDENT (for result slip / GPA)
   
    public List<Score> getScoresByStudent(int studentId) {
        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");
        List<Score> scores = scoreDao.findScoresByStudent(studentId);
        if (scores.isEmpty())
            throw new IllegalStateException("No scores found for this student.");
        return scores;
    }

    
    //  GET SCORES BY OFFERING (lecturer sees class results)

    public List<Score> getScoresByOffering(int offeringId) {
        if (offeringId < 1)
            throw new IllegalArgumentException("Offering ID must be a positive number.");
        List<Score> scores = scoreDao.findScoresByOffering(offeringId);
        if (scores.isEmpty())
            throw new IllegalStateException("No scores found for this course offering.");
        return scores;
    }

   
    //  CALCULATE GPA FOR STUDENT

    public double calculateGpa(int studentId) {
        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");
        List<Score> scores = scoreDao.findScoresByStudent(studentId);
        if (scores.isEmpty())
            throw new IllegalStateException("No scores found to calculate GPA.");

        double totalPoints = 0;
        for (Score s : scores) {
            totalPoints += s.getGradePoint();
        }
        return Math.round((totalPoints / scores.size()) * 100.0) / 100.0;
    }

    
    //  DELETE SCORE
  
    public void deleteScore(int scoreId) {
        if (scoreId < 1)
            throw new IllegalArgumentException("Score ID must be a positive number.");
        Score score = scoreDao.findScoreById(scoreId);
        if (score == null)
            throw new IllegalArgumentException("Score not found.");
        boolean deleted = scoreDao.deleteScore(scoreId);
        if (!deleted)
            throw new RuntimeException("Failed to delete score. Please try again.");
    }

    
    //  HELPERS 
    
  
    private char computeLetterGrade(float total) {
        if (total >= 70) return 'A';
        if (total >= 60) return 'B';
        if (total >= 50) return 'C';
        if (total >= 40) return 'D';
        return 'E'; 
    }

    private float computeGradePoint(float total) {
        if (total >= 70) return 4.0f;
        if (total >= 60) return 3.0f;
        if (total >= 50) return 2.0f;
        if (total >= 40) return 1.0f;
        return 0.0f;
    }
}
