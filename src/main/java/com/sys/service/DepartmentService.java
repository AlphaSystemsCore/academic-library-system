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
        if (saved)
            System.out.println("Department added: " + name);
        else
            System.out.println("Faileed to add department");
    }

    public List<Department> getAllDepartments(){
        List<Department> departments = departmentDao.findAllDepartments();
        if(departments.isEmpty())
            System.out.print("No departments found.");

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
    
    if(!departmentDao.idExistsDeapartment(departmentId))
        throw new IllegalArgumentException("The department not found: " + departmentId);

    
    if(departmentDao.nameExistsDepartment(newName))
        throw new IllegalArgumentException("Name already taken: " + newName);

    Department department = new Department(newName, newDescription);
    boolean success = departmentDao.updateDeparment(department, departmentId);
    
    if (success)
        System.out.println("Deaprtment updated successfully");
    else
        System.out.println("Failed to update  department");
    }

    public void deleteDepartment(int departmentId){
        if(!departmentDao.idExistsDeapartment(departmentId))
            throw new IllegalArgumentException("Deparment not found:" + departmentId);

        boolean deleted = departmentDao.deleteDepartment(departmentId);
        if(deleted)
            System.out.println("department deleted successfully");
        else
            System.out.println("Failed to delete department");
    }
}
