package com.curelingo.curelingo.egen.dto;

import lombok.Data;

import java.util.List;

@Data
public class EgenBody<T> {
    private ItemsWrapper<T> items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
