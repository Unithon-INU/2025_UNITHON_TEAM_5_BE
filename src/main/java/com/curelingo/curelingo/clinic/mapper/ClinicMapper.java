package com.curelingo.curelingo.clinic.mapper;

import com.curelingo.curelingo.clinic.domain.Clinic;
import com.curelingo.curelingo.mongodb.MongoHospital;

public class ClinicMapper {

    public static Clinic toDomain(MongoHospital mongoHospital) {
        return Clinic.builder()
                .hpid(mongoHospital.getHpid())
                .name(mongoHospital.getDutyName())
                .nameEn(mongoHospital.getDutyNameEn())
                .addr(mongoHospital.getDutyAddr())
                .addrEn(mongoHospital.getDutyAddrEn())
                .tel(mongoHospital.getDutyTel1())
                .lat(mongoHospital.getWgs84Lat())
                .lng(mongoHospital.getWgs84Lon())
                .type(determineClinicType(mongoHospital))
                .departments(mongoHospital.getDepartments())  // 진료과목 목록 추가
                .h3Cell(null)  // H3 셀 정보는 별도 계산 필요
                // 운영시간 필드들 매핑 추가
                .dutyTime1s(mongoHospital.getDutyTime1s())
                .dutyTime1c(mongoHospital.getDutyTime1c())
                .dutyTime2s(mongoHospital.getDutyTime2s())
                .dutyTime2c(mongoHospital.getDutyTime2c())
                .dutyTime3s(mongoHospital.getDutyTime3s())
                .dutyTime3c(mongoHospital.getDutyTime3c())
                .dutyTime4s(mongoHospital.getDutyTime4s())
                .dutyTime4c(mongoHospital.getDutyTime4c())
                .dutyTime5s(mongoHospital.getDutyTime5s())
                .dutyTime5c(mongoHospital.getDutyTime5c())
                .dutyTime6s(mongoHospital.getDutyTime6s())
                .dutyTime6c(mongoHospital.getDutyTime6c())
                .dutyTime7s(mongoHospital.getDutyTime7s())
                .dutyTime7c(mongoHospital.getDutyTime7c())
                .dutyTime8s(mongoHospital.getDutyTime8s())
                .dutyTime8c(mongoHospital.getDutyTime8c())
                .build();
    }

    private static String determineClinicType(MongoHospital mongoHospital) {
        // 병원 종류 판별 로직 (필요에 따라 확장 가능)
        String name = mongoHospital.getDutyName();
        if (name != null) {
            if (name.contains("병원")) return "병원";
            if (name.contains("의원")) return "의원";
            if (name.contains("치과")) return "치과";
            if (name.contains("한의원")) return "한의원";
            if (name.contains("약국")) return "약국";
        }
        return "기타";
    }
} 