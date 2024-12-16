package com.sales_scout.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum InteractionSubject {
    QUALIFICATION("Qualification"),
    INFORMATION_REQUEST("Demande d'information"),
    LOYALTY("Fidélisation"),
    PROSPECTING("Prospection"),
    COMPLAINT("Réclamation"),
    COLLECTION_FOLLOWUP("Suivi de recouvrement"),
    OFFER_FOLLOWUP("Suivi des offres");

    private final String description;

    // A map to link description to the enum constants
    private static final Map<String, InteractionSubject> DESCRIPTION_MAP = new HashMap<>();

    static {
        for (InteractionSubject subject : InteractionSubject.values()) {
            DESCRIPTION_MAP.put(subject.getDescription(), subject);
        }
    }

    InteractionSubject(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static InteractionSubject fromDescription(String description) {
        InteractionSubject subject = DESCRIPTION_MAP.get(description);
        if (subject == null) {
            throw new IllegalArgumentException(
                    "Unknown value for enum InteractionSubject: " + description
            );
        }
        return subject;
    }
}
