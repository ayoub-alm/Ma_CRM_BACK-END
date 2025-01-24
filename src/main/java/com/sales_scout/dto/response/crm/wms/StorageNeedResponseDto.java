package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.dto.BaseDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class StorageNeedResponseDto extends BaseDto {
    private Long id;
    private UUID ref;
    private String liverStatus;  // LivreEnum as String
    private String storageReason; // StorageReasonEnum as String
    private String status; // NeedStatusEnum as String
    private LocalDateTime expirationDate;
    private Long duration;
    private int numberOfSku;
    private String productType;
    private CustomerDto customer; // Nested Customer DTO

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRef() {
        return ref;
    }

    public void setRef(UUID ref) {
        this.ref = ref;
    }

    public String getLiverStatus() {
        return liverStatus;
    }

    public void setLiverStatus(String liverStatus) {
        this.liverStatus = liverStatus;
    }

    public String getStorageReason() {
        return storageReason;
    }

    public void setStorageReason(String storageReason) {
        this.storageReason = storageReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getNumberOfSku() {
        return numberOfSku;
    }

    public void setNumberOfSku(int numberOfSku) {
        this.numberOfSku = numberOfSku;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }
}
