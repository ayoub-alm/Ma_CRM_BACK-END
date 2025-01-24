package com.sales_scout.enums.crm.wms;

import lombok.Getter;

@Getter
public enum StorageReasonEnum {
    OVERFLOW("Débord"),
    OUTSOURCING("Externalisation");

    private final String description;

    // Constructor to initialize the description
    StorageReasonEnum(String description) {
        this.description = description;
    }
}