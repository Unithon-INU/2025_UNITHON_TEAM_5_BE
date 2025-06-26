package com.curelingo.curelingo.clinic;

import com.curelingo.curelingo.clinic.dto.NearbyClinicDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "병원 검색 API", description = "H3 기반 진료과별 병원 검색 서비스를 제공합니다.")
public interface ClinicSwagger {

    @Operation(summary = "진료과별 인근 병원 검색 (3km 반경)", description = "현재 위치를 기준으로 반경 3km 이내의 특정 진료과가 있는 병원을 검색합니다.")
    ResponseEntity<List<Map<String, Object>>> getNearbyClinicsByDepartment(
            @Parameter(description = "현재 위도", example = "37.5154") @RequestParam double lat,
            @Parameter(description = "현재 경도", example = "127.0346") @RequestParam double lng,
            @Parameter(description = "진료과 코드 (D001~D032)", example = "D001") @RequestParam String department,
            @Parameter(description = "언어 (ko: 한국어, en: 영어)", example = "ko") @RequestParam(defaultValue = "ko") String language
    );
} 