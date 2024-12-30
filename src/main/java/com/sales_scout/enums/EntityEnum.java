package com.sales_scout.enums;

import lombok.Getter;

@Getter
public enum EntityEnum {
    PROSPECT("PROSPECT"),
    INTERACTION("INTERACTION"),
    INTERLOCUTOR("INTERLOCUTOR"),
    INCLASSED("IN CLASSED");

    private final String value;

    EntityEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}