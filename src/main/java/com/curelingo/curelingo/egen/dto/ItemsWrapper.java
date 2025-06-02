package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ItemsWrapper<T> {
    @JsonProperty("item")
    @JsonDeserialize(using = SingleOrListDeserializer.class)
    private List<T> item;

    public static class SingleOrListDeserializer extends JsonDeserializer<List<?>> {
        @Override
        public List<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectCodec codec = p.getCodec();
            JsonNode node = codec.readTree(p);
            List<Object> result = new ArrayList<>();
            JavaType javaType = ctxt.getContextualType();
            JavaType valueType = javaType != null && javaType.containedTypeCount() > 0 ? javaType.containedType(0) : ctxt.constructType(Object.class);

            if (node.isArray()) {
                for (JsonNode e : node) {
                    result.add(codec.treeToValue(e, valueType.getRawClass()));
                }
            } else if (node.isObject()) {
                result.add(codec.treeToValue(node, valueType.getRawClass()));
            }
            return result;
        }
    }
}
