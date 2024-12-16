package com.sales_scout.service.data;


import com.sales_scout.entity.data.OnlinePaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/online-payment-methods")
public class OnlinePaymentMethodController {
    private final OnlinePaymentMethodService onlinePaymentMethodService;

    @Autowired
    public OnlinePaymentMethodController(OnlinePaymentMethodService onlinePaymentMethodService) {
        this.onlinePaymentMethodService = onlinePaymentMethodService;
    }

    @GetMapping
    public ResponseEntity<List<OnlinePaymentMethod>> getAllOnlinePaymentMethods() {
        List<OnlinePaymentMethod> onlinePaymentMethods = onlinePaymentMethodService.getAllOnlinePaymentMethods();
        return ResponseEntity.ok(onlinePaymentMethods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnlinePaymentMethod> getOnlinePaymentMethodById(@PathVariable Long id) {
        return onlinePaymentMethodService.getOnlinePaymentMethodById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OnlinePaymentMethod> createOnlinePaymentMethod(@RequestBody OnlinePaymentMethod onlinePaymentMethod) {
        OnlinePaymentMethod createdOnlinePaymentMethod = onlinePaymentMethodService.saveOnlinePaymentMethod(onlinePaymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOnlinePaymentMethod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OnlinePaymentMethod> updateOnlinePaymentMethod(@PathVariable Long id, @RequestBody OnlinePaymentMethod onlinePaymentMethod) {
        onlinePaymentMethod.setId(id); // Ensure ID matches
        OnlinePaymentMethod updatedOnlinePaymentMethod = onlinePaymentMethodService.updateOnlinePaymentMethod(onlinePaymentMethod);
        return ResponseEntity.ok(updatedOnlinePaymentMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOnlinePaymentMethod(@PathVariable Long id) {
        onlinePaymentMethodService.deleteOnlinePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }
}
