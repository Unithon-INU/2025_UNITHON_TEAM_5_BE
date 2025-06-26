package com.curelingo.curelingo.clinic.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalDetailDto {
    private String hpid;                // 병원 고유 ID
    private String dutyName;            // 병원명 (한국어)
    private String dutyNameEn;          // 병원명 (영어)
    private String dutyAddr;            // 주소 (한국어)
    private String dutyAddrEn;          // 주소 (영어)
    private String dutyDivNam;          // 병원분류명
    private List<String> departments;   // 진료과목 목록
    private String dutyEryn;            // 응급실운영여부
    private String dutyTel1;            // 대표전화
    private String dutyTel3;            // 응급실전화
    private String dutyEtc;             // 비고
    
    // 진료시간 (월~일, 공휴일)
    private String dutyTime1s;          // 월요일 시작
    private String dutyTime1c;          // 월요일 종료
    private String dutyTime2s;          // 화요일 시작
    private String dutyTime2c;          // 화요일 종료
    private String dutyTime3s;          // 수요일 시작
    private String dutyTime3c;          // 수요일 종료
    private String dutyTime4s;          // 목요일 시작
    private String dutyTime4c;          // 목요일 종료
    private String dutyTime5s;          // 금요일 시작
    private String dutyTime5c;          // 금요일 종료
    private String dutyTime6s;          // 토요일 시작
    private String dutyTime6c;          // 토요일 종료
    private String dutyTime7s;          // 일요일 시작
    private String dutyTime7c;          // 일요일 종료
    private String dutyTime8s;          // 공휴일 시작
    private String dutyTime8c;          // 공휴일 종료
    
    // 위치 정보
    private Double wgs84Lat;            // 위도
    private Double wgs84Lon;            // 경도
    private String rnum;                // 일련번호
} 