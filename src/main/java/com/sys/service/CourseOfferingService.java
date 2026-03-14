package com.sys.service;

import java.util.List;
import com.sys.dao.CourseDao;
import com.sys.dao.CourseOfferingDao;
import com.sys.dao.LecturerDao;
import com.sys.dao.ProgrammeDao;
import com.sys.dao.SemesterDao;
import com.sys.model.CourseOffering;

public class CourseOfferingService {

    private final CourseOfferingDao offeringDao = new CourseOfferingDao();
    private final CourseDao courseDao = new CourseDao();
    private final LecturerDao lecturerDao = new LecturerDao();
    private final SemesterDao semesterDao = new SemesterDao();
    private final ProgrammeDao programmeDao = new ProgrammeDao();

    
    //  ADD COURSE OFFERING
  
    public void addCourseOffering(int courseId, int lecturerId, int semesterId, int programmeId) {  //frontend parameters

        if (courseId < 1)
            throw new IllegalArgumentException("Course ID must be a positive number.");

        if (lecturerId < 1)
            throw new IllegalArgumentException("Lecturer ID must be a positive number.");

        if (semesterId < 1)
            throw new IllegalArgumentException("Semester ID must be a positive number.");

        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");

        if (!courseDao.idExistsCourse(courseId))
            throw new IllegalArgumentException("Course does not exist.");

        if (!lecturerDao.idExistsLecturer(lecturerId))
            throw new IllegalArgumentException("Lecturer does not exist.");

        if (!semesterDao.idExistsSemester(semesterId))
            throw new IllegalArgumentException("Semester does not exist.");

        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme does not exist.");

        if (offeringDao.offeringExists(courseId, lecturerId, semesterId))
            throw new IllegalArgumentException("This course is already assigned to this lecturer in the selected semester.");

        CourseOffering offering = new CourseOffering(courseId, lecturerId, semesterId, programmeId);

        boolean saved = offeringDao.saveCourseOffering(offering);
        if (!saved)
            throw new RuntimeException("Failed to save course offering. Please try again.");
    }


    //  GET ALL OFFERINGS
  
    public List<CourseOffering> getAllOfferings() {
        List<CourseOffering> offerings = offeringDao.findAllOfferings();
        if (offerings.isEmpty())
            throw new IllegalStateException("No course offerings found.");
        return offerings;
    }

   
    //  GET BY PROGRAMME AND SEMESTER (student view)
  
    public List<CourseOffering> getOfferingsByProgrammeAndSemester(int programmeId, int semesterId) {   //frontend parameters---student portal
        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");

        if (semesterId < 1)
            throw new IllegalArgumentException("Semester ID must be a positive number.");

        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme does not exist.");

        if (!semesterDao.idExistsSemester(semesterId))
            throw new IllegalArgumentException("Semester does not exist.");

        List<CourseOffering> offerings = offeringDao.findOfferingsByProgrammeAndSemester(programmeId, semesterId);
        if (offerings.isEmpty())
            throw new IllegalStateException("No course offerings found for this programme and semester.");
        return offerings;
    }

  
    //  GET BY LECTURER (lecturer view — their assigned courses)

    public List<CourseOffering> getOfferingsByLecturer(int lecturerId, int semesterId) {    //frontend parameters letc portall
        if (lecturerId < 1)
            throw new IllegalArgumentException("Lecturer ID must be a positive number.");

        if (semesterId < 1)
            throw new IllegalArgumentException("Semester ID must be a positive number.");

        if (!lecturerDao.idExistsLecturer(lecturerId))
            throw new IllegalArgumentException("Lecturer does not exist.");

        if (!semesterDao.idExistsSemester(semesterId))
            throw new IllegalArgumentException("Semester does not exist.");

        List<CourseOffering> offerings = offeringDao.findOfferingsByLecturer(lecturerId, semesterId);
        if (offerings.isEmpty())
            throw new IllegalStateException("No courses assigned to this lecturer in the selected semester.");
        return offerings;
    }

   
    //  GET BY ID
   
    public CourseOffering getOfferingById(int offeringId) {  //frontend parameters
        if (offeringId < 1)
            throw new IllegalArgumentException("Offering ID must be a positive number.");

        CourseOffering offering = offeringDao.findOfferingById(offeringId);
        if (offering == null)
            throw new IllegalStateException("Course offering not found for ID: " + offeringId);
        return offering;
    }


    //  UPDATE COURSE OFFERING
  
    public void updateCourseOffering(int offeringId, int courseId, int lecturerId,
                                     int semesterId, int programmeId) {

        if (offeringId < 1)
            throw new IllegalArgumentException("Offering ID must be a positive number.");

        if (!offeringDao.idExistsOffering(offeringId))
            throw new IllegalArgumentException("Course offering not found.");

        if (courseId < 1)
            throw new IllegalArgumentException("Course ID must be a positive number.");

        if (lecturerId < 1)
            throw new IllegalArgumentException("Lecturer ID must be a positive number.");

        if (semesterId < 1)
            throw new IllegalArgumentException("Semester ID must be a positive number.");

        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");

        if (!courseDao.idExistsCourse(courseId))
            throw new IllegalArgumentException("Course does not exist.");

        if (!lecturerDao.idExistsLecturer(lecturerId))
            throw new IllegalArgumentException("Lecturer does not exist.");

        if (!semesterDao.idExistsSemester(semesterId))
            throw new IllegalArgumentException("Semester does not exist.");

        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme does not exist.");

        CourseOffering offering = new CourseOffering(offeringId, courseId, lecturerId, semesterId, programmeId);

        boolean updated = offeringDao.updateCourseOffering(offering);
        if (!updated)
            throw new RuntimeException("Failed to update course offering. Please try again.");
    }


    //  DELETE COURSE OFFERING
  
    public void deleteCourseOffering(int offeringId) {   //frontend parameter
        if (offeringId < 1)
            throw new IllegalArgumentException("Offering ID must be a positive number.");

        if (!offeringDao.idExistsOffering(offeringId))
            throw new IllegalArgumentException("Course offering not found.");

        boolean deleted = offeringDao.deleteCourseOffering(offeringId);
        if (!deleted)
            throw new RuntimeException("Failed to delete course offering. Please try again.");
    }
}
