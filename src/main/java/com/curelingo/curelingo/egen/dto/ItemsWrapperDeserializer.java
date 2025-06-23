package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.util.List;

public class ItemsWrapperDeserializer extends JsonDeserializer<ItemsWrapper<?>> implements ContextualDeserializer {

    private JavaType itemType;

    public ItemsWrapperDeserializer() {
        this.itemType = null;
    }

    public ItemsWrapperDeserializer(JavaType itemType) {
        this.itemType = itemType;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        JavaType wrapperType = ctxt.getContextualType();
        JavaType itemType = null;
        if (wrapperType != null && wrapperType.containedTypeCount() > 0) {
            JavaType maybeT = wrapperType.containedType(0);
            if (maybeT != null && maybeT.containedTypeCount() > 0) {
                itemType = maybeT.containedType(0);
            } else {
                itemType = maybeT;
            }
        }
        if (itemType == null) {
            itemType = ctxt.constructType(Object.class);
        }
        return new ItemsWrapperDeserializer(itemType);
    }

    @Override
    public ItemsWrapper<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        ItemsWrapper<Object> wrapper = new ItemsWrapper<>();
        JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, itemType);
        List<Object> items = new java.util.ArrayList<>();
        JsonNode itemsNode = node.get("item");
        if (itemsNode != null) {
            if (itemsNode.isArray()) {
                items = mapper.convertValue(itemsNode, listType);
            } else if (itemsNode.isObject()) {
                Object obj = mapper.convertValue(itemsNode, itemType);
                items.add(obj);
            }
            // if itemsNode is null or not array/object: keep empty
        } else if (node.isArray()) {
            items = mapper.convertValue(node, listType);
        } else if (node.isObject()) {
            Object obj = mapper.convertValue(node, itemType);
            items.add(obj);
        }
        wrapper.setItem(items);
        return wrapper;
    }
}