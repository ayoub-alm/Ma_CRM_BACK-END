package com.sales_scout.service.leads;

import com.sales_scout.dto.request.ProspectInterestRequestDto;
import com.sales_scout.dto.response.ProspectInterestResponseDto;
import com.sales_scout.entity.leads.Prospect;
import com.sales_scout.entity.leads.ProspectInterest;
import com.sales_scout.mapper.ProspectInterestDtoBuilder;
import com.sales_scout.repository.leads.ProspectInterestRepository;
import com.sales_scout.repository.leads.ProspectRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProspectInterestService {
    private final ProspectInterestRepository prospectInterestRepository;
    private final ProspectRepository prospectRepository;

    public ProspectInterestService(ProspectInterestRepository prospectInterestRepository, ProspectRepository prospectRepository) {
        this.prospectInterestRepository = prospectInterestRepository;
        this.prospectRepository = prospectRepository;
    }

    public ProspectInterestResponseDto updateProspectInterest(ProspectInterestRequestDto prospectInterestDetails){
        Optional<Prospect> existingProspect =  prospectRepository.findByDeletedAtIsNullAndId(prospectInterestDetails.getProspectId());
       if (existingProspect.isPresent()){
           Prospect prospect = existingProspect.get();
           Optional<ProspectInterest> matchingProspectInterest = prospect
                   .getProspectInterests()
                   .stream()
                   .filter(prospectInterest -> prospectInterest.getInterest().getId().equals(prospectInterestDetails.getInterestId()) )
                   .findFirst();
           if (matchingProspectInterest.isPresent()){
               ProspectInterest prospectInterest = matchingProspectInterest.get();
               prospectInterest.setStatus(prospectInterestDetails.getStatus());
               prospectRepository.save(prospect);
               return ProspectInterestDtoBuilder.fromEntity(prospectInterest);
           } else {
               throw new RuntimeException("ProspectInterest with interestId " + prospectInterestDetails.getInterestId() + " not found for prospect " + prospectInterestDetails.getProspectId());
           } }else {
           throw new RuntimeException("Prospect with id " + prospectInterestDetails.getProspectId() + " not found or is deleted");
       }
    }
}
