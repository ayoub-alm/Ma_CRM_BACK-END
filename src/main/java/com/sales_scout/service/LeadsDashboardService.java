package com.sales_scout.service;

import com.sales_scout.dto.response.ProspectResponseDto;
import com.sales_scout.dto.response.leads_dashboard.ProspectCountDto;
import com.sales_scout.enums.ProspectStatus;
import com.sales_scout.repository.InteractionRepository;
import com.sales_scout.repository.InterlocutorRepository;
import com.sales_scout.repository.ProspectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    public List<ProspectCountDto> getCountOfProspectsPerStatus() {
        List<Object[]> results = prospectRepository.getCountOfProspectsByStatusBetweenOptionalDates(null, null);

        return results.stream()
                .map(row -> new ProspectCountDto ( ProspectStatus.valueOf(row[0].toString()), ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

}
