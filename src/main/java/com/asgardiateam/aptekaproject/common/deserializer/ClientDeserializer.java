package com.asgardiateam.aptekaproject.common.deserializer;

import com.asgardiateam.aptekaproject.enums.ClientType;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static java.util.Optional.ofNullable;

public class ClientDeserializer extends JsonDeserializer<ClientType> {

    @Override
    public ClientType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return ofNullable(jsonParser.getText())
                .map(ClientType::tryFindClientType)
                .orElseThrow(AptekaException::clientTypeNotValid);
    }

}
