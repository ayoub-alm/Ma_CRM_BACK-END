package com.sales_scout.service;


import com.sales_scout.entity.Role;
import com.sales_scout.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * This function allows to get all non-soft-deleted roles.
     *
     * @return List of roles.
     */
    public List<Role> getAllRoles() {
        return roleRepository.findByDeletedAtIsNull();
    }

    /**
     * Get role by id.
     *
     * @param id {Long} ID of the role.
     * @return {Role} The role entity.
     */
    public Role getRoleById(Long id) {
        return roleRepository.findByIdAndDeletedAtIsNull(id);
    }

    /**
     * Create a new role.
     *
     * @param role {Role} The role to be created.
     * @return {Role} The created role.
     */
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    /**
     * Update an existing role.
     *
     * @param id {Long} ID of the role to be updated.
     * @param updatedRole {Role} The updated role data.
     * @return {Role} The updated role entity.
     */
    public Role updateRole(Long id, Role updatedRole) {
        Role role = this.getRoleById(id);
        if (role != null) {
            role.setRole(updatedRole.getRole());
            role.setDescription(updatedRole.getDescription());
            return roleRepository.save(role);
        } else {
            throw new RuntimeException("Role not found with id " + id);
        }
    }

    /**
     * Soft delete a role by setting its deletedAt attribute.
     *
     * @param id {Long} ID of the role to delete.
     */
    public void deleteRole(Long id) {
        Role role = this.getRoleById(id);
        if (role != null) {
            role.setDeletedAt(LocalDateTime.now());
            roleRepository.save(role);
        } else {
            throw new RuntimeException("Role not found with id " + id);
        }
    }
}