package com.sales_scout.service;

import com.sales_scout.entity.Right;
import com.sales_scout.repository.RightRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RightService {

    private final RightRepository rightRepository;

    public RightService(RightRepository rightRepository) {
        this.rightRepository = rightRepository;
    }


    public List<Right> getAllRight() {
        return rightRepository.findAll();
    }

    
    public Right getRightById(Long id) {
        Optional<Right> right = rightRepository.findById(id);
        return right.orElse(null); // return null or throw exception if not found
    }

    
    public Right createRight(Right right) {
        return rightRepository.save(right); // Save the right entity to the database
    }

    
    public Right updateRight(Long id, Right right) {
        // Check if the right exists
        if (rightRepository.existsById(id)) {
            right.setId(id); // Set the ID for the updated right entity
            return rightRepository.save(right);
        }
        return null; // Return null or throw exception if not found
    }

    
    public void deleteRight(Long id) {
        // You can implement soft delete logic here, or just delete the entity.
        if (rightRepository.existsById(id)) {
            rightRepository.deleteById(id); // Delete the right by ID
        }
    }
}
