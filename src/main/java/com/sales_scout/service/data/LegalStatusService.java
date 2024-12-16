package com.sales_scout.service.data;


import com.sales_scout.entity.data.LegalStatus;
import com.sales_scout.repository.data.LegalStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LegalStatusService {
    private final LegalStatusRepository legalStatusRepository;

    public LegalStatusService(LegalStatusRepository legalStatusRepository) {
        this.legalStatusRepository = legalStatusRepository;
    }

    public List<LegalStatus> getAllLegalStatuses() {
        return legalStatusRepository.findAll();
    }

    public Optional<LegalStatus> getLegalStatusById(Long id) {
        return legalStatusRepository.findById(id);
    }

    public LegalStatus saveLegalStatus(LegalStatus legalStatus) {
        return legalStatusRepository.save(legalStatus);
    }

    public void deleteLegalStatus(Long id) {
        legalStatusRepository.deleteById(id);
    }

    public LegalStatus updateLegalStatus(LegalStatus legalStatus) {
        return legalStatusRepository.save(legalStatus);
    }
}
