package com.sales_scout.enums;

public enum ActiveInactiveEnum {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String displayName;

    ActiveInactiveEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
