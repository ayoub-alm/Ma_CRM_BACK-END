package com.sales_scout.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.sales_scout.enums.ActiveInactiveEnum;

import java.io.IOException;

public class ActiveInactiveEnumDeserializer extends JsonDeserializer<ActiveInactiveEnum> {

    @Override
    public ActiveInactiveEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase(); // Convert input to uppercase for matching
        try {
            return ActiveInactiveEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid value for ActiveInactiveEnum: " + value);
        }
    }
}
