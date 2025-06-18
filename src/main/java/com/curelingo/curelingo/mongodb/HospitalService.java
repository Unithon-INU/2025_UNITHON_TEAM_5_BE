package com.curelingo.curelingo.mongodb;

import org.springframework.stereotype.Service;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public void saveHospital(HospitalDto dto) {
        Hospital hospital = Hospital.builder()
                .hpid(dto.getHpid())
                .dutyName(dto.getDutyName())
                .dutyAddr(dto.getDutyAddr())
                .dutyDivNam(dto.getDutyDivNam())
                .dutyEryn(dto.getDutyEryn())
                .dutyTel1(dto.getDutyTel1())
                .dutyTel3(dto.getDutyTel3())
                .dutyEtc(dto.getDutyEtc())
                .dutyTime1s(dto.getDutyTime1s())
                .dutyTime1c(dto.getDutyTime1c())
                .dutyTime2s(dto.getDutyTime2s())
                .dutyTime2c(dto.getDutyTime2c())
                .dutyTime3s(dto.getDutyTime3s())
                .dutyTime3c(dto.getDutyTime3c())
                .dutyTime4s(dto.getDutyTime4s())
                .dutyTime4c(dto.getDutyTime4c())
                .dutyTime5s(dto.getDutyTime5s())
                .dutyTime5c(dto.getDutyTime5c())
                .dutyTime6s(dto.getDutyTime6s())
                .dutyTime6c(dto.getDutyTime6c())
                .dutyTime7s(dto.getDutyTime7s())
                .dutyTime7c(dto.getDutyTime7c())
                .dutyTime8s(dto.getDutyTime8s())
                .dutyTime8c(dto.getDutyTime8c())
                .wgs84Lat(dto.getWgs84Lat())
                .wgs84Lon(dto.getWgs84Lon())
                .rnum(dto.getRnum())
                .build();

        hospitalRepository.save(hospital);
    }
}
