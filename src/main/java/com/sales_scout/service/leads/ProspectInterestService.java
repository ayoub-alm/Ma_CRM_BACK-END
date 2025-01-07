package com.sales_scout.service.leads;

import com.sales_scout.dto.request.ProspectInterestRequestDto;
import com.sales_scout.dto.response.ProspectInterestResponseDto;
import com.sales_scout.entity.leads.ProspectInterest;
import com.sales_scout.mapper.ProspectInterestDtoBuilder;
import com.sales_scout.repository.leads.ProspectInterestRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProspectInterestService {
    private final ProspectInterestRepository prospectInterestRepository;

    public ProspectInterestService(ProspectInterestRepository prospectInterestRepository) {
        this.prospectInterestRepository = prospectInterestRepository;
    }

    public ProspectInterestResponseDto updateProspectInterest(Long interestId, Long prospectId, ProspectInterestRequestDto prospectInterestDetails){
        Optional<ProspectInterest> existingProspectInterest = prospectInterestRepository.findByDeletedAtIsNullAndInterestIdAndProspectId(interestId,prospectId);
        if(existingProspectInterest.isEmpty()){
            throw new RuntimeException("ProspectInterest n'existe pas ou est supprimée");
        }
        ProspectInterest prospectInterest = existingProspectInterest.get();
        prospectInterest.setStatus(prospectInterestDetails.getStatus());
        ProspectInterest updateProspectInterest = prospectInterestRepository.save(prospectInterest);
        return ProspectInterestDtoBuilder.fromEntity(updateProspectInterest);
    }
}
