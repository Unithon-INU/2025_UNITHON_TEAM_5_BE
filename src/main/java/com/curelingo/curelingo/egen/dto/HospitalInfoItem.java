package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class HospitalInfoItem {
    private String dutyName;
    private String dutyTel3;
    private String hpid;
    private String hv5;
    private String hvamyn;
    private String hvmriayn;
    private String hvctayn;
    private String hvidate;
}
