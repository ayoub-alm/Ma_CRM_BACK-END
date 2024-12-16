package com.sales_scout.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum InteractionType {
    PHONE_CALL("Appel téléphonique"),
    EMAIL("E-mail"),
    VIDEO_CHAT("Chat vidéo"),
    IN_PERSON("En personne"),
    LINKEDIN_MESSAGE("Message Linkedin"),
    WHATSAPP_MESSAGE("Message WhatsApp"),
    ADD_OPTION("Ajouter une option");

    private final String description;

    // Static map for description-to-enum lookup
    private static final Map<String, InteractionType> DESCRIPTION_MAP = new HashMap<>();

    static {
        for (InteractionType type : InteractionType.values()) {
            DESCRIPTION_MAP.put(type.getDescription(), type);
        }
    }

    InteractionType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static InteractionType fromDescription(String description) {
        InteractionType type = DESCRIPTION_MAP.get(description);
        if (type == null) {
            throw new IllegalArgumentException("Unknown value for InteractionType: " + description);
        }
        return type;
    }
}
