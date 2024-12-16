package com.sales_scout.service.data;


import com.sales_scout.entity.data.Court;
import com.sales_scout.repository.data.CourtRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtService {

    private final CourtRepository courtRepository;


    public CourtService(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    /**
     * This function allows to all courts
     * @return {List<Court>} list of courts
     */
    public List<Court> findAll() {
        return courtRepository.findAll();
    }

    /**
     * this function allows to find a court by id
     * @param {id} the ID of the court
     * @return {Court}
     */
    public Optional<Court> findById(Long id) {
        return courtRepository.findById(id);
    }
    /**
     * this function allows to create new court
     * @param court the court to create
     */
    public Court save(Court court) {
        return courtRepository.save(court);
    }

    /**
     * This function allows to delete court by id
     * @param id
     */
    public void deleteById(Long id) {
        courtRepository.deleteById(id);
    }
}