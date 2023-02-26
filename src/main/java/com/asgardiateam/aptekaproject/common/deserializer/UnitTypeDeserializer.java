package com.asgardiateam.aptekaproject.common.deserializer;

import com.asgardiateam.aptekaproject.enums.UnitType;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Optional;

public class UnitTypeDeserializer extends JsonDeserializer<UnitType> {

    @Override
    public UnitType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return Optional.of(jsonParser.getText())
                .map(UnitType::tryFindUnitType)
                .orElseThrow(AptekaException::unitTypeNotValid);
    }
}
