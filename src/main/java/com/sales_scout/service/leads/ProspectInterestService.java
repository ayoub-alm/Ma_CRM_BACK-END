package com.sales_scout.service.leads;

import com.sales_scout.dto.request.ProspectInterestRequestDto;
import com.sales_scout.dto.response.ProspectInterestResponseDto;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.CustomerInterest;
import com.sales_scout.mapper.ProspectInterestDtoBuilder;
import com.sales_scout.repository.InterestRepository;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.repository.leads.CustomerInterestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProspectInterestService {
    private final CustomerInterestRepository prospectInterestRepository;
    private final InterestRepository interestRepository;
    private final CustomerRepository customerRepository;
    private final CustomerInterestRepository customerInterestRepository;
    public ProspectInterestService(CustomerInterestRepository prospectInterestRepository, InterestRepository interestRepository, CustomerRepository customerRepository, CustomerInterestRepository customerInterestRepository) {
        this.prospectInterestRepository = prospectInterestRepository;
        this.interestRepository = interestRepository;
        this.customerRepository = customerRepository;
        this.customerInterestRepository = customerInterestRepository;
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
        Customer customer = customerRepository.findByDeletedAtIsNullAndId(prospectInterestDetails.getProspectId())
                .orElseThrow(() -> new RuntimeException("Prospect with id " + prospectInterestDetails.getProspectId() + " not found or is deleted"));

        // Find the matching ProspectInterest
        Optional<CustomerInterest> matchingProspectInterest = customer.getCustomerInterests()
                .stream()
                .filter(prospectInterest -> prospectInterest.getInterest().getId().equals(prospectInterestDetails.getInterestId()))
                .findFirst();

        if (matchingProspectInterest.isPresent()) {
            // Update the existing ProspectInterest
            CustomerInterest prospectInterest = matchingProspectInterest.get();
            prospectInterest.setStatus(prospectInterestDetails.getStatus());
            customerInterestRepository.save(prospectInterest);
            customerRepository.save(customer);
            return ProspectInterestDtoBuilder.fromEntity(prospectInterest);
        } else {
            // Create a new ProspectInterest if it doesn't exist
            Interest newInterest = this.interestRepository.findById(prospectInterestDetails.getInterestId())
                    .orElseThrow(() -> new EntityNotFoundException("Interest with id " + prospectInterestDetails.getInterestId() + " not found"));
            CustomerInterest newProspectInterest = CustomerInterest.builder()
                    .interest(newInterest)
                    .name(newInterest.getName())
                    .customer(customer) // Use the existing Prospect entity
                    .status(prospectInterestDetails.getStatus())
                    .build();

            // Save the new ProspectInterest and return it as a DTO
            return ProspectInterestDtoBuilder.fromEntity(prospectInterestRepository.save(newProspectInterest));
        }
    }
}
