package com.curelingo.curelingo.emergencyhospital;

import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceRequest;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "응급상황 병원 추천 API", description = "응급상황에 따른 적절한 병원을 추천해주는 서비스를 제공합니다.")
public interface EmergencyAdvisorSwagger {

    @Operation(summary = "응급상황 병원 추천", description = "환자의 증상과 위치 정보를 바탕으로 가장 적합한 응급 병원을 추천합니다.")
    ResponseEntity<EmergencyAdviceResponse> recommend(
            @Parameter(description = "응급상황 정보 요청") @RequestBody EmergencyAdviceRequest request
    );
} 