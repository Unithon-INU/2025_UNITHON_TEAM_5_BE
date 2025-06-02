package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NearbyHospitalItem {
    private String dutyName;
    private String dutyTel3;
    private String hpid;
    private String distance;
}
