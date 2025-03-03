package com.sales_scout.service;

import com.sales_scout.dto.request.InterestRequestDto;
import com.sales_scout.dto.response.InterestResponseDto;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.CustomerInterest;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.mapper.InterestDtoBuilder;
import com.sales_scout.repository.InterestRepository;
import com.sales_scout.repository.leads.CustomerInterestRepository;
import com.sales_scout.repository.leads.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterestService {

    private  final InterestRepository interestRepository;
    private final CustomerRepository customerRepository;
    private final CustomerInterestRepository customerInterestRepository;
    public InterestService(InterestRepository interestRepository, CustomerRepository customerRepository, CustomerInterestRepository customerInterestRepository) {
        this.interestRepository = interestRepository;
        this.customerRepository = customerRepository;
        this.customerInterestRepository = customerInterestRepository;
    }

    /**
     * This function allows to get companies that are not soft deleted
     * @return {List<InterestResponseDto>} the list of Interest
     */
    public List<InterestResponseDto> getAllInterestByCutomerId(Long CustomerId) throws EntityNotFoundException {
        // Récupérer le prospect par son ID
        Customer customer = this.customerRepository.findById(CustomerId)
                .orElseThrow(() -> new EntityNotFoundException("Prospect not found with id: " + CustomerId));

        // Récupérer les intérêts associés au prospect via la table de jointure ProspectInterest
        List<Interest> interests = customer.getCustomerInterests().stream()
                .map(CustomerInterest::getInterest)
                .toList();

        // Convertir les entités Interest en DTOs
        return interests.stream()
                .map(InterestDtoBuilder::fromEntity)
                .collect(Collectors.toList());
    }


    /**
     *
     * @param companyId
     * @return
     */
    public List<InterestResponseDto> getAllInterestByCompanyId(Long companyId) {
        return this.interestRepository.findAllByCompanyIdAndDeletedAtIsNull(companyId)
                .stream()
                .map(InterestDtoBuilder::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Adds an interest to a customer.
     *
     * @param customerId The ID of the customer.
     * @param interestId The ID of the interest to add.
     * @throws DataNotFoundException if the customer or interest is not found.
     */
    @Transactional
    public void addInterestToCustomer(Long customerId, Long interestId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new DataNotFoundException("Customer not found", 404L));

        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new DataNotFoundException("Interest not found", 404L));

        // Check if interest already exists for the customer
        boolean exists = customerInterestRepository.existsByCustomerAndInterest(customer, interest);
        if (exists) {
            throw new IllegalArgumentException("Interest is already assigned to the customer.");
        }

        // Create and save the new CustomerInterest relationship
        CustomerInterest customerInterest = CustomerInterest.builder()
                .customer(customer)
                .interest(interest)
                .name(interest.getName()) // Assigning interest name
                .status(true)
                .build();

        customerInterestRepository.save(customerInterest);
    }

    /**
     * Removes an interest from a customer.
     *
     * @param customerId The ID of the customer.
     * @param interestId The ID of the interest to remove.
     * @throws DataNotFoundException if the customer, interest, or relationship is not found.
     */
    @Transactional
    public void removeInterestFromCustomer(Long customerId, Long interestId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new DataNotFoundException("Customer not found", 404L));

        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new DataNotFoundException("Interest not found", 404L));

        Optional<CustomerInterest> customerInterest = customerInterestRepository.findByCustomerAndInterest(customer, interest);

        if (customerInterest.isPresent()) {
            customerInterestRepository.delete(customerInterest.get());
        } else {
            throw new DataNotFoundException("Customer does not have the specified interest.", 404L);
        }
    }
}
