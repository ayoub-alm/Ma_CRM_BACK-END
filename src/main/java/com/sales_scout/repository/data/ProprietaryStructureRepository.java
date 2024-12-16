package com.sales_scout.repository.data;


import com.sales_scout.entity.data.ProprietaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.SequencedCollection;

@Repository
public interface ProprietaryStructureRepository extends JpaRepository<ProprietaryStructure, Long> {
    SequencedCollection<ProprietaryStructure> findByNameContainingIgnoreCase(String name);
    // Additional query methods can be defined here if needed
}
