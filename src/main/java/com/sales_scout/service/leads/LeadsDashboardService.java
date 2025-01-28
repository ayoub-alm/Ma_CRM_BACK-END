package com.sales_scout.service.leads;

import com.sales_scout.dto.response.leads_dashboard.CountsDto;
import com.sales_scout.dto.response.leads_dashboard.ProspectCountDto;
import com.sales_scout.enums.ProspectStatus;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.repository.leads.InteractionRepository;
import com.sales_scout.repository.leads.InterlocutorRepository;

import org.springframework.stereotype.Service;

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
    public List<ProspectCountDto> getCountOfProspectsPerStatus() {
        List<Object[]> results = customerRepository.getCountOfCustomersByStatusBetweenOptionalDates(null, null);

        return results.stream()
                .map(row -> new ProspectCountDto ( ProspectStatus.valueOf(row[0].toString()), ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

}
