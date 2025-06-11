package com.sales_scout.dto.response.crm.wms;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StorageOfferPaymentMethodDto {
    private Long id;
    private String name;
    private boolean selected = true;
}
