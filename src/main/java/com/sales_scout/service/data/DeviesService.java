package com.sales_scout.service.data;


import com.sales_scout.entity.data.Devise;
import com.sales_scout.repository.data.DeviseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviesService {
    private final DeviseRepository deviseRepository;

    public DeviesService(DeviseRepository deviseRepository) {
        this.deviseRepository = deviseRepository;
    }

  
    public List<Devise> findAll() {
        return deviseRepository.findAll();
    }

  
    public Devise findById(Long id) {
        return deviseRepository.findById(id).orElse(null);
    }

  
    public Devise save(Devise devise) {
        return deviseRepository.save(devise);
    }

  
    public void deleteById(Long id) {
        deviseRepository.deleteById(id);
    }
}
