package com.sales_scout.enums.crm;

public enum DiscountTypeEnum {
    NOTAPPLICABLE("NOTAPPLICABLE"),
    RATE("RATE"),
    VALUE("VALUE");

    private final String displayName;
    DiscountTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
