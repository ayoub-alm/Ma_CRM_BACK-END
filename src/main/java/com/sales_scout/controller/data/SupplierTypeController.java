package com.sales_scout.controller.data;


import com.sales_scout.entity.data.SupplierType;
import com.sales_scout.service.data.SupplierTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/supplier-types")
public class SupplierTypeController {

    private final SupplierTypeService supplierTypeService;

    @Autowired
    public SupplierTypeController(SupplierTypeService supplierTypeService) {
        this.supplierTypeService = supplierTypeService;
    }

    @GetMapping
    public List<SupplierType> getAllSupplierTypes() {
        return supplierTypeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierType> getSupplierTypeById(@PathVariable Long id) {
        Optional<SupplierType> supplierType = supplierTypeService.findById(id);
        return supplierType.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SupplierType> createSupplierType(@RequestBody SupplierType supplierType) {
        SupplierType savedSupplierType = supplierTypeService.save(supplierType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplierType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierType> updateSupplierType(@PathVariable Long id, @RequestBody SupplierType supplierTypeDetails) {
        try {
            SupplierType updatedSupplierType = supplierTypeService.update(id, supplierTypeDetails);
            return ResponseEntity.ok(updatedSupplierType);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierType(@PathVariable Long id) {
        if (supplierTypeService.findById(id).isPresent()) {
            supplierTypeService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
