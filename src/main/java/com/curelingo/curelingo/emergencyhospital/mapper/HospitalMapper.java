package com.curelingo.curelingo.emergencyhospital.mapper;

import com.curelingo.curelingo.emergencyhospital.domain.Hospital;
import com.curelingo.curelingo.mongodb.MongoHospital;

public class HospitalMapper {

    public static Hospital toDomain(MongoHospital mongoHospital) {
        return Hospital.builder()
                .hpid(mongoHospital.getHpid())
                .name(mongoHospital.getDutyName())
                .addr(mongoHospital.getDutyAddr())
                .tel(mongoHospital.getDutyTel1())
                .lat(mongoHospital.getWgs84Lat())
                .lng(mongoHospital.getWgs84Lon())
                .h3Cell(null)  // H3 셀 정보는 별도 계산 필요
                .build();
    }
}