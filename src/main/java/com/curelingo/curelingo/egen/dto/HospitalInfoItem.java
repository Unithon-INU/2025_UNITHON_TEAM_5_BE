package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class HospitalInfoItem {
    private String dgidIdName;
    private String dutyAddr;
    private String dutyEryn;
    private String dutyHano;
    private String dutyHayn;
    private String dutyMapimg;
    private String dutyName;
    private String dutyTel1;
    private String dutyTel3;
    private String dutyTime1c;
    private String dutyTime1s;
    private String dutyTime2c;
    private String dutyTime2s;
    private String dutyTime3c;
    private String dutyTime3s;
    private String dutyTime4c;
    private String dutyTime4s;
    private String dutyTime5c;
    private String dutyTime5s;
    private String dutyTime6c;
    private String dutyTime6s;
    private String hpbdn;
    private String hperyn;
    private String hpgryn;
    private String hpid;
    private String hpopyn;
    private String hvec;
    private String hvgc;
    private String hvicc;
    private String hvoc;
    private String MKioskTy10;
    private String MKioskTy11;
    private String MKioskTy7;
    private String MKioskTy8;
    private String MKioskTy9;
    private String postCdn1;
    private String postCdn2;
    private String wgs84Lat;
    private String wgs84Lon;
}
