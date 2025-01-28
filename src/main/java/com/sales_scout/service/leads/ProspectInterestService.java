package com.sales_scout.service.leads;

import com.sales_scout.dto.request.ProspectInterestRequestDto;
import com.sales_scout.dto.response.ProspectInterestResponseDto;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.ProspectInterest;
import com.sales_scout.mapper.ProspectInterestDtoBuilder;
import com.sales_scout.repository.InterestRepository;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.repository.leads.ProspectInterestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProspectInterestService {
    private final ProspectInterestRepository prospectInterestRepository;
    private final InterestRepository interestRepository;
    private final CustomerRepository customerRepository;
    public ProspectInterestService(ProspectInterestRepository prospectInterestRepository, InterestRepository interestRepository, CustomerRepository customerRepository) {
        this.prospectInterestRepository = prospectInterestRepository;
        this.interestRepository = interestRepository;
        this.customerRepository = customerRepository;
    }


    /**
     * Updates the status of a ProspectInterest or creates a new one if it doesn't exist.
     *
     * @param prospectInterestDetails The DTO containing the update details.
     * @return The updated or newly created ProspectInterest as a DTO.
     * @throws RuntimeException If the Prospect is not found or deleted.
     */
    public ProspectInterestResponseDto updateProspectInterest(ProspectInterestRequestDto prospectInterestDetails) throws RuntimeException {
        // Fetch the Prospect entity or throw an exception if not found
        Customer prospect = customerRepository.findByDeletedAtIsNullAndId(prospectInterestDetails.getProspectId())
                .orElseThrow(() -> new RuntimeException("Prospect with id " + prospectInterestDetails.getProspectId() + " not found or is deleted"));

        // Find the matching ProspectInterest
        Optional<ProspectInterest> matchingProspectInterest = prospect.getProspectInterests()
                .stream()
                .filter(prospectInterest -> prospectInterest.getInterest().getId().equals(prospectInterestDetails.getInterestId()))
                .findFirst();

        if (matchingProspectInterest.isPresent()) {
            // Update the existing ProspectInterest
            ProspectInterest prospectInterest = matchingProspectInterest.get();
            prospectInterest.setStatus(prospectInterestDetails.getStatus());
            customerRepository.save(prospect); // Save the updated Prospect entity
            return ProspectInterestDtoBuilder.fromEntity(prospectInterest);
        } else {
            // Create a new ProspectInterest if it doesn't exist

            Interest newInterest = this.interestRepository.findById(prospectInterestDetails.getInterestId())
                    .orElseThrow(() -> new EntityNotFoundException("Interest with id " + prospectInterestDetails.getInterestId() + " not found"));
            ProspectInterest newProspectInterest = ProspectInterest.builder()
                    .interest(newInterest)
                    .name(newInterest.getName())
                    .customer(prospect) // Use the existing Prospect entity
                    .status(prospectInterestDetails.getStatus())
                    .build();

            // Save the new ProspectInterest and return it as a DTO
            return ProspectInterestDtoBuilder.fromEntity(prospectInterestRepository.save(newProspectInterest));
        }
    }
}
