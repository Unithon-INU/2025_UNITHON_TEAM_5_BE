package com.curelingo.curelingo.emergencyhospital;

import com.curelingo.curelingo.emergencyhospital.dto.EmergencyBedStatus;
import com.curelingo.curelingo.emergencyhospital.dto.NearbyHospitalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "응급의료기관 API", description = "응급상황 병원 추천 및 인근 응급실 조회 서비스를 제공합니다.")
public interface EmergencyAdvisorSwagger {

    @Operation(summary = "인근 응급실 목록 조회", description = "현재 위치를 기준으로 반경 5km 이내의 응급실 목록을 조회합니다.")
    ResponseEntity<List<Map<String, Object>>> getNearbyHospitals(
            @Parameter(description = "현재 위도", example = "37.5154") @RequestParam double lat,
            @Parameter(description = "현재 경도", example = "127.0346") @RequestParam double lon,
            @Parameter(description = "언어 (ko: 한국어, en: 영어)", example = "ko") @RequestParam(defaultValue = "ko") String language
    );

    @Operation(summary = "인근 응급실 가용병상 조회", description = "현재 위치를 기준으로 반경 N km 이내의 응급실 목록과 가용병상 정보를 조회합니다.")
    List<EmergencyBedStatus> getNearbyEmergencyBedStatus(
            @Parameter(description = "현재 위도", example = "37.5154") @RequestParam double lat,
            @Parameter(description = "현재 경도", example = "127.0346") @RequestParam double lng,
            @Parameter(description = "검색 반경(km)", example = "5.0") @RequestParam double radiusKm
    );
}
