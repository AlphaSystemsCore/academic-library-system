package com.sys.service;

import java.util.List;
import com.sys.dao.DepartmentDao;
import com.sys.dao.ProgrammeDao;
import com.sys.model.Programme;
import com.sys.model.Programme.LevelEnum;

public class ProgrammeService {

    private final ProgrammeDao programmeDao = new ProgrammeDao();
    private final DepartmentDao departmentDao = new DepartmentDao();

    public void addProgramme(String name, String code, LevelEnum level,
                             int durationYears, int departmentId) {

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Programme name cannot be blank.");
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("Programme code cannot be blank.");
        if (level == null)
            throw new IllegalArgumentException("Programme level cannot be null.");
        if (durationYears < 1 || durationYears > 10)
            throw new IllegalArgumentException("Duration years must be between 1 and 10.");
        if (departmentId < 1)
            throw new IllegalArgumentException("Department ID must be a positive number.");
        if (!departmentDao.idExistsDepartment(departmentId))
            throw new IllegalArgumentException("Department does not exist.");
        if (programmeDao.nameExistsProgramme(name))
            throw new IllegalArgumentException("A programme with this name already exists.");

        Programme programme = new Programme(name, code, level, durationYears, departmentId);
        boolean saved = programmeDao.saveProgramme(programme);
        if (!saved)
            throw new RuntimeException("Failed to save programme. Please try again.");
    }

    public List<Programme> getAllProgrammes() {
        List<Programme> programmes = programmeDao.findAllProgrammes();
        if (programmes.isEmpty())
            throw new IllegalStateException("No programmes found.");
        return programmes;
    }

    public Programme getProgrammeById(int programmeId) {
        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");
        Programme programme = programmeDao.findProgrammeById(programmeId);
        if (programme == null)
            throw new IllegalStateException("Programme not found for ID: " + programmeId);
        return programme;
    }

    public void updateProgramme(int programmeId, String name, String code,
                                LevelEnum level, int durationYears, int departmentId) {

        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");
        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme not found.");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Programme name cannot be blank.");
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("Programme code cannot be blank.");
        if (level == null)
            throw new IllegalArgumentException("Programme level cannot be null.");
        if (durationYears < 1 || durationYears > 10)
            throw new IllegalArgumentException("Duration years must be between 1 and 10.");
        if (departmentId < 1)
            throw new IllegalArgumentException("Department ID must be a positive number.");
        if (!departmentDao.idExistsDepartment(departmentId))
            throw new IllegalArgumentException("Department does not exist.");

        Programme existing = programmeDao.findProgrammeById(programmeId);
        if (!existing.getName().equals(name) && programmeDao.nameExistsProgramme(name))
            throw new IllegalArgumentException("A programme with this name already exists.");

        existing.setName(name);
        existing.setProgrammeCode(code);
        existing.setLevel(level);
        existing.setDurationYears(durationYears);
        existing.setDepartmentId(departmentId);

        boolean updated = programmeDao.isUpdateProgramme(existing);
        if (!updated)
            throw new RuntimeException("Failed to update programme. Please try again.");
    }

    public void deleteProgramme(int programmeId) {
        if (programmeId < 1)
            throw new IllegalArgumentException("Programme ID must be a positive number.");
        if (!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme not found.");
        boolean deleted = programmeDao.deleteProgramme(programmeId);
        if (!deleted)
            throw new RuntimeException("Failed to delete programme. Please try again.");
    }
}
