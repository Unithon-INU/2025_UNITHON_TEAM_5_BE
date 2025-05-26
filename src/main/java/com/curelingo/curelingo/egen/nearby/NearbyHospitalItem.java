package com.curelingo.curelingo.egen.nearby;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbyHospitalItem {
    private String hpid;
    private String dutyDiv;
    private String dutyDivName;
    private String dutyName;
    private String dutyAddr;
    private String dutyTel1;
    private double latitude;
    private double longitude;
    private String startTime;
    private String endTime;
    private double distance;
}
