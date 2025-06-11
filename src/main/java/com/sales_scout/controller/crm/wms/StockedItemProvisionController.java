package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.response.crm.wms.StockedItemProvisionResponseDto;
import com.sales_scout.entity.crm.wms.StockedItemProvision;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.StockedItemProvisionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocked-item-provisions")
public class StockedItemProvisionController {
    private final StockedItemProvisionService service;

    public StockedItemProvisionController(StockedItemProvisionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<StockedItemProvision>> getAll() {
        return ResponseEntity.ok(service.getAllStockedItemProvisions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockedItemProvision> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStockedItemProvisionById(id));
    }

    @PostMapping
    public ResponseEntity<StockedItemProvision> create(@RequestBody StockedItemProvision provision) {
        return ResponseEntity.ok(service.createStockedItemProvision(provision));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockedItemProvisionResponseDto> update(@PathVariable Long id, @RequestBody StockedItemProvision provision)
    throws ResourceNotFoundException
    {
        try {
            return ResponseEntity.ok(service.updateStockedItemProvision(id, provision));
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage(),"Stocked item not found","");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteStockedItemProvision(id);
        return ResponseEntity.noContent().build();
    }
}
