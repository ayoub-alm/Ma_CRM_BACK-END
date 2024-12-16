package com.sales_scout.controller.data;


import com.sales_scout.entity.data.City;
import com.sales_scout.service.data.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing cities.
 */
@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Endpoint to retrieve all cities.
     *
     * @return List of City objects.
     */
    @GetMapping
    public List<City> getAllCities() {
        return cityService.findAll();
    }

    /**
     * Endpoint to retrieve a city by ID.
     *
     * @param id the ID of the city.
     * @return ResponseEntity containing the City if found, otherwise a 404 response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        Optional<City> city = cityService.findById(id);
        return city.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to create a new city.
     *
     * @param city the City to create.
     * @return ResponseEntity containing the created City and a 201 status.
     */
    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody City city) {
        City savedCity = cityService.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCity);
    }

    /**
     * Endpoint to update an existing city.
     *
     * @param id the ID of the city to update.
     * @param cityDetails the new details for the city.
     * @return ResponseEntity containing the updated City or a 404 response if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City cityDetails) {
        try {
            City updatedCity = cityService.update(id, cityDetails);
            return ResponseEntity.ok(updatedCity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to delete a city.
     *
     * @param id the ID of the city to delete.
     * @return ResponseEntity with no content if deleted, otherwise a 404 response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        if (cityService.findById(id).isPresent()) {
            cityService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
