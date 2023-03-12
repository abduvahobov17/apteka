package com.asgardiateam.aptekaproject.common.deserializer;

import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static java.util.Optional.ofNullable;

public class BotStateDeserializer extends JsonDeserializer<BotState> {
    @Override
    public BotState deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return ofNullable(jsonParser.getText())
                .map(BotState::tryFindBotState)
                .orElseThrow(AptekaException::botStateNotValid);
    }
}
