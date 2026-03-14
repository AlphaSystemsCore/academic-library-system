package com.sys.service;

import java.util.List;
import com.sys.dao.RolesDao;
import com.sys.model.Roles;

public class RoleService {

    private final RolesDao rolesDao = new RolesDao();

    public void addRole(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Role name cannot be blank.");
        if (rolesDao.nameExistsRole(name))
            throw new IllegalArgumentException("A role with this name already exists.");
        Roles role = new Roles(name);
        boolean saved = rolesDao.saveRole(role);
        if (!saved)
            throw new RuntimeException("Failed to save role. Please try again.");
    }

    public List<Roles> getAllRoles() {
        List<Roles> roles = rolesDao.findAllRoles();
        if (roles.isEmpty())
            throw new IllegalStateException("No roles found.");
        return roles;
    }

    public Roles getRoleById(int roleId) {
        if (roleId < 1)
            throw new IllegalArgumentException("Role ID must be a positive number.");
        Roles role = rolesDao.findRoleById(roleId);
        if (role == null)
            throw new IllegalStateException("Role not found for ID: " + roleId);
        return role;
    }

    public Roles getRoleByName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Role name cannot be blank.");
        Roles role = rolesDao.findRoleByName(name);
        if (role == null)
            throw new IllegalStateException("No role found with name: " + name);
        return role;
    }
}
