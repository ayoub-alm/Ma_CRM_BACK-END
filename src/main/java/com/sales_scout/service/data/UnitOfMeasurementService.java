package com.sales_scout.service.data;


import com.sales_scout.entity.data.UnitOfMeasurement;
import com.sales_scout.repository.data.UnitOfMeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing unit of measurements.
 */
@Service
public class UnitOfMeasurementService {

    private final UnitOfMeasurementRepository unitOfMeasurementRepository;


    public UnitOfMeasurementService(UnitOfMeasurementRepository unitOfMeasurementRepository) {
        this.unitOfMeasurementRepository = unitOfMeasurementRepository;
    }

    /**
     * Retrieves all unit of measurements.
     *
     * @return List of UnitOfMeasurement objects.
     */
    public List<UnitOfMeasurement> findAll() {
        return unitOfMeasurementRepository.findAll();
    }

    /**
     * Finds a unit of measurement by its ID.
     *
     * @param id the ID of the unit of measurement.
     * @return Optional containing the UnitOfMeasurement if found, otherwise empty.
     */
    public Optional<UnitOfMeasurement> findById(Long id) {
        return unitOfMeasurementRepository.findById(id);
    }

    /**
     * Saves a new unit of measurement.
     *
     * @param unitOfMeasurement the UnitOfMeasurement to save.
     * @return the saved UnitOfMeasurement.
     */
    public UnitOfMeasurement save(UnitOfMeasurement unitOfMeasurement) {
        return unitOfMeasurementRepository.save(unitOfMeasurement);
    }

    /**
     * Updates an existing unit of measurement.
     *
     * @param id the ID of the unit of measurement to update.
     * @param unitOfMeasurementDetails the new details for the unit of measurement.
     * @return the updated UnitOfMeasurement.
     * @throws RuntimeException if the unit of measurement with the given ID is not found.
     */
    public UnitOfMeasurement update(Long id, UnitOfMeasurement unitOfMeasurementDetails) {
        UnitOfMeasurement unitOfMeasurement = unitOfMeasurementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UnitOfMeasurement not found with id " + id));
        unitOfMeasurement.setName(unitOfMeasurementDetails.getName());
        return unitOfMeasurementRepository.save(unitOfMeasurement);
    }

    /**
     * Deletes a unit of measurement by its ID.
     *
     * @param id the ID of the unit of measurement to delete.
     */
    public void deleteById(Long id) {
        unitOfMeasurementRepository.deleteById(id);
    }
}
