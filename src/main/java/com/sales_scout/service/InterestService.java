package com.sales_scout.service;

import com.sales_scout.dto.request.InterestRequestDto;
import com.sales_scout.dto.response.InterestResponseDto;
import com.sales_scout.dto.response.ProspectResponseDto;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.leads.Prospect;
import com.sales_scout.entity.leads.ProspectInterest;
import com.sales_scout.mapper.InterestDtoBuilder;
import com.sales_scout.repository.InterestRepository;
import com.sales_scout.repository.leads.ProspectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterestService {

    private  final InterestRepository interestRepository;
    private final ProspectRepository prospectRepository;

    public InterestService(InterestRepository interestRepository, ProspectRepository prospectRepository) {
        this.interestRepository = interestRepository;
        this.prospectRepository = prospectRepository;
    }

    /**
     * This function allows to get companies that are not soft deleted
     * @return {List<InterestResponseDto>} the list of Interest
     */
    public List<InterestResponseDto> getAllInterestByProspectId(Long prospectId) throws EntityNotFoundException {
        // Récupérer le prospect par son ID
        Prospect prospect = this.prospectRepository.findById(prospectId)
                .orElseThrow(() -> new EntityNotFoundException("Prospect not found with id: " + prospectId));

        // Récupérer les intérêts associés au prospect via la table de jointure ProspectInterest
        List<Interest> interests = prospect.getProspectInterests().stream()
                .map(ProspectInterest::getInterest)
                .toList();

        // Convertir les entités Interest en DTOs
        return interests.stream()
                .map(InterestDtoBuilder::fromEntity)
                .collect(Collectors.toList());
    }

    public InterestResponseDto updateInterest(Long id , InterestRequestDto interestDetails){
       Optional<Interest> existingInterest= interestRepository.findByDeletedAtIsNullAndId(id);
       if(existingInterest.isEmpty()){
           throw  new RuntimeException("Interest n'existe pas ou est supprimée");
       }
       Interest interest = existingInterest.get();

       Interest updateInterest = interestRepository.save(interest);
       return InterestDtoBuilder.fromEntity(updateInterest);
   }


}
