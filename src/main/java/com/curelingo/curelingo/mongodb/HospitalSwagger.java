package com.curelingo.curelingo.mongodb;

import com.curelingo.curelingo.mongodb.dto.HospitalBasicDto;
import com.curelingo.curelingo.mongodb.dto.HospitalDto;
import com.curelingo.curelingo.mongodb.dto.HospitalRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "MongoDB API", description = "MongoDB에 다국어 지원 병원 정보를 저장하고 조회하는 API입니다.")
public interface HospitalSwagger {

    @Operation(
            summary = "단일 병원 정보 저장",
            description = "단일 병원 정보를 MongoDB에 저장합니다."
    )
    ResponseEntity<HospitalDto> saveHospital(@RequestBody HospitalRequestDto requestDto);

    @Operation(
            summary = "병원 전체 자동 저장 (다국어 번역 포함)",
            description = "공공데이터 API를 순회하며 MongoDB에 병원 정보를 자동 저장합니다."
    )
    ResponseEntity<String> saveHospitalMongo();

    @Operation(
            summary = "모든 병원 데이터 삭제",
            description = "MongoDB에 저장된 모든 병원 데이터를 삭제합니다."
    )
    ResponseEntity<String> deleteAllHospitals();

    @Operation(
            summary = "한/영 병원 기본 정보(병원명, 주소) 조회",
            description = "hpid로 병원의 기본 정보(병원명, 주소)를 조회합니다."
    )
    ResponseEntity<HospitalBasicDto> getHospitalBasicInfo(
            @Parameter(description = "병원 고유 식별자", example = "A1100119")
            @PathVariable String hpid,
            @Parameter(description = "응답 언어 (ko: 한국어, en: 영어)", example = "ko")
            @RequestParam(defaultValue = "ko") String language
    );
}
