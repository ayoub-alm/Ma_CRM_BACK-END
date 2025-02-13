package com.sales_scout.service;

import com.sales_scout.dto.request.create.CompanyDepartmentRequestDTO;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.CompanyDepartment;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.repository.CompanyDepartmentRepository;
import com.sales_scout.repository.CompanyRepository;
import com.sales_scout.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyDepartmentService {
    private final CompanyDepartmentRepository companyDepartmentRepository;
    private final CompanyRepository companyRepository;
    @Autowired
    private final UserRepository userRepository;

    public CompanyDepartmentService(CompanyDepartmentRepository companyDepartmentRepository, CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyDepartmentRepository = companyDepartmentRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all departments (all with soft-deleted)
     * @return {List<CompanyDepartment>}
     */
    public List<CompanyDepartment> getAllDepartments(){
        return this.companyDepartmentRepository.findAll();
    }

    /**
     * This function allows to get all departments within the soft-deleted
     * @return List<CompanyDepartment>
     */
    public List<CompanyDepartment> getAllDepartmentsWithinDeleted(){
        return this.companyDepartmentRepository.findByDeletedAtIsNull();
    }

    /**
     * This function allows us to get all departments for a company by his id
     * @param companyId {Long} the id for company
     * @return List<CompanyDepartment>
     */
    public List<CompanyDepartment> getAllCompanyDepartmentsByCompanyIdAndDeletedAtIsNull(Long companyId){
        return this.companyDepartmentRepository.findByCompanyIdAndDeletedAtIsNull(companyId);
    }

    /**
     * This function allows to get all departments including soft-deleted departments for a company
     * @param companyId {Long}
     * @return { List<CompanyDepartment>} list of departments
     */
    public List<CompanyDepartment> getAllCompanyDepartmentsByCompanyId(Long companyId){
        return this.companyDepartmentRepository.findByCompanyId(companyId);
    }

    /**
     * Get department by company ID
     * @param id id of department
     * @return { Optional<CompanyDepartment> }
     */
    public Optional<CompanyDepartment> getCompanyDepartmentById(Long id){
        return this.companyDepartmentRepository.findByIdAndDeletedAtIsNull(id);
    }

    /**
     * get soft-deleted department by id
     * @param id the id of department
     * @return Optional<CompanyDepartment>
     */
    public Optional<CompanyDepartment> getSoftDeletedCompanyDepartmentById(Long id){
        return this.companyDepartmentRepository.findByIdAndDeletedAtIsNotNull(id);
    }

    /**
     * Get soft-deleted departments by company id
     * @param companyId {Long} the id of company
     * @return {Optional<CompanyDepartment> } list of departments
     */
    public Optional<CompanyDepartment> getCompanyDepartmentByCompanyIdAndDeletedAtIsNotNull(Long companyId){
        return this.companyDepartmentRepository.findByCompanyIdAndDeletedAtIsNotNull(companyId);
    }

    /**
     * this function allows to create now company department
     * @param companyDepartmentDto data to create new department
     * @return {Optional<CompanyDepartment>} created department
     * @throws EntityNotFoundException
     */
    public Optional<CompanyDepartment> createCompanyDepartment(CompanyDepartmentRequestDTO companyDepartmentDto) {
        CompanyDepartment companyDepartment1 = new CompanyDepartment();

        Company company = companyRepository.findByDeletedAtIsNullAndId(companyDepartmentDto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyDepartmentDto.getCompanyId()));
        companyDepartment1.setCompany(company);

        UserEntity user = userRepository.findByDeletedAtIsNullAndId(companyDepartmentDto.getResponsibleId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", companyDepartmentDto.getResponsibleId()));
        companyDepartment1.setResponsible(user);

        companyDepartment1.setName(companyDepartmentDto.getName());
        return Optional.of(companyDepartmentRepository.save(companyDepartment1));
    }


    /**
     * This function allows to update company department
     * @param companyDepartmentDto necessary data to update department
     * @return {Optional<CompanyDepartment>} updated department
     * @throws EntityNotFoundException in case of user or company not found
     */
    public Optional<CompanyDepartment> updateCompanyDepartments(CompanyDepartmentRequestDTO companyDepartmentDto)
         throws EntityNotFoundException{
            CompanyDepartment companyDepartment1 = new CompanyDepartment();

            Company company = this.companyRepository.findByDeletedAtIsNullAndId(companyDepartmentDto.getCompanyId())
                    .orElseThrow(()-> new EntityNotFoundException("Company not found"));
            companyDepartment1.setCompany(company);

            UserEntity user = userRepository.findById(companyDepartmentDto.getResponsibleId())
                    .orElseThrow(()-> new EntityNotFoundException("User not found"));
            companyDepartment1.setResponsible(user);

            companyDepartment1.setName(companyDepartmentDto.getName());
            return Optional.of(this.companyDepartmentRepository.save(companyDepartment1));
    }

    /**
     * This function allows to soft-delete departments
     * @param id {Long} id of department to be soft-deleted
     * @return {Boolean}
     */
    public boolean SoftDeleteCompanyDepartment(Long id) {
       Optional<CompanyDepartment> department = this.companyDepartmentRepository.findByIdAndDeletedAtIsNull(id);
       if(department.isPresent()){
          department.get().setDeletedAt(LocalDateTime.now());
          CompanyDepartment companyDepartment = this.companyDepartmentRepository.save(department.get());
           return true;
       }else{
           return false;
       }
    }

    /**
     * Restore the soft-deleted department
     * @param id {Long} the id of departments to restore
     * @return {Boolean}
     * @throws Exception {Exception}
     */
    public Boolean restoreSoftDeleteCompanyDepartment(Long id) throws Exception{
        Optional<CompanyDepartment> department = this.companyDepartmentRepository.findByIdAndDeletedAtIsNotNull(id);
        if(department.isPresent()){
            department.get().setDeletedAt(null);
            this.companyDepartmentRepository.save(department.get());
            return true;
        }else{
            return false;
        }
    }
}
