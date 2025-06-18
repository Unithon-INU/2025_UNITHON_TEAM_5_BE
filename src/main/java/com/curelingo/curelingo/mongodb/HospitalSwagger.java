package com.curelingo.curelingo.mongodb;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "MongoDB API", description = "MongoDB에 병원 정보를 저장하는 API입니다.")
public interface HospitalSwagger {

    @Operation(
            summary = "병원 정보 저장",
            description = "HospitalDto를 입력받아 MongoDB에 병원 정보를 저장합니다."
    )
    ResponseEntity<String> saveHospital(@RequestBody HospitalDto dto);
}
