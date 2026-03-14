package com.sys.service;

import java.util.List;
import com.sys.dao.DepartmentDao;
import com.sys.model.Department;

public class DepartmentService {

    private final DepartmentDao departmentDao = new DepartmentDao();

    public void addDepartment(String name, String description) { //frontend parameters
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Department name cannot be blank.");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description cannot be blank.");
        if (departmentDao.nameExistsDepartment(name))
            throw new IllegalArgumentException("A department with this name already exists.");

        Department dept = new Department(name, description);
        boolean saved = departmentDao.saveDepartment(dept);
        if (!saved)
            throw new RuntimeException("Failed to save department. Please try again.");
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = departmentDao.findAllDepartments();
        if (departments.isEmpty())
            throw new IllegalStateException("No departments found.");
        return departments;
    }

    public Department getDepartmentById(int departmentId) {  //frontend parameters
        if (departmentId < 1)
            throw new IllegalArgumentException("Department ID must be a positive number.");
        Department dept = departmentDao.findByIdDepartment(departmentId);
        if (dept == null)
            throw new IllegalStateException("Department not found for ID: " + departmentId);
        return dept;
    }

    public void updateDepartment(int departmentId, String name, String description) {  //frontend parameters
        if (departmentId < 1)
            throw new IllegalArgumentException("Department ID must be a positive number.");
        if (!departmentDao.idExistsDepartment(departmentId))
            throw new IllegalArgumentException("Department not found.");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Department name cannot be blank.");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description cannot be blank.");

        Department dept = departmentDao.findByIdDepartment(departmentId);
        if (!dept.getName().equals(name) && departmentDao.nameExistsDepartment(name))
            throw new IllegalArgumentException("A department with this name already exists.");

        dept.setName(name);
        dept.setDescription(description);

        boolean updated = departmentDao.isUpdateDeparment(dept);
        if (!updated)
            throw new RuntimeException("Failed to update department. Please try again.");
    }

    public void deleteDepartment(int departmentId) {  //frontend parameters
        if (departmentId < 1)
            throw new IllegalArgumentException("Department ID must be a positive number.");
        if (!departmentDao.idExistsDepartment(departmentId))
            throw new IllegalArgumentException("Department not found.");
        boolean deleted = departmentDao.deleteDepartment(departmentId);
        if (!deleted)
            throw new RuntimeException("Failed to delete department. Please try again.");
    }
}
