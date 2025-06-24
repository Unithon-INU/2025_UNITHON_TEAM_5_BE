package com.curelingo.curelingo.mongodb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospitalRequestDto {
    private String hpid;
    private String dutyName;      // 한국어 병원명
    private String dutyAddr;      // 한국어 주소
    private String dutyDivNam;
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

    /**
     * HospitalRequestDto를 HospitalDto로 변환
     */
    public HospitalDto toHospitalDto() {
        return HospitalDto.builder()
                .hpid(this.hpid)
                .dutyName(this.dutyName)
                .dutyAddr(this.dutyAddr)
                .dutyDivNam(this.dutyDivNam)
                .dutyEryn(this.dutyEryn)
                .dutyTel1(this.dutyTel1)
                .dutyTel3(this.dutyTel3)
                .dutyEtc(this.dutyEtc)
                .dutyTime1s(this.dutyTime1s)
                .dutyTime1c(this.dutyTime1c)
                .dutyTime2s(this.dutyTime2s)
                .dutyTime2c(this.dutyTime2c)
                .dutyTime3s(this.dutyTime3s)
                .dutyTime3c(this.dutyTime3c)
                .dutyTime4s(this.dutyTime4s)
                .dutyTime4c(this.dutyTime4c)
                .dutyTime5s(this.dutyTime5s)
                .dutyTime5c(this.dutyTime5c)
                .dutyTime6s(this.dutyTime6s)
                .dutyTime6c(this.dutyTime6c)
                .dutyTime7s(this.dutyTime7s)
                .dutyTime7c(this.dutyTime7c)
                .dutyTime8s(this.dutyTime8s)
                .dutyTime8c(this.dutyTime8c)
                .wgs84Lat(this.wgs84Lat)
                .wgs84Lon(this.wgs84Lon)
                .rnum(this.rnum)
                .build();
    }
} 