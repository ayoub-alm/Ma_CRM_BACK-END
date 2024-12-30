package com.sales_scout.enums.crm.wms;

import lombok.Getter;

@Getter
public enum NeedStatusEnum {
    CREATION("Creation"),
    PROPOSAL("Proposal"),
    NEGOTIATION("Negotiation"),
    WON("Won"),
    LOST("Lost"),
    CANCELED("Canceled");

    private final String status;

    // Constructor to initialize the status
    NeedStatusEnum(String status) {
        this.status = status;
    }
}
