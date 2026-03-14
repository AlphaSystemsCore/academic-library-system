package com.sys.service;

import java.util.List;
import com.sys.dao.CourseDao;
import com.sys.dao.ProgrammeDao;
import com.sys.model.Course;

public class CourseService {

    private final CourseDao courseDao = new CourseDao();
    private final ProgrammeDao programmeDao = new ProgrammeDao();


    //  ADD COURSE
    public void addCourse(String courseCode, String title, int creditHours, int programmeId) {  //frontend parameters

        if (courseCode == null || courseCode.isBlank())
            throw new IllegalArgumentException("Course code cannot be blank.");

        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Course title cannot be blank.");

        if (creditHours < 1 || creditHours > 10)
            throw new IllegalArgumentException("Credit hours must be between 1 and 10.");

        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");

        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme does not exist.");

        if (courseDao.courseCodeExists(courseCode))
            throw new IllegalArgumentException("Course code already exists.");

        Course course = new Course(courseCode, title, creditHours, programmeId);

        boolean saved = courseDao.saveCourse(course);
        if (!saved)
            throw new RuntimeException("Failed to save course. Please try again.");
    }


    //  GET ALL COURSES

    public List<Course> getAllCourses() {
        List<Course> courses = courseDao.findAllCourses();
        if (courses.isEmpty())
            throw new IllegalStateException("No courses found.");
        return courses;
    }

   
    //  GET BY PROGRAMME

    public List<Course> getCoursesByProgramme(int programmeId) {  //frontend parameters
        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");

        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme does not exist.");

        List<Course> courses = courseDao.findCoursesByProgramme(programmeId);
        if (courses.isEmpty())
            throw new IllegalStateException("No courses found for this programme.");
        return courses;
    }


    //  GET BY ID

    public Course getCourseById(int courseId) {  //frontend parameters
        if (courseId < 1)
            throw new IllegalArgumentException("Course ID must be a positive number.");

        Course course = courseDao.findCourseById(courseId);
        if (course == null)
            throw new IllegalStateException("Course not found for ID: " + courseId);
        return course;
    }


    //  UPDATE COURSE

    public void updateCourse(int courseId, String courseCode, String title,
                             int creditHours, int programmeId) {   //frontend parameters

        if (courseId < 1)
            throw new IllegalArgumentException("Course ID must be a positive number.");

        if (!courseDao.idExistsCourse(courseId))
            throw new IllegalArgumentException("Course not found.");

        if (courseCode == null || courseCode.isBlank())
            throw new IllegalArgumentException("Course code cannot be blank.");

        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Course title cannot be blank.");

        if (creditHours < 1 || creditHours > 10)
            throw new IllegalArgumentException("Credit hours must be between 1 and 10.");

        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");

        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme does not exist.");

        // Allow same code only if it belongs to THIS course #for it to be valid
        Course existing = courseDao.findCourseById(courseId);
        if (!existing.getCourseCode().equals(courseCode) && courseDao.courseCodeExists(courseCode))
            throw new IllegalArgumentException("Course code is already taken by another course.");

        // existing.setCourseCode(courseCode);
        // existing.setTitle(title);
        // existing.setCreditHours(creditHours);
        // existing.setProgrammeId(programmeId);

        boolean updated = courseDao.updateCourse(courseId, courseCode, title, creditHours, programmeId);
        if (!updated)
            throw new RuntimeException("Failed to update course. Please try again.");
    }

    
    //  DELETE COURSE
    public void deleteCourse(int courseId) {  //frontend parameters
        if (courseId < 1)
            throw new IllegalArgumentException("Course ID must be a positive number.");

        if (!courseDao.idExistsCourse(courseId))
            throw new IllegalArgumentException("Course not found.");

        boolean deleted = courseDao.deleteCourse(courseId);
        if (!deleted)
            throw new RuntimeException("Failed to delete course. Please try again.");
    }
}
