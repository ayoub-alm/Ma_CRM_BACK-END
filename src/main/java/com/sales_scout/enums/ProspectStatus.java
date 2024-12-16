package com.sales_scout.enums;

public enum ProspectStatus {
    NEW("Nouvelle"),               // New
    QUALIFIED("Qualifiée"),        // Qualified
    INTERESTED("Intéressée"),      // Interested
    OPPORTUNITY("Opportunité"),    // Opportunity
    CONVERTED("Convertie"),        // Converted
    DISQUALIFIED("Disqualifiée"),  // Disqualified
    LOST("Perdue"),                // Lost
    NRP("NRP");                    // No Response/No Reply

    private final String label;

    ProspectStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

