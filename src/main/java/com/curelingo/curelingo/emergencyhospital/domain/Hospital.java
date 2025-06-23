package com.curelingo.curelingo.emergencyhospital.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital {
    private String hpid;       // 병원 고유 ID
    private String name;       // 병원명
    private String tel;        // 전화번호
    private String addr;       // 주소
    private double lat;        // 위도
    private double lng;        // 경도
    private String h3Cell;     // H3 셀 주소
}