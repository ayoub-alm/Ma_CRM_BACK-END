package com.sales_scout.repository;



import com.sales_scout.entity.CompanyDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyDepartmentRepository extends JpaRepository<CompanyDepartment, Long> {
    /**
     * Get departments with company ID
     * @param companyId {Long} the ID of company
     * @return {List<CompanyDepartment>} list of department
     */
    List<CompanyDepartment> findByCompanyIdAndDeletedAtIsNull(Long companyId);

     /**
     * Get department by id
     * @param id {Long}  the id of department
     * @return {Optional<CompanyDepartment>} Department
     */
    Optional<CompanyDepartment> findByIdAndDeletedAtIsNull(Long id);

    /**
     * Get all department
     * @return {List<CompanyDepartment>} list of departments
     */
    List<CompanyDepartment> findByDeletedAtIsNull();

    /**
     * Get list of departments by Company ID
     * @param companyId
     * @return
     */
    Optional<CompanyDepartment> findByCompanyIdAndDeletedAtIsNotNull(Long companyId);

    /**
     * Get department from trash
     * @param id {Long} id of department
     * @return {Optional<CompanyDepartment>}
     */
    Optional<CompanyDepartment> findByIdAndDeletedAtIsNotNull(Long id);

    /**
     * Get All trashed departments
     * return {List<CompanyDepartment>} list of departments
     */
    List<CompanyDepartment> findByDeletedAtIsNotNull();

    List<CompanyDepartment> findByCompanyId(Long companyId);
}
