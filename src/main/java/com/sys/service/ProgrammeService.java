package com.sys.service;

import java.util.List;
import java.util.ArrayList;
import com.sys.model.Programme;
import com.sys.model.Programme.*;
import com.sys.dao.*;


public class ProgrammeService {
    ProgrammeDao programmeDao = new ProgrammeDao();
    DepartmentDao departmentDao = new DepartmentDao();
    StudentDao studentDao = new StudentDao();
    CourseDao courseDao = new CourseDao();
    public void addProgrammeService(String name, String programmeCode, LevelEnum level, int durationYears, int departmentId){
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("Programme Name can not be null.");
        if(programmeCode == null || programmeCode.isBlank())
            throw new IllegalArgumentException("Programme Code can not be null.");
        if(level == null)
            throw new IllegalArgumentException("You must select a level.");
        if(durationYears < 1 )
            throw new IllegalArgumentException("Duration must be aleast 1 year.");
        if(durationYears > 6)
            throw new IllegalArgumentException("Duration can't exceed 6 years.");
        if(departmentId < 1)
            throw new IllegalArgumentException("Department Id can not be negative integer.");

        // bs rules
        if(programmeDao.nameExistsProgramme(name))
            throw new IllegalArgumentException("Name already exists.");
        if(!departmentDao.idExistsDepartment(departmentId))
            throw new IllegalArgumentException("Department Id does not exist");
        Programme programme = new Programme(name, programmeCode, level, durationYears, departmentId);
        boolean saved = programmeDao.saveProgramme(programme);
        if(!saved)
            throw new RuntimeException("Not saved, please try again");   
    }

    public List<Programme> viewAllProgrammeService(){
         List<Programme> programmes = programmeDao.findAllProgrammes();
        if(programmes.isEmpty())
            throw new IllegalArgumentException("No program Found.");
        return programmes;

    }

    public void deleteProgrammeService(int programmeId){
        if(programmeId < 1)
            throw new IllegalArgumentException("Invalid programme id");
        if(!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Programme Id doest no exist");
       
        // integrity referencial vvd
        if (studentDao.hasStudentProgramme(programmeId))
            throw new IllegalArgumentException("Cannot delete — students are registered under this programme.");

        if (courseDao.hasProgramme(programmeId))
            throw new IllegalArgumentException("Cannot delete — courses are linked to this programme.");

         boolean deleted = programmeDao.deleteProgramme(programmeId);
        if(!deleted)
            throw new RuntimeException("Programme not delete, try agiain");

    }

    public void updateProgrammeAllService(int programmeId, String name, String code, LevelEnum level, int durationYears, int departmentId){
        if(programmeId < 1)
            throw new IllegalArgumentException("Progrma id Must be a positive integer.");
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("Program name can not be null");
        if(code == null || code.isBlank())
            throw new IllegalArgumentException("Code can not be null");
        if(level == null)
            throw new IllegalArgumentException("Must selct level.");
        if(durationYears<1)
            throw new IllegalArgumentException("The least duration year is 1.");
        if(durationYears > 6)
            throw new IllegalArgumentException("Duration years can not exceed 6");
        if(departmentId <1)
            throw new IllegalArgumentException("Department id must be a posive integer");
        // bs logic vals
        if(!programmeDao.idExistsProgramme(programmeId))
            throw new IllegalArgumentException("Program id dont exist, please try again");
        if(!departmentDao.idExistsDepartment(departmentId))
            throw new IllegalArgumentException("Department id dont exist, please try again.");
        if(programmeDao.nameExistsProgramme(name))
            throw new IllegalArgumentException("Name alraedy exists try another name please");
        Programme programme = new Programme(programmeId, name, code, level, durationYears, departmentId);

        if(!programmeDao.isUpdateProgramme(programme))
            throw new RuntimeException("Failed to update please try again");

    }

    public Programme getByIdProgrammeService(int programmeId){
        if(programmeId <1)
            throw new IllegalArgumentException("Programme Id must be positive number");
        Programme programme = programmeDao.findProgrammeById(programmeId);
        if(programme == null)
            throw new IllegalArgumentException("No program Found");
        return programme;
    }


    
}
