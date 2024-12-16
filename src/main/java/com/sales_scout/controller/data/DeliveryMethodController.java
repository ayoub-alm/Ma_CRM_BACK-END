package com.sales_scout.controller.data;


import com.sales_scout.entity.data.DeliveryMethod;
import com.sales_scout.service.data.DeliveryMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-methods")
public class DeliveryMethodController {

    private final DeliveryMethodService deliveryMethodService;

    @Autowired
    public DeliveryMethodController(DeliveryMethodService deliveryMethodService) {
        this.deliveryMethodService = deliveryMethodService;
    }

    /**
     * Get all delivery methods
     * @return List of DeliveryMethod
     */
    @GetMapping
    public ResponseEntity<List<DeliveryMethod>> getAllDeliveryMethods() {
        List<DeliveryMethod> deliveryMethods = deliveryMethodService.getAllDeliveryMethods();
        return ResponseEntity.ok(deliveryMethods);
    }

    /**
     * Get delivery method by ID
     * @param id Long
     * @return DeliveryMethod
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryMethod> getDeliveryMethodById(@PathVariable Long id) {
        return deliveryMethodService.getDeliveryMethodById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new delivery method
     * @param deliveryMethod DeliveryMethod
     * @return Created DeliveryMethod
     */
    @PostMapping
    public ResponseEntity<DeliveryMethod> createDeliveryMethod(@RequestBody DeliveryMethod deliveryMethod) {
        DeliveryMethod createdDeliveryMethod = deliveryMethodService.saveDeliveryMethod(deliveryMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDeliveryMethod);
    }

    /**
     * Update an existing delivery method
     * @param id Long
     * @param deliveryMethod DeliveryMethod
     * @return Updated DeliveryMethod
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryMethod> updateDeliveryMethod(@PathVariable Long id, @RequestBody DeliveryMethod deliveryMethod) {
        // Assuming the deliveryMethod object has the correct ID
        deliveryMethod.setId(id); // Ensure ID matches
        DeliveryMethod updatedDeliveryMethod = deliveryMethodService.updateDeliveryMethod(deliveryMethod);
        return ResponseEntity.ok(updatedDeliveryMethod);
    }

    /**
     * Delete a delivery method
     * @param id Long
     * @return No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryMethod(@PathVariable Long id) {
        deliveryMethodService.deleteDeliveryMethod(id);
        return ResponseEntity.noContent().build();
    }
}