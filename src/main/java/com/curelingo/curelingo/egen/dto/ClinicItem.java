package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class ClinicItem {
    private String hpid;
    private String dutyName;
    private String dutyNameEn;
    private String dutyAddr;
    private String dutyAddrEn;
    private String dutyDivNam;
    private String dgidIdNam; // 진료과
    private String dutyEryn;
    private String dutyTel1;
    private String dutyTel3;
    private String dutyEtc;
    private String dutyTime1s;
    private String dutyTime1c;
    private String dutyTime2s;
    private String dutyTime2c;
    private String dutyTime3s;
    private String dutyTime3c;
    private String dutyTime4s;
    private String dutyTime4c;
    private String dutyTime5s;
    private String dutyTime5c;
    private String dutyTime6s;
    private String dutyTime6c;
    private String dutyTime7s;
    private String dutyTime7c;
    private String dutyTime8s;
    private String dutyTime8c;
    private Double wgs84Lat;
    private Double wgs84Lon;
    private String rnum;
}
