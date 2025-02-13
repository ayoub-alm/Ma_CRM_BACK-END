package com.sales_scout.service;


import com.sales_scout.dto.request.RoleRequestDto;
import com.sales_scout.dto.response.RoleResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.Role;
import com.sales_scout.exception.DataAlreadyExistsException;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.mapper.RoleMapper;
import com.sales_scout.repository.CompanyRepository;
import com.sales_scout.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    public final CompanyRepository companyRepository;
    public RoleService(RoleRepository roleRepository, CompanyRepository companyRepository) {
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
    }

    /**
     * This function allows to get all non-soft-deleted roles.
     *
     * @return List of roles.
     */
    public List<RoleResponseDto> getAllRoles() throws DataNotFoundException {
        List<Role> roles = roleRepository.findAllByDeletedAtIsNull();
        if (!roles.isEmpty() && roles != null){
            return roles.stream().map(RoleMapper::fromEntity).collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data of Roles Not Found : List of Roles Not Found",777L);
        }
    }

    /**
     * Get role by id.
     *
     * @param id {Long} ID of the role.
     * @return {Role} The role entity.
     */
    public RoleResponseDto getRoleById(Long id) throws DataNotFoundException {
        Role role = roleRepository.findByIdAndDeletedAtIsNull(id);
        if(role != null){
            return RoleMapper.fromEntity(role);
        }else {
            throw new DataNotFoundException("Role with Id : "+id+" Not Found",555L);
        }
    }

    /**
     * Create a new role.
     *
     * @param roleRequestDto {Role} The role to be created.
     * @return {Role} The created role.
     */
    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) throws DataAlreadyExistsException {
        Role roleCheck = roleRepository.findByRoleAndDeletedAtIsNull(roleRequestDto.getRole());
        if (roleCheck == null){
            Role role = RoleMapper.fromDto(roleRequestDto);
            Role roleSave = roleRepository.save(role);
            return RoleMapper.fromEntity(roleSave);
        }else {
            throw new DataAlreadyExistsException("Role Name Already exists ",111L);
        }
    }

    /**
     * Update an existing role.
     *
     * @param id {Long} ID of the role to be updated.
     * @param updatedRole {Role} The updated role data.
     * @return {Role} The updated role entity.
     */
    public RoleResponseDto updateRole(Long id, RoleRequestDto updatedRole) throws DataNotFoundException {
        Role role = roleRepository.findByIdAndDeletedAtIsNull(id);
        if (role != null) {
            role.setRole(updatedRole.getRole());
            role.setDescription(updatedRole.getDescription());
            Optional<Company> company = companyRepository.findByDeletedAtIsNullAndId(updatedRole.getCompanyId());
            if (updatedRole.getCompanyId() != null){
                role.setCompany(company.get());
            }
            Role roleSave = roleRepository.save(role);
            return RoleMapper.fromEntity(roleSave);
        } else {
            throw new DataNotFoundException("Role with Id "+id+" Not Found",888L);
        }
    }

    /**
     * Soft delete a role by setting its deletedAt attribute.
     * @param id {Long} ID of the role to delete.
     */
    public Boolean deleteRole(Long id) throws DataNotFoundException {
        Role role = roleRepository.findByIdAndDeletedAtIsNull(id);
        if (role != null) {
           roleRepository.delete(role);
           return true;
        } else {
           throw new DataNotFoundException("Role with Id "+ id + " Not Found",258L);
        }
    }
}