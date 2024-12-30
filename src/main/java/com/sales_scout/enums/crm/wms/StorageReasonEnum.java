package com.sales_scout.enums.crm.wms;

import lombok.Getter;

@Getter
public enum StorageReasonEnum {
    TEMPORARY("Temporary storage for short term use"),
    PERMANENT("Permanent storage for long term use"),
    SEASONAL("Storage for seasonal items");

    private final String description;

    // Constructor to initialize the description
    StorageReasonEnum(String description) {
        this.description = description;
    }
}