package com.sys.service;

import com.sys.model.Department;
import com.sys.dao.DepartmentDao;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
    private DepartmentDao departmentDao = new DepartmentDao();

    public void addDepartment(String name, String description){
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Department name cannot be empy");
        if(description == null || description.isBlank())
            throw new IllegalArgumentException("descritption cannot be emplty");
        if(name.length() > 100)
            throw new IllegalArgumentException("Name too long - max 100 characters");
        if(departmentDao.nameExistsDepartment(name))
            throw new IllegalArgumentException("Departmant name already exists" + name);

        Department department = new Department(name, description);
        boolean saved = departmentDao.saveDepartment(department);
        if (!saved)
            throw new IllegalArgumentException("Failed to save, please try again");
        
    }

    public List<Department> getAllDepartments(){
        List<Department> departments = departmentDao.findAllDepartments();
        if(departments.isEmpty())
            throw new IllegalArgumentException("Department not found");

        return departments;
    }

    public Department getDepartmentById(int departmentId){
        if(departmentId <=0)
            throw new IllegalArgumentException("Invalid department ID");

        Department department = departmentDao.findByIdDepartment(departmentId);

        if(department == null)
            throw new IllegalArgumentException("Department not fouind with ID " + departmentId );

        return department;
    }

    public void updateDeparment(int departmentId, String newName, String newDescription){
    if(newName == null || newName.isBlank())
        throw new IllegalArgumentException("New name can not be empty") ;

    if(newDescription == null || newDescription.isBlank())
        throw new IllegalArgumentException("New Description can not be empty");
    
    if(!departmentDao.idExistsDepartment(departmentId))
        throw new IllegalArgumentException("The department not found: " + departmentId);

    
    if(departmentDao.nameExistsDepartment(newName))
        throw new IllegalArgumentException("Name already taken: " + newName);

    Department department = new Department(departmentId,newName, newDescription);
    boolean success = departmentDao.isUpdateDeparment(department);
    
    if (!success)
        throw new IllegalArgumentException("Failed to update please try again.");
    }

    public void deleteDepartment(int departmentId){
        if(!departmentDao.idExistsDepartment(departmentId))
            throw new IllegalArgumentException("Deparment not found:" + departmentId);

        boolean deleted = departmentDao.deleteDepartment(departmentId);
        if(!deleted)
            throw new RuntimeException("Failed to delete, please try agian");
        
         
    }
}
