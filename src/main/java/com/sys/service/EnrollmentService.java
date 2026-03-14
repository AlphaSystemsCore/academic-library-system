package com.sys.service;

import java.time.LocalDate;
import java.util.List;
import com.sys.dao.CourseOfferingDao;
import com.sys.dao.EnrollmentDao;
import com.sys.dao.StudentDao;
import com.sys.model.Enrollment;
import com.sys.model.Enrollment.StatusEnum;

public class EnrollmentService {

    private final EnrollmentDao enrollmentDao = new EnrollmentDao();
    private final StudentDao studentDao = new StudentDao();
    private final CourseOfferingDao offeringDao = new CourseOfferingDao();

    
    private static final int MAX_COURSES_PER_SEMESTER = 5;

    
    //  ENROLL STUDENT
    
    public void enrollStudent(int studentId, int offeringId) { //frontend parameters--student dashboard

        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");
        if (offeringId < 1)
            throw new IllegalArgumentException("Offering ID must be a positive number.");

        if (!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student not found.");
        if (!offeringDao.idExistsOffering(offeringId))
            throw new IllegalArgumentException("Course offering not found.");

        // Prevent duplicate active enrollment
        if (enrollmentDao.enrollmentExists(studentId, offeringId))
            throw new IllegalStateException("Student is already enrolled in this course.");

        // Enforce max 5 courses per semester
        int activeCount = enrollmentDao.countActiveEnrollmentsInCurrentSemester(studentId);
        if (activeCount >= MAX_COURSES_PER_SEMESTER)
            throw new IllegalStateException("Student cannot enroll in more than " + MAX_COURSES_PER_SEMESTER + " courses per semester.");

        int attemptNumber = enrollmentDao.getAttemptNumber(studentId, offeringId);

        Enrollment enrollment = new Enrollment(studentId, offeringId, attemptNumber, LocalDate.now());
        enrollment.setStatus(StatusEnum.NORMAL);
        enrollment.setActive(true);
        enrollment.setCountedInGpa(true);

        boolean saved = enrollmentDao.saveEnrollment(enrollment);
        if (!saved)
            throw new RuntimeException("Failed to enroll student. Please try again.");
    }

    
    //  DROP COURSE

    public void dropCourse(int enrollmentId) { //frontend parameters
        if (enrollmentId < 1)
            throw new IllegalArgumentException("Enrollment ID must be a positive number.");
        if (!enrollmentDao.idExistsEnrollment(enrollmentId))
            throw new IllegalArgumentException("Enrollment not found.");

        Enrollment enrollment = enrollmentDao.findEnrollmentById(enrollmentId);

        if (!enrollment.isActive())
            throw new IllegalStateException("This enrollment is already inactive.");

        enrollment.setStatus(StatusEnum.DROPPED);
        enrollment.setActive(false);
        enrollment.setCountedInGpa(false);

        boolean updated = enrollmentDao.updateEnrollmentStatus(enrollment);
        if (!updated)
            throw new RuntimeException("Failed to drop course. Please try again.");
    }

    
    //  GET ALL ENROLLMENTS

    public List<Enrollment> getAllEnrollments() { 
        List<Enrollment> enrollments = enrollmentDao.findAllEnrollments();
        if (enrollments.isEmpty())
            throw new IllegalStateException("No enrollments found.");
        return enrollments;
    }

//    all active course
    //  GET BY STUDENT (active courses this semester)
    
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");
        if (!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student not found.");

        List<Enrollment> enrollments = enrollmentDao.findEnrollmentsByStudent(studentId);
        if (enrollments.isEmpty())
            throw new IllegalStateException("No active enrollments found for this student.");
        return enrollments;
    }

    
    //  GET BY OFFERING 
    // //students in a course — lecturer view
    
    public List<Enrollment> getEnrollmentsByOffering(int offeringId) {  //frontend parameters-lec dashboard
        if (offeringId < 1)
            throw new IllegalArgumentException("Offering ID must be a positive number.");
        if (!offeringDao.idExistsOffering(offeringId))
            throw new IllegalArgumentException("Course offering not found.");

        List<Enrollment> enrollments = enrollmentDao.findEnrollmentsByOffering(offeringId);
        if (enrollments.isEmpty())
            throw new IllegalStateException("No students enrolled in this course offering.");
        return enrollments;
    }

    
    //  GET BY ID
  
    public Enrollment getEnrollmentById(int enrollmentId) {  //frontend parameters
        if (enrollmentId < 1)
            throw new IllegalArgumentException("Enrollment ID must be a positive number.");
        Enrollment enrollment = enrollmentDao.findEnrollmentById(enrollmentId);
        if (enrollment == null)
            throw new IllegalStateException("Enrollment not found for ID: " + enrollmentId);
        return enrollment;
    }
}
