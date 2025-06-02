package com.curelingo.curelingo.egen.dto;

import lombok.Data;

@Data
public class EgenResponse<T> {
    private EgenHeader header;
    private EgenBody<T> body;
}
