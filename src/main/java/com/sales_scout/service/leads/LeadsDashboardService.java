package com.sales_scout.service.leads;

import com.sales_scout.dto.EntityFilters.CustomerFilerFields;
import com.sales_scout.dto.response.InterestResponseDto;
import com.sales_scout.dto.response.leads_dashboard.*;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.data.City;
import com.sales_scout.entity.data.Industry;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import com.sales_scout.enums.ProspectStatus;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.mapper.InterestDtoBuilder;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.repository.leads.InteractionRepository;
import com.sales_scout.repository.leads.InterlocutorRepository;

import com.sales_scout.specification.CustomerSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadsDashboardService {
    private final CustomerRepository customerRepository;
    private final InterlocutorRepository interlocutorRepository;
    private final InteractionRepository interactionRepository;


    public LeadsDashboardService(CustomerRepository customerRepository, InterlocutorRepository interlocutorRepository, InteractionRepository interactionRepository) {
        this.customerRepository = customerRepository;
        this.interlocutorRepository = interlocutorRepository;
        this.interactionRepository = interactionRepository;
    }

    /**
     * This function allows to get count of prospects
     * @return {Long} count of prospects
     */
    public CountsDto getCountOfProspects(){
        Long countOfInterlocutors = (long) this.interlocutorRepository.findByDeletedAtIsNull().size();
        Long countOfProspects =  (long) this.customerRepository.findByDeletedAtIsNull().size();
        Long countOfInteractions = (long) this.interactionRepository.findByDeletedAtIsNull().size();

        return  CountsDto.builder()
                .countOfProspects(countOfProspects)
                .CountOfInteractions(countOfInteractions)
                .CountOfInterlocutors(countOfInterlocutors)
                .build();
    }

    /**
     * This function allows to get count of prospects by status
     * @return List<ProspectCountDto> status , count
     */


    public List<DashboardCountDto> getCustomerCountByStatus(CustomerFilerFields filters, boolean useOrCondition) {
        List<Object[]> results = customerRepository.getCountOfCustomersByStatusBetweenOptionalDates(
                filters.getCreatedAtStart(),
                filters.getCreatedAtEnd(),
                filters.getStatusIds(),
                filters.getIndustryIds(),
                filters.getCityIds(),
                filters.getCompanySizeIds(),
                filters.getCreatedByIds(),
                filters.getAffectedToIds()
        );

        return results.stream()
                .map(row -> new DashboardCountDto(row[0].toString(), ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    /**
     * get Count of Customer By Seller
     * @return {List<CustomerCountDto>}
     * @throws {DataNotFoundException}
     */
    public List<DashboardCountDto> getCountOfCustomerPerSeller(CustomerFilerFields filters, boolean useOrCondition)throws DataNotFoundException {
        List<Object[]> results = customerRepository.getCountOfCustomerBySeller( filters.getCreatedAtStart(),
                filters.getCreatedAtEnd(),
                filters.getStatusIds(),
                filters.getIndustryIds(),
                filters.getCityIds(),
                filters.getCompanySizeIds(),
                filters.getCreatedByIds(),
                filters.getAffectedToIds());
        if (!results.isEmpty()){
            return results.stream()
                    .map(row -> new DashboardCountDto( row[0].toString(), ((Number) row[1]).longValue()))
                    .collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data of Customer Not Found : Count Customer by Seller Not Found",750L);
        }
    }


    /**
     * get Count of Customer By City
     * @return {List<CustomerCountDto>}
     * @throws {DataNotFoundException}
     */
    public List<DashboardCountDto> getCountOfCustomerPerCity(CustomerFilerFields filters,boolean useOrCondition) throws DataNotFoundException {
        List<Object[]> results = customerRepository.getCountOfCustomerByCity(filters.getCreatedAtStart(),
                filters.getCreatedAtEnd(),
                filters.getStatusIds(),
                filters.getIndustryIds(),
                filters.getCityIds(),
                filters.getCompanySizeIds(),
                filters.getCreatedByIds(),
                filters.getAffectedToIds());
        if (!results.isEmpty()){
            return results.stream()
                    .map(row -> new DashboardCountDto(row[0].toString(),((Number) row[1]).longValue()))
                    .collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data of Customer Not Found : Count Customer by City Not Found",750L);
        }
    }


    /**
     * get Count of Customer By Interest
     * @return {List<CustomerCountDto>}
     * @throws {DataNotFoundException}
     */
    public List<CustomerCountDto> getCountOfCustomerByInterest()throws DataNotFoundException{
        List<Object[]> results = customerRepository.getCountOfCustomerByInterest();
        if (!results.isEmpty()){
            return results.stream()
                    .map(row->
                            new CustomerCountDto(null,(InterestResponseDto) InterestDtoBuilder.fromEntity((Interest) row[0]),null,null,null,null,(Long) row[1]))
                    .collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data of Customer Not Found : Count Customer by Interest Not Found",950L);
        }
    }

    /**
     * get Count of Customer By Date
     * @return {List<CustomerCountDto>}
     * @throws {DataNotFoundException}
     */
    public List<DashboardCountDto> getCountOfCustomerPerDate(CustomerFilerFields filters, boolean useOrCondition) throws DataNotFoundException {
        List<Object[]> results = customerRepository.getCountOfCustomerByDate(
                filters.getCreatedAtStart(),
                filters.getCreatedAtEnd(),
                filters.getStatusIds(),
                filters.getIndustryIds(),
                filters.getCityIds(),
                filters.getCompanySizeIds(),
                filters.getCreatedByIds(),
                filters.getAffectedToIds()
        );

        if (!results.isEmpty()) {
            return results.stream()
                    .map(row -> new DashboardCountDto(row[0].toString(), ((Number) row[1]).longValue()))
                    .collect(Collectors.toList());
        } else {
            throw new DataNotFoundException("Data of Customer Not Found: Count Customer by Date Not Found", 750L);
        }
    }


    /**
     * Get count of customers per status with optional filters
     */
    public List<DashboardCountDto> getCountOfCustomerPerStatus(CustomerFilerFields filters, boolean useOrCondition) throws DataNotFoundException {
        Specification<Customer> specification = CustomerSpecification.hasCustomerFilter(filters, useOrCondition);

        List<DashboardCountDto> results = customerRepository.findAll(specification)
                .stream()
                .collect(Collectors.groupingBy(Customer::getStatus, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new DashboardCountDto(entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            throw new DataNotFoundException("No Customer Data Found for Status Count", 750L);
        }

        return results;
    }

    public List<DashboardCountDto> getCountOfCustomerPerStructure(CustomerFilerFields filters, boolean useOrCondition) throws DataNotFoundException {
        List<Object[]> results = customerRepository.getCountOfCustomerByStructure(filters.getCreatedAtStart(),
                filters.getCreatedAtEnd(),
                filters.getStatusIds(),
                filters.getIndustryIds(),
                filters.getCityIds(),
                filters.getCompanySizeIds(),
                filters.getCreatedByIds(),
                filters.getAffectedToIds());
        if (!results.isEmpty()){
            return results.stream()
                    .map(row -> new DashboardCountDto( (row[0]).toString(),((Number) row[1]).longValue()))
                    .collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data of Customer Not Found : Count Customer by Date Not Found",750L);
        }
    }

    /**
     * Get count of customers per industry with optional filters
     */
    public List<DashboardCountDto> getCountOfCustomerPerIndustry(CustomerFilerFields filters, boolean useOrCondition) throws DataNotFoundException {
        List<Object[]> results = customerRepository.getCountOfCustomerByIndustry(filters.getCreatedAtStart(),
                filters.getCreatedAtEnd(),
                filters.getStatusIds(),
                filters.getIndustryIds(),
                filters.getCityIds(),
                filters.getCompanySizeIds(),
                filters.getCreatedByIds(),
                filters.getAffectedToIds());
        if (!results.isEmpty()) {
            return results.stream().map(row-> new DashboardCountDto((String) row[0] , (Long) row[1])).collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data Of Interlocutor Not Found : Count Interlocutor by Seller Not Found", 454L);
        }
    }


    /**
     * get Count of Interlocutor By Seller
     * @return {List<InterlocutorCountDto>}
     * @throws {DataNotFoundException}
     */
    public List<InterlocutorCountDto> getCountOfInterlocutorBySeller()throws DataNotFoundException {
        List<Object[]> results = interlocutorRepository.getCountOfInterlocutorPerSeller();
        if (!results.isEmpty()) {
            return results.stream().map(row-> new InterlocutorCountDto((String) row[0], null , (Long) row[1])).collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data Of Interlocutor Not Found : Count Interlocutor by Seller Not Found", 454L);
        }
    }

    /**
     * get Count of Interaction By Seller
     * @return {List<InteractionCountDto>}
     * @throws {DataNotFoundException}
     */
    public List<InteractionCountDto> getCountOfInteractionBySeller()throws DataNotFoundException{
        List<Object[]> results = interactionRepository.getCountOfInteractionPerSeller();
        if (!results.isEmpty()){
            return results.stream()
                    .map(row->
                            new InteractionCountDto(null,null,(String) row[0] , (Long) row[1]))
                    .collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data of Interaction Not Found : Count Interaction by Seller Not Found",950L);
        }
    }

    /**
     * get Count of Interaction By Subject
     * @return {List<InteractionCountDto>}
     * @throws {DataNotFoundException}
     */
    public List<InteractionCountDto> getCountOfInteractionBySubject() throws DataNotFoundException {
        List<Object[]> results = interactionRepository.getCountOfInteractionBySubject();
        if (!results.isEmpty()){
            return results.stream()
                    .map(row->
                            new InteractionCountDto((InteractionSubject.valueOf(row[0].toString())),null , null , (Long) row[1]))
                    .collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data Of Interaction Not Found ", 222L);
        }

    }

    /**
     * get Count of Interaction By Type
     * @return {List<InteractionCountDto>}
     * @throws {DataNotFoundException}
     */
    public List<InteractionCountDto> getCountOfInteractionByType()throws DataNotFoundException{
        List<Object[]> results = interactionRepository.getCountOfInteractionByType();
        if (!results.isEmpty()){
            return results.stream().map(row ->
                    new InteractionCountDto(null ,(InteractionType.valueOf(row[0].toString())), null , (Long) row[1])).collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data of Interaction Not Found : Count Interaction by Type Not Found",900L);
        }
    }


}
