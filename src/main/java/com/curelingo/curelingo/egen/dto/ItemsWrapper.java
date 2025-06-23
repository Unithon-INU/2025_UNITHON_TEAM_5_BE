package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        private static final Logger log = LoggerFactory.getLogger(SingleOrListDeserializer.class);

        @Override
        public List<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            // Logging before reading node
            JsonNode node = p.readValueAsTree();
            log.info("[Deserializer] Raw JSON node: {}", node.toPrettyString());

            JavaType contextualType = ctxt.getContextualType();
            log.info("[Deserializer] ContextualType: {}", contextualType);

            // The node may be an object with "item" or just an array/object directly
            JsonNode itemNode = node.get("item");
            log.info("[Deserializer] itemNode: {}", itemNode);

            ObjectCodec codec = p.getCodec();
            JavaType valueType = (contextualType != null && contextualType.containedTypeCount() > 0)
                    ? contextualType.containedType(0)
                    : ctxt.constructType(Object.class);

            List<Object> result = new ArrayList<>();
            ObjectMapper mapper = (codec instanceof ObjectMapper) ? (ObjectMapper) codec : new ObjectMapper();

            if (itemNode != null) {
                if (itemNode.isArray()) {
                    for (JsonNode e : itemNode) {
                        Object parsed = mapper.treeToValue(e, valueType.getRawClass());
                        log.info("[Deserializer] Parsed item: {}", parsed);
                        result.add(parsed);
                    }
                } else {
                    Object parsed = mapper.treeToValue(itemNode, valueType.getRawClass());
                    log.info("[Deserializer] Parsed single item: {}", parsed);
                    result.add(parsed);
                }
            } else if (node.isArray()) {
                for (JsonNode e : node) {
                    Object parsed = mapper.treeToValue(e, valueType.getRawClass());
                    log.info("[Deserializer] Parsed item: {}", parsed);
                    result.add(parsed);
                }
            } else if (node.isObject()) {
                Object parsed = mapper.treeToValue(node, valueType.getRawClass());
                log.info("[Deserializer] Parsed single item: {}", parsed);
                result.add(parsed);
            }

            log.info("[Deserializer] Final result list size: {}", result.size());
            return result;
        }
    }
}
