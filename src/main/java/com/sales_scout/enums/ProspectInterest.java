package com.sales_scout.enums;

public enum ProspectInterest {
    NATIONL_TRASPORT("Trasport national"),
    INNATIONL_TRASPORT5("Trasport Transport International   "),
    STORAG("Entreposage");



    private final String displayName;

    ProspectInterest(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
