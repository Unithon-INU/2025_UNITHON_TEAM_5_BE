package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AvailableBedsItem {
    private String dutyName;
    private String dutyTel3;
    private String hpid;
    private String hvec;
    private String hvoc;
    private String hvicc;
    private String hvgc;
    private String hvventiayn;
    private String hvventisoayn;
    private String hvidate;
}
