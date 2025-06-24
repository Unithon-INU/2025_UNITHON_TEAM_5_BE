package com.curelingo.curelingo.mongodb;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "MongoDB API", description = "MongoDB에 다국어 지원 병원 정보를 저장하고 조회하는 API입니다.")
public interface HospitalSwagger {

    @Operation(
            summary = "단일 병원 정보 저장",
            description = "단일 병원 정보를 MongoDB에 저장합니다."
    )
    ResponseEntity<HospitalDto> saveHospital(@RequestBody HospitalDto dto);

    @Operation(
            summary = "병원 전체 자동 저장 (다국어 번역 포함)",
            description = "공공데이터 API를 순회하며 MongoDB에 병원 정보를 자동 저장합니다.\n" +
                    "• 중복(`hpid`)은 저장되지 않습니다\n" +
                    "• 페이지네이션을 통해 모든 데이터를 가져옵니다\n" +
                    "• 병원명과 주소는 구글 번역 API를 통해 영어로 번역되어 함께 저장됩니다\n" +
                    "• 배치 번역을 통해 API 호출을 최소화합니다\n" +
                    "• 번역 결과는 캐시되어 중복 번역을 방지합니다"
    )
    ResponseEntity<String> saveHospitalMongo();

    @Operation(
            summary = "모든 병원 데이터 삭제",
            description = "MongoDB에 저장된 모든 병원 데이터를 삭제합니다.\n" +
                    "주의: 이 작업은 되돌릴 수 없습니다."
    )
    ResponseEntity<String> deleteAllHospitals();

    @Operation(
            summary = "전체 병원 목록 조회 (다국어 지원)",
            description = "MongoDB에 저장된 모든 병원 정보를 조회합니다.\n" +
                    "• language 파라미터를 통해 원하는 언어로 병원명과 주소를 제공받을 수 있습니다\n" +
                    "• 지원 언어: ko (한국어, 기본값), en (영어)\n" +
                    "• 영어 번역이 없는 경우 한국어 원본을 반환합니다"
    )
    ResponseEntity<List<HospitalDto>> getAllHospitals(
            @Parameter(description = "응답 언어 (ko: 한국어, en: 영어)", example = "ko")
            @RequestParam(defaultValue = "ko") String language
    );

    @Operation(
            summary = "응급실 운영 병원 조회 (다국어 지원)",
            description = "응급실을 운영하는 병원만 조회합니다.\n" +
                    "• dutyEryn 값이 '1'인 병원만 필터링됩니다\n" +
                    "• language 파라미터를 통해 원하는 언어로 병원명과 주소를 제공받을 수 있습니다\n" +
                    "• 지원 언어: ko (한국어, 기본값), en (영어)\n" +
                    "• 영어 번역이 없는 경우 한국어 원본을 반환합니다"
    )
    ResponseEntity<List<HospitalDto>> getEmergencyHospitals(
            @Parameter(description = "응답 언어 (ko: 한국어, en: 영어)", example = "ko")
            @RequestParam(defaultValue = "ko") String language
    );
}
