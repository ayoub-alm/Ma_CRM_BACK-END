package com.sales_scout.controller;


import com.sales_scout.dto.request.RoleRequestDto;
import com.sales_scout.dto.response.RoleResponseDto;
import com.sales_scout.entity.Role;
import com.sales_scout.exception.DataAlreadyExistsException;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Get all roles.
     *
     * @return List of roles.
     */
    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() throws DataNotFoundException {
        try {
            List<RoleResponseDto> roles = roleService.getAllRoles();
            return ResponseEntity.ok(roles);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(), e.getCode());
        }
    }

    /**
     * Get role by ID.
     * @param id Role ID.
     * @return Role entity.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable Long id)throws DataNotFoundException {
        try{
            RoleResponseDto role = roleService.getRoleById(id);
            return ResponseEntity.ok(role);
        }catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage(), e.getCode());
        }
    }

    /**
     * Create a new role.
     *
     * @param roleRequestDto New role data.
     * @return Created role.
     */
    @PostMapping
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody RoleRequestDto roleRequestDto) throws DataAlreadyExistsException {
        try {
            RoleResponseDto createdRole = roleService.createRole(roleRequestDto);
            return ResponseEntity.ok(createdRole);
        } catch (DataAlreadyExistsException e) {
            throw new DataAlreadyExistsException(e.getMessage(), e.getCode());
        }
    }

    /**
     * Update an existing role.
     * @param id Role ID to update.
     * @param roleRequestDto Updated role data.
     * @return Updated role entity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> updateRole(@PathVariable Long id, @RequestBody RoleRequestDto roleRequestDto) throws DataNotFoundException{
        try{
            RoleResponseDto updatedRole = roleService.updateRole(id, roleRequestDto);
            return ResponseEntity.ok(updatedRole);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(), e.getCode());
        }
    }

    /**
     * Soft delete a role.
     *
     * @param id Role ID to delete.
     * @return Success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRole(@PathVariable Long id) throws DataNotFoundException{
        try{
            return ResponseEntity.ok(roleService.deleteRole(id));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

}
