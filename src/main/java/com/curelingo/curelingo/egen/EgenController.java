package com.curelingo.curelingo.egen;

import com.curelingo.curelingo.egen.dto.EgenResponse;
import com.curelingo.curelingo.egen.dto.NearbyHospitalItem;
import com.curelingo.curelingo.egen.dto.HospitalInfoItem;
import com.curelingo.curelingo.egen.dto.AvailableBedsItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "응급의료정보 API", description = "응급 병원 위치/정보/병상 데이터를 제공합니다.")
@RestController
@RequestMapping("/api/egen")
@RequiredArgsConstructor
public class EgenController {

    private final EgenService egenService;

    @Operation(summary = "가까운 응급 병원 목록 조회", description = "위도와 경도를 기준으로 가까운 응급 병원을 반환합니다.")
    @GetMapping("/nearby")
    public EgenResponse<NearbyHospitalItem> getNearbyHospitals(@RequestParam double lat, @RequestParam double lon) {
        return egenService.getNearbyHospitals(lat, lon);
    }

    @Operation(summary = "병상 정보 조회", description = "지역 기준으로 사용 가능한 병상 정보를 조회합니다.")
    @GetMapping("/info")
    public EgenResponse<HospitalInfoItem> getHospitalInfo(@RequestParam String hpid) {
        return egenService.getHospitalInfo(hpid);
    }

    @Operation(summary = "병원 정보 상세 조회", description = "기관 ID를 기준으로 병원 상세 정보를 조회합니다.")
    @GetMapping("/beds")
    public EgenResponse<AvailableBedsItem> getAvailableBeds(
            @RequestParam String stage1,
            @RequestParam String stage2
    ) {
        return egenService.getAvailableBeds(stage1, stage2);
    }
}
