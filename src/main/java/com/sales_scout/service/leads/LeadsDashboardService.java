package com.sales_scout.service.leads;

import com.sales_scout.dto.response.leads_dashboard.CountsDto;
import com.sales_scout.dto.response.leads_dashboard.ProspectCountDto;
import com.sales_scout.enums.ProspectStatus;
import com.sales_scout.repository.leads.InteractionRepository;
import com.sales_scout.repository.leads.InterlocutorRepository;
import com.sales_scout.repository.leads.ProspectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadsDashboardService {
    private final ProspectRepository prospectRepository;
    private final InterlocutorRepository interlocutorRepository;
    private final InteractionRepository interactionRepository;


    public LeadsDashboardService(ProspectRepository prospectRepository, InterlocutorRepository interlocutorRepository, InteractionRepository interactionRepository) {
        this.prospectRepository = prospectRepository;
        this.interlocutorRepository = interlocutorRepository;
        this.interactionRepository = interactionRepository;
    }

    /**
     * This function allows to get count of prospects
     * @return {Long} count of prospects
     */
    public CountsDto getCountOfProspects(){
        Long countOfInterlocutors = (long) this.interlocutorRepository.findByDeletedAtIsNull().size();
        Long countOfProspects =  (long) this.prospectRepository.findByDeletedAtIsNull().size();
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
        List<Object[]> results = prospectRepository.getCountOfProspectsByStatusBetweenOptionalDates(null, null);

        return results.stream()
                .map(row -> new ProspectCountDto ( ProspectStatus.valueOf(row[0].toString()), ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

}
