package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
public class EgenBody<T> {
    @JsonDeserialize(using = ItemsWrapperDeserializer.class)
    private ItemsWrapper<T> items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
