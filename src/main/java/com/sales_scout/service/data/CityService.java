package com.sales_scout.service.data;


import com.sales_scout.entity.data.City;
import com.sales_scout.repository.data.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing cities.
 */
@Service
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    /**
     * Retrieves all cities.
     *
     * @return List of City objects.
     */
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    /**
     * Finds a city by its ID.
     *
     * @param id the ID of the city.
     * @return Optional containing the City if found, otherwise empty.
     */
    public Optional<City> findById(Long id) {
        return cityRepository.findById(id);
    }

    /**
     * Saves a new city.
     *
     * @param city the City to save.
     * @return the saved City.
     */
    public City save(City city) {
        return cityRepository.save(city);
    }

    /**
     * Updates an existing city.
     *
     * @param id the ID of the city to update.
     * @param cityDetails the new details for the city.
     * @return the updated City.
     * @throws RuntimeException if the city with the given ID is not found.
     */
    public City update(Long id, City cityDetails) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found with id " + id));
        city.setName(cityDetails.getName());
        return cityRepository.save(city);
    }

    /**
     * Deletes a city by its ID.
     *
     * @param id the ID of the city to delete.
     */
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }

    public Optional<City> findByNameContainingIgnoreCase(String name){
        return  this.cityRepository.findByNameContainingIgnoreCase(name).stream().findFirst();
    }
}
