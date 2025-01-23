package com.sales_scout.dto.request.create;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.NeedStatusEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.time.LocalDateTime;

public class StorageNeedCreateDto extends BaseDto {

        @NotNull(message = "Reference UUID is required")
        private UUID ref;

        @NotNull(message = "Storage reason is required")
        private StorageReasonEnum storageReason;

        @NotNull(message = "Need status is required")
        private NeedStatusEnum status;

        @NotNull(message = "Livre status is required")
        private LivreEnum liverStatus;

        @NotNull(message = "Expiration date is required")
        private LocalDateTime expirationDate;

        @NotNull(message = "Duration is required")
        @Min(value = 1, message = "Duration must be at least 1")
        private Long duration;

        @NotNull(message = "Number of SKUs is required")
        @Min(value = 1, message = "Number of SKUs must be at least 1")
        private int numberOfSku;

        @NotBlank(message = "Product type is required")
        private String productType;

        @NotNull(message = "Customer ID is required")
        private Long customerId;

        // Getters and Setters
        public UUID getRef() {
            return ref;
        }

        public void setRef(UUID ref) {
            this.ref = ref;
        }

        public StorageReasonEnum getStorageReason() {
            return storageReason;
        }

        public void setStorageReason(StorageReasonEnum storageReason) {
            this.storageReason = storageReason;
        }

        public NeedStatusEnum getStatus() {
            return status;
        }

        public void setStatus(NeedStatusEnum status) {
            this.status = status;
        }

        public LivreEnum getLiverStatus() {
            return liverStatus;
        }

        public void setLiverStatus(LivreEnum liverStatus) {
            this.liverStatus = liverStatus;
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

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }
    }
