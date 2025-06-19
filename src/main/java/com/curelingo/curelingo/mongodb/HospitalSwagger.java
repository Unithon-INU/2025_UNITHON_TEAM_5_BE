package com.curelingo.curelingo.mongodb;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "MongoDB API", description = "MongoDB에 FullData 기반 병원 정보를 저장하는 API입니다.")
public interface HospitalSwagger {

    @Operation(
            summary = "단일 병원 정보 저장",
            description = "HospitalDto를 입력받아 MongoDB에 병원 정보를 저장합니다."
    )
    ResponseEntity<String> saveHospital(@RequestBody HospitalDto dto);

    @Operation(
            summary = "병원 전체 자동 저장",
            description = "공공데이터 API를 순회하며 MongoDB에 병원 정보를 자동 저장합니다.\n" +
                    "중복(`hpid`)은 저장되지 않습니다. 페이지네이션을 통해 모든 데이터를 가져옵니다."
    )
    ResponseEntity<String> saveHospitalMongo();

    @Operation(
            summary = "모든 병원 데이터 삭제",
            description = "MongoDB에 저장된 모든 병원 데이터를 삭제합니다."
    )
    ResponseEntity<String> deleteAllHospitals();
}
