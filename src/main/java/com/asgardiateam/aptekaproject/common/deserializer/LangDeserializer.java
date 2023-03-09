package com.asgardiateam.aptekaproject.common.deserializer;

import com.asgardiateam.aptekaproject.enums.Lang;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static java.util.Optional.ofNullable;

public class LangDeserializer extends JsonDeserializer<Lang> {
    @Override
    public Lang deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return ofNullable(jsonParser.getText())
                .map(Lang::tryFindLangByText)
                .orElseThrow(AptekaException::langNotValid);
    }
}
