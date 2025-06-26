package com.curelingo.curelingo.clinic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clinic {
    private String hpid;         // 병원 고유 ID
    private String name;         // 병원명
    private String nameEn;       // 병원명 (영문)
    private String tel;          // 전화번호
    private String addr;         // 주소
    private String addrEn;       // 주소 (영문)
    private double lat;          // 위도
    private double lng;          // 경도
    private String h3Cell;       // H3 셀 주소
    private String type;         // 병원 종류 (예: 병원, 의원, 치과 등)
    private List<String> departments; // 진료과목 목록 (D001~D032)
} 