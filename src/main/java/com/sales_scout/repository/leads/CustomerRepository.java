package com.sales_scout.repository.leads;

import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.Interaction;
import com.sales_scout.enums.ProspectStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> , JpaSpecificationExecutor<Customer> {

    List<Customer> findAllByCompanyId(Long companyId);
    /**
     * get prospects where deleted at is null
     * @return {List<Prospect>}
     */
    List<Customer> findAllByDeletedAtIsNullAndCompanyIdIn(List<Long> companyIds);
    /**
     * Get non-Soft-deleted prospect by ID
     * @return {Optional<Prospect>}
     * @param id {Long}
     */
    Optional<Customer> findByDeletedAtIsNullAndId(Long id);

    List<Customer> findAllByDeletedAtIsNullAndIdIn(List<Long> ids);

    /**
     * Get Soft-deleted prospect by ID
     * @param id
     * @return
     */
    Optional<Customer> findByDeletedAtIsNotNullAndId(Long id);


    @Query("SELECT c.status, COUNT(c.id) " +
            "FROM Customer c " +
            "WHERE (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:statusIds IS NULL OR c.status.id IN :statusIds) " +
            "AND (:industryIds IS NULL OR c.industry.id IN :industryIds) " +
            "AND (:cityIds IS NULL OR c.city.id IN :cityIds) " +
            "AND (:companySizeIds IS NULL OR c.companySize.id IN :companySizeIds) " +
            "AND (:createdByIds IS NULL OR c.createdBy.id IN :createdByIds) " +
            "AND (:affectedToIds IS NULL OR c.affectedTo.id IN :affectedToIds) " +
            "AND c.deletedAt IS NULL " +
            "GROUP BY c.status")
    List<Object[]> getCountOfCustomersByStatusBetweenOptionalDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statusIds") List<Long> statusIds,
            @Param("industryIds") List<Long> industryIds,
            @Param("cityIds") List<Long> cityIds,
            @Param("companySizeIds") List<Long> companySizeIds,
            @Param("createdByIds") List<Long> createdByIds,
            @Param("affectedToIds") List<Long> affectedToIds);



    /**
     * Get the count of customers per interest
     */
    @Query("SELECT ci.interest, COUNT(c.id) " +
            "FROM Customer c " +
            "JOIN c.customerInterests ci " +
            "WHERE c.deletedAt IS NULL " +
            "GROUP BY ci.interest.id")
    List<Object[]> getCountOfCustomerByInterest();

    /**
     * Get the count of customers per seller (createdBy is a UserEntity)
     */
    @Query("SELECT c.createdBy.name, COUNT(c.id) " +
            "FROM Customer c " +
            "WHERE (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:statusIds IS NULL OR c.status.id IN :statusIds) " +
            "AND (:industryIds IS NULL OR c.industry.id IN :industryIds) " +
            "AND (:cityIds IS NULL OR c.city.id IN :cityIds) " +
            "AND (:companySizeIds IS NULL OR c.companySize.id IN :companySizeIds) " +
            "AND (:createdByIds IS NULL OR c.createdBy.id IN :createdByIds) " +
            "AND (:affectedToIds IS NULL OR c.affectedTo.id IN :affectedToIds) " +
            "AND c.deletedAt IS NULL " +
            "GROUP BY c.createdBy.name")
    List<Object[]> getCountOfCustomerBySeller(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              @Param("statusIds") List<Long> statusIds,
                                              @Param("industryIds") List<Long> industryIds,
                                              @Param("cityIds") List<Long> cityIds,
                                              @Param("companySizeIds") List<Long> companySizeIds,
                                              @Param("createdByIds") List<Long> createdByIds,
                                              @Param("affectedToIds") List<Long> affectedToIds);

    /**
     * Get the count of customers per city
     */
    @Query("SELECT c.city.name, COUNT(c.id) " +
            "FROM Customer c " +
            "WHERE (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:statusIds IS NULL OR c.status.id IN :statusIds) " +
            "AND (:industryIds IS NULL OR c.industry.id IN :industryIds) " +
            "AND (:cityIds IS NULL OR c.city.id IN :cityIds) " +
            "AND (:companySizeIds IS NULL OR c.companySize.id IN :companySizeIds) " +
            "AND (:createdByIds IS NULL OR c.createdBy.id IN :createdByIds) " +
            "AND (:affectedToIds IS NULL OR c.affectedTo.id IN :affectedToIds) " +
            "AND c.deletedAt IS NULL " +
            "GROUP BY c.city.name")
    List<Object[]> getCountOfCustomerByCity(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statusIds") List<Long> statusIds,
            @Param("industryIds") List<Long> industryIds,
            @Param("cityIds") List<Long> cityIds,
            @Param("companySizeIds") List<Long> companySizeIds,
            @Param("createdByIds") List<Long> createdByIds,
            @Param("affectedToIds") List<Long> affectedToIds);

    /**
     * Get the count of customers per creation date
     */
    @Query("SELECT FUNCTION('DATE', c.createdAt), COUNT(c.id) " +
            "FROM Customer c " +
            "WHERE (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:statusIds IS NULL OR c.status.id IN :statusIds) " +
            "AND (:industryIds IS NULL OR c.industry.id IN :industryIds) " +
            "AND (:cityIds IS NULL OR c.city.id IN :cityIds) " +
            "AND (:companySizeIds IS NULL OR c.companySize.id IN :companySizeIds) " +
            "AND (:createdByIds IS NULL OR c.createdBy.id IN :createdByIds) " +
            "AND (:affectedToIds IS NULL OR c.affectedTo.id IN :affectedToIds) " +
            "AND c.deletedAt IS NULL " +
            "GROUP BY FUNCTION('DATE', c.createdAt) " +
            "ORDER BY FUNCTION('DATE', c.createdAt) ASC")
    List<Object[]> getCountOfCustomerByDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statusIds") List<Long> statusIds,
            @Param("industryIds") List<Long> industryIds,
            @Param("cityIds") List<Long> cityIds,
            @Param("companySizeIds") List<Long> companySizeIds,
            @Param("createdByIds") List<Long> createdByIds,
            @Param("affectedToIds") List<Long> affectedToIds);




    @Query("SELECT c.proprietaryStructure.name, COUNT(c.id) " +
            "FROM Customer c " +
            "WHERE (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:statusIds IS NULL OR c.status.id IN :statusIds) " +
            "AND (:industryIds IS NULL OR c.industry.id IN :industryIds) " +
            "AND (:cityIds IS NULL OR c.city.id IN :cityIds) " +
            "AND (:companySizeIds IS NULL OR c.companySize.id IN :companySizeIds) " +
            "AND (:createdByIds IS NULL OR c.createdBy.id IN :createdByIds) " +
            "AND (:affectedToIds IS NULL OR c.affectedTo.id IN :affectedToIds) " +
            "AND c.deletedAt IS NULL " +
            "GROUP BY  c.proprietaryStructure.name")
    List<Object[]> getCountOfCustomerByStructure(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statusIds") List<Long> statusIds,
            @Param("industryIds") List<Long> industryIds,
            @Param("cityIds") List<Long> cityIds,
            @Param("companySizeIds") List<Long> companySizeIds,
            @Param("createdByIds") List<Long> createdByIds,
            @Param("affectedToIds") List<Long> affectedToIds);

    /**
     * Get the count of customers per industry
     */
    @Query("SELECT c.industry.name, COUNT(c.id) " +
            "FROM Customer c " +
            "WHERE (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:statusIds IS NULL OR c.status.id IN :statusIds) " +
            "AND (:industryIds IS NULL OR c.industry.id IN :industryIds) " +
            "AND (:cityIds IS NULL OR c.city.id IN :cityIds) " +
            "AND (:companySizeIds IS NULL OR c.companySize.id IN :companySizeIds) " +
            "AND (:createdByIds IS NULL OR c.createdBy.id IN :createdByIds) " +
            "AND (:affectedToIds IS NULL OR c.affectedTo.id IN :affectedToIds) " +
            "AND c.deletedAt IS NULL " +
            "GROUP BY c.industry.name")
    List<Object[]> getCountOfCustomerByIndustry(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statusIds") List<Long> statusIds,
            @Param("industryIds") List<Long> industryIds,
            @Param("cityIds") List<Long> cityIds,
            @Param("companySizeIds") List<Long> companySizeIds,
            @Param("createdByIds") List<Long> createdByIds,
            @Param("affectedToIds") List<Long> affectedToIds);


    Collection<Customer> findByDeletedAtIsNull();

    List<Customer> findAllByDeletedAtIsNullAndCompanyId(Long companiesId);

    @Query("SELECT c FROM Customer c " +
            "LEFT JOIN c.city city " +
            "LEFT JOIN c.company company " +
            "LEFT JOIN c.country country " +
            "LEFT JOIN c.industry industry " +
            "LEFT JOIN c.legalStatus legalStatus " +
            "LEFT JOIN c.companySize companySize " +
            "WHERE LOWER(c.name) LIKE %:keyword% " +
            "OR LOWER(c.sigle) LIKE %:keyword% " +
            "OR LOWER(c.headOffice) LIKE %:keyword% " +
            "OR LOWER(c.legalRepresentative) LIKE %:keyword% " +
            "OR LOWER(c.email) LIKE %:keyword% " +
            "OR LOWER(c.phone) LIKE %:keyword% " +
            "OR LOWER(c.website) LIKE %:keyword% " +
            "OR LOWER(c.linkedin) LIKE %:keyword% " +
            "OR LOWER(c.whatsapp) LIKE %:keyword% " +
            "OR LOWER(c.ice) LIKE %:keyword% " +
            "OR LOWER(c.rc) LIKE %:keyword% " +
            "OR LOWER(c.ifm) LIKE %:keyword% " +
            "OR LOWER(c.patent) LIKE %:keyword% " +
            "OR LOWER(CAST(c.businessDescription AS string)) LIKE %:keyword% " +
            "OR LOWER(city.name) LIKE %:keyword% " +
            "OR LOWER(company.name) LIKE %:keyword% " +
            "OR LOWER(country.name) LIKE %:keyword% " +
            "OR LOWER(industry.name) LIKE %:keyword% " +
            "OR LOWER(legalStatus.name) LIKE %:keyword% " +
            "OR LOWER(companySize.name) LIKE %:keyword% " +
            "AND c.company.id = :companyId")
    List<Customer> searchAllFields(@Param("keyword") String keyword, @Param("companyId") Long companyId);



    List<Customer> findAllByCompanyIdAndStatus(Long companyId, ProspectStatus status);




}

