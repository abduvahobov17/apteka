package com.asgardiateam.aptekaproject.common.deserializer;

import com.asgardiateam.aptekaproject.enums.BucketStatus;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static java.util.Optional.ofNullable;

public class BucketStatusDeserializer extends JsonDeserializer<BucketStatus> {
    @Override
    public BucketStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return ofNullable(jsonParser.getText())
                .map(BucketStatus::tryFindByName)
                .orElseThrow(AptekaException::bucketStatusNotValid);
    }
}
