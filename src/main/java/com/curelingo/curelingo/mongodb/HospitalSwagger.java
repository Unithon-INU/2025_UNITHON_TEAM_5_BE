package com.curelingo.curelingo.mongodb;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "MongoDB API", description = "MongoDB에 진료과별 전체 병원 정보를 저장하는 API입니다.")
public interface HospitalSwagger {

    @Operation(
            summary = "단일 병원 정보 저장",
            description = "HospitalDto를 입력받아 MongoDB에 병원 정보를 저장합니다."
    )
    ResponseEntity<String> saveHospital(@RequestBody HospitalDto dto);

    @Operation(
            summary = "병원 전체 자동 저장 (진료과목별)",
            description = "공공데이터 진료과별 전체 병원 정보를 MongoDB에 자동 저장합니다.\n" +
                    "QD 파라미터로 특정 진료과목만 저장하거나, 생략 시 모든 진료과목을 저장합니다.\n" +
                    "중복(`hpid`)은 저장되지 않습니다. 페이지네이션을 통해 모든 데이터를 가져옵니다."
    )
    ResponseEntity<String> saveHospitalMongo(
            @Parameter(description = "진료과목 코드 (예: D001=내과, D002=신경과)", example = "D001") 
            @RequestParam(required = false) String qd
    );

    @Operation(
            summary = "모든 병원 데이터 삭제",
            description = "MongoDB에 저장된 모든 병원 데이터를 삭제합니다."
    )
    ResponseEntity<String> deleteAllHospitals();
}
