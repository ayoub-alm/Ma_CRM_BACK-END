package com.sales_scout.service.data;

import com.sales_scout.entity.data.ProprietaryStructure;
import com.sales_scout.repository.data.ProprietaryStructureRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProprietaryStructureService {
    private final ProprietaryStructureRepository proprietaryStructureRepository;

    public ProprietaryStructureService(ProprietaryStructureRepository proprietaryStructureRepository) {
        this.proprietaryStructureRepository = proprietaryStructureRepository;
    }

    /**
     * Retrieves all proprietary structures.
     *
     * @return List of ProprietaryStructure
     */
    public List<ProprietaryStructure> getAllProprietaryStructures() {
        return proprietaryStructureRepository.findAll();
    }

    /**
     * Retrieves a proprietary structure by its ID.
     *
     * @param id the ID of the proprietary structure
     * @return Optional of ProprietaryStructure
     */
    public Optional<ProprietaryStructure> getProprietaryStructureById(Long id) {
        return proprietaryStructureRepository.findById(id);
    }

    /**
     * Adds a new proprietary structure.
     *
     * @param proprietaryStructure the proprietary structure to add
     * @return the added ProprietaryStructure
     */
    public ProprietaryStructure addProprietaryStructure(ProprietaryStructure proprietaryStructure) {
        proprietaryStructure.setActive(true);
        return proprietaryStructureRepository.save(proprietaryStructure);
    }

    /**
     * Updates an existing proprietary structure.
     *
     * @param id the ID of the proprietary structure to update
     * @param proprietaryStructure the updated proprietary structure
     * @return the updated ProprietaryStructure
     */
    public ProprietaryStructure updateProprietaryStructure(Long id, ProprietaryStructure proprietaryStructure) {
        if (!proprietaryStructureRepository.existsById(id)) {
            throw new EntityNotFoundException("ProprietaryStructure with id " + id + " not found.");
        }
        proprietaryStructure.setId(id); // Set the ID for the update
        return proprietaryStructureRepository.save(proprietaryStructure);
    }

    /**
     * Deletes a proprietary structure by its ID.
     *
     * @param id the ID of the proprietary structure to delete
     */
    public void deleteProprietaryStructure(Long id) {
        if (!proprietaryStructureRepository.existsById(id)) {
            throw new EntityNotFoundException("ProprietaryStructure with id " + id + " not found.");
        }
        proprietaryStructureRepository.deleteById(id);
    }
}
