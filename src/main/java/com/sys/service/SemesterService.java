package com.sys.service;

import java.time.LocalDate;
import java.util.List;
import com.sys.dao.SemesterDao;
import com.sys.model.Semester;

public class SemesterService {

    private final SemesterDao semesterDao = new SemesterDao();

    public void addSemester(int academicYear, int semesterNumber,
                            LocalDate startDate, LocalDate endDate, boolean isCurrent) {

        if (academicYear < 2000 || academicYear > LocalDate.now().getYear() + 5)
            throw new IllegalArgumentException("Invalid academic year.");
        if (semesterNumber < 1 || semesterNumber > 3)
            throw new IllegalArgumentException("Semester number must be 1, 2, or 3.");
        if (startDate == null)
            throw new IllegalArgumentException("Start date cannot be null.");
        if (endDate == null)
            throw new IllegalArgumentException("End date cannot be null.");
        if (!endDate.isAfter(startDate))
            throw new IllegalArgumentException("End date must be after start date.");

        Semester semester = new Semester(academicYear, semesterNumber, startDate, endDate, isCurrent);
        boolean saved = semesterDao.isSaveSemester(semester);
        if (!saved)
            throw new RuntimeException("Failed to save semester. Please try again.");
    }

    public List<Semester> getAllSemesters() {
        List<Semester> semesters = semesterDao.findAllSemester();
        if (semesters.isEmpty())
            throw new IllegalStateException("No semesters found.");
        return semesters;
    }

    public Semester getSemesterById(int semesterId) {
        if (semesterId < 1)
            throw new IllegalArgumentException("Semester ID must be a positive number.");
        Semester semester = semesterDao.findByIdSemester(semesterId);
        if (semester == null)
            throw new IllegalStateException("Semester not found for ID: " + semesterId);
        return semester;
    }

    public Semester getCurrentSemester() {
        Semester semester = semesterDao.findCurrentSemester();
        if (semester == null)
            throw new IllegalStateException("No current semester is set.");
        return semester;
    }

    public void setCurrentSemester(int semesterId) {
        if (semesterId < 1)
            throw new IllegalArgumentException("Semester ID must be a positive number.");
        if (!semesterDao.idExistsSemester(semesterId))
            throw new IllegalArgumentException("Semester not found.");
        boolean updated = semesterDao.isSetCurrentSemester(semesterId);
        if (!updated)
            throw new RuntimeException("Failed to set current semester. Please try again.");
    }

    public void updateSemester(int semesterId, int academicYear, int semesterNumber,
                               LocalDate startDate, LocalDate endDate, boolean isCurrent) {

        if (semesterId < 1)
            throw new IllegalArgumentException("Semester ID must be a positive number.");
        if (!semesterDao.idExistsSemester(semesterId))
            throw new IllegalArgumentException("Semester not found.");
        if (academicYear < 2000 || academicYear > LocalDate.now().getYear() + 5)
            throw new IllegalArgumentException("Invalid academic year.");
        if (semesterNumber < 1 || semesterNumber > 3)
            throw new IllegalArgumentException("Semester number must be 1, 2, or 3.");
        if (startDate == null)
            throw new IllegalArgumentException("Start date cannot be null.");
        if (endDate == null)
            throw new IllegalArgumentException("End date cannot be null.");
        if (!endDate.isAfter(startDate))
            throw new IllegalArgumentException("End date must be after start date.");

        Semester semester = semesterDao.findByIdSemester(semesterId);
        semester.setAcademicYear(academicYear);
        semester.setSemesterNumber(semesterNumber);
        semester.setStartDate(startDate);
        semester.setEndDate(endDate);
        semester.setCurrent(isCurrent);

        boolean updated = semesterDao.isUpdateSemester(semester);
        if (!updated)
            throw new RuntimeException("Failed to update semester. Please try again.");
    }

    public void deleteSemester(int semesterId) {
        if (semesterId < 1)
            throw new IllegalArgumentException("Semester ID must be a positive number.");
        if (!semesterDao.idExistsSemester(semesterId))
            throw new IllegalArgumentException("Semester not found.");
        boolean deleted = semesterDao.isDeleteSemester(semesterId);
        if (!deleted)
            throw new RuntimeException("Failed to delete semester. Please try again.");
    }
}
