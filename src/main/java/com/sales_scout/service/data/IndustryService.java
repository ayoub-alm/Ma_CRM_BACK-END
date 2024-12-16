package com.sales_scout.service.data;


import com.sales_scout.entity.data.Industry;
import com.sales_scout.repository.data.IndustryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IndustryService {
    private final IndustryRepository industryRepository;
    public IndustryService(IndustryRepository industryRepository){
        this.industryRepository = industryRepository;
    }


    // Get all industries
    public List<Industry> findAll() {
        return industryRepository.findAll();
    }

    // Get industry by ID
    public Optional<Industry> findById(Long id) {
        return industryRepository.findById(id);
    }

    // Create a new industry
    public Industry save(Industry industry) {
        return industryRepository.save(industry);
    }

    // Update an existing industry
    public Industry update(Long id, Industry industryDetails) {
        Industry industry = industryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Industry not found with id " + id));

        industry.setName(industryDetails.getName());
        return industryRepository.save(industry);
    }

    // Delete an industry
    public void deleteById(Long id) {
        industryRepository.deleteById(id);
    }
}

