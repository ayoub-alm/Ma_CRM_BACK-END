package com.sales_scout.mapper;

import com.sales_scout.dto.request.create.wms.StorageNeedCreateDto;
import com.sales_scout.dto.response.crm.wms.CustomerDto;
import com.sales_scout.dto.response.crm.wms.StorageNeedResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.Customer;
import com.sales_scout.entity.crm.wms.StorageNeed;
import com.sales_scout.repository.crm.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StorageNeedMapper {

    private final CustomerRepository customerRepository;

    public StorageNeedMapper(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Maps a CreateStorageNeedDto to a StorageNeed entity.
     *
     * @param dto The CreateStorageNeedDto containing the input data.
     * @return The StorageNeed entity.
     */
    public StorageNeed toEntity(StorageNeedCreateDto dto) {
        if (dto == null) {
            return null;
        }

        StorageNeed storageNeed = new StorageNeed();
        storageNeed.setRef(dto.getRef());
        storageNeed.setStorageReason(dto.getStorageReason());
        storageNeed.setStatus(dto.getStatus());
        storageNeed.setLiverStatus(dto.getLiverStatus());
        storageNeed.setExpirationDate(dto.getExpirationDate());
        storageNeed.setDuration(dto.getDuration());
        storageNeed.setNumberOfSku(dto.getNumberOfSku());
        storageNeed.setProductType(dto.getProductType());

        // Set the customer entity by ID (assumes a customer service or repository fetch)
        Optional<Customer> customer = customerRepository.findById(dto.getCustomerId());
        if(customer.isPresent()){
            storageNeed.setCustomer(customer.get());
        }
        else {
            Customer customer2 = Customer.builder().id(dto.getCustomerId()).build();
            storageNeed.setCustomer(customer2);
        }
        Company company = new Company();
        company.setId(dto.getCompanyId());
        storageNeed.setCompany(company);
        return storageNeed;
    }


    public static StorageNeedResponseDto toResponseDto(StorageNeed storageNeed) {
        if (storageNeed == null) {
            return null;
        }

        StorageNeedResponseDto dto = new StorageNeedResponseDto();
        dto.setId(storageNeed.getId());
        dto.setRef(storageNeed.getRef());
        dto.setLiverStatus(storageNeed.getLiverStatus().name()); // Convert Enum to String
        dto.setStorageReason(storageNeed.getStorageReason().name()); // Convert Enum to String
        dto.setStatus(storageNeed.getStatus().name()); // Convert Enum to String
        dto.setExpirationDate(storageNeed.getExpirationDate());
        dto.setDuration(storageNeed.getDuration());
        dto.setNumberOfSku(storageNeed.getNumberOfSku());
        dto.setProductType(storageNeed.getProductType());

        // Map nested customer details
        if (storageNeed.getCustomer() != null) {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setId(storageNeed.getCustomer().getId());
            customerDto.setName(storageNeed.getCustomer().getName());
            dto.setCustomer(customerDto);
        }

        return dto;
    }
}
