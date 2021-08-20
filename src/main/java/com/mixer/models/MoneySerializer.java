package com.mixer.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneySerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(final BigDecimal value, final JsonGenerator jgen,
                          final SerializerProvider provider) throws IOException {
        jgen.writeString(value.toString());
    }
}
