package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ItemsWrapperDeserializer extends JsonDeserializer<ItemsWrapper<?>> {
    @Override
    public ItemsWrapper<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 빈 문자열이면 null 반환
        if (p.currentToken() == JsonToken.VALUE_STRING && "".equals(p.getValueAsString())) {
            return null;
        }
        // 아니면 원래대로 deserialize
        return p.readValueAs(ItemsWrapper.class);
    }
}
