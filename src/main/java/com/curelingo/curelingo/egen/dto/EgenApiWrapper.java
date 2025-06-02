package com.curelingo.curelingo.egen.dto;

import lombok.Data;

@Data
public class EgenApiWrapper<T> {
    private EgenResponse<T> response;
}
