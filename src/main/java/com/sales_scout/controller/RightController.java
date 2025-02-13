package com.sales_scout.controller;


import com.sales_scout.dto.request.RightsRequestDto;
import com.sales_scout.dto.response.RightsResponseDto;
import com.sales_scout.entity.Right;
import com.sales_scout.exception.DataAlreadyExistsException;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.exception.DuplicateKeyValueException;
import com.sales_scout.service.RightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rights")
public class RightController {

    private final RightService rightsService;

    public RightController(RightService rightsService) {
        this.rightsService = rightsService;
    }

    /**
     * Get all rights.
     * @return List of rights.
     */
    @GetMapping
    public ResponseEntity<List<RightsResponseDto>> getAllRights() throws DataNotFoundException{
        try {
            List<RightsResponseDto> rights = rightsService.getAllRight();
            return ResponseEntity.ok(rights);
        }catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    /**
     * Get right by ID.
     * @param id Right ID.
     * @return Right entity.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RightsResponseDto> getRightsById(@PathVariable Long id) throws DataNotFoundException {
        try {
            RightsResponseDto rights = rightsService.getRightById(id);
            return ResponseEntity.ok(rights);
        }catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    /**
     * Create a new right.
     * @param rightsRequestDto New right data.
     * @return Created right.
     */
    @PostMapping
    public ResponseEntity<RightsResponseDto> createRights(@RequestBody RightsRequestDto rightsRequestDto)throws DataAlreadyExistsException {
        try{
            RightsResponseDto createdRights = rightsService.createRight(rightsRequestDto);
            return ResponseEntity.ok(createdRights);
        }catch (DataAlreadyExistsException e){
            throw new DataAlreadyExistsException(e.getMessage(), e.getCode());
        }
    }

    /**
     * Update an existing right.
     * @param id Right ID to update.
     * @param rightsRequestDto Updated right data.
     * @return Updated right entity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RightsResponseDto> updateRights(@PathVariable Long id, @RequestBody RightsRequestDto rightsRequestDto) throws DataNotFoundException{
        try{
            RightsResponseDto updatedRights = rightsService.updateRight(id, rightsRequestDto);
            return ResponseEntity.ok(updatedRights);
        }catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage(), e.getCode());
        }
    }

    /**
     * Soft delete a right.
     * @param id Right ID to delete.
     * @return Success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRights(@PathVariable Long id) throws DataNotFoundException {
       try{
           return ResponseEntity.ok(rightsService.deleteRight(id));
       }catch (DataNotFoundException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
       }

    }
}
