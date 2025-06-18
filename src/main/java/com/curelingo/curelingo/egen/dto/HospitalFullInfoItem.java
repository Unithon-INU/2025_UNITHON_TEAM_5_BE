package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class HospitalFullInfoItem {
    private String hpid;          // 기관 ID
    private String dutyName;      // 기관명
    private String dutyAddr;      // 주소
    private String dutyDivNam;    // 병원분류명
    private String dutyEryn;      // 응급실 운영 여부(1, 2)
    private String dutyTel1;      // 대표전화1
    private String dutyTel3;      // 응급실전화
    private String dutyEtc;       // 비고
    private String dutyTime1s;    // 진료시간(월요일) open
    private String dutyTime1c;    // 진료시간(월요일) close
    private String dutyTime2s;    // 진료시간(화요일) open
    private String dutyTime2c;    // 진료시간(화요일) close
    private String dutyTime3s;    // 진료시간(수요일) open
    private String dutyTime3c;    // 진료시간(수요일) close
    private String dutyTime4s;    // 진료시간(목요일) open
    private String dutyTime4c;    // 진료시간(목요일) close
    private String dutyTime5s;    // 진료시간(금요일) open
    private String dutyTime5c;    // 진아료시간(금요일) close
    private String dutyTime6s;    // 진료시간(토요일) open
    private String dutyTime6c;    // 진료시간(토요일) close
    private String dutyTime7s;    // 진료시간(일요일) open
    private String dutyTime7c;    // 진료시간(일요일) close
    private String dutyTime8s;    // 진료시간(공휴일) open
    private String dutyTime8c;    // 진료시간(공휴일) close
    private Double wgs84Lat;      // 병원 위도
    private Double wgs84Lon;      // 병원 경도
    private String rnum;          // 순서
}
