package com.sales_scout.enums.crm.wms;

import lombok.Getter;

@Getter
public enum LivreEnum {
    OPEN("OPEN"),
    CLOSE("CLOSE");

    private final String status;

    // Constructor to initialize the status
    LivreEnum(String status) {
        this.status = status;
    }
}
