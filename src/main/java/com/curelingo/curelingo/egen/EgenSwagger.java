package com.curelingo.curelingo.egen;

import com.curelingo.curelingo.egen.dto.EgenResponse;
import com.curelingo.curelingo.egen.dto.NearbyHospitalItem;
import com.curelingo.curelingo.egen.dto.HospitalInfoItem;
import com.curelingo.curelingo.egen.dto.AvailableBedsItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Egen API", description = "Egen API 데이터를 제공합니다.")
public interface EgenSwagger {

    @Operation(summary = "응급의료기관 위치정보 조회", description = "응급의료기관 위치정보를 경도/위도별 반경내 정보를 조회할 수 있다.")
    EgenResponse<NearbyHospitalItem> getNearbyHospitals(
            @Parameter(description = "위도", example = "37.488132562487905") @RequestParam double lat, 
            @Parameter(description = "경도", example = "127.08515659273706") @RequestParam double lon
    );

    @Operation(summary = "응급의료기관 기본정보 조회", description = "기관 ID를 기준으로 응급의료기관의 상세정보를 조회할 수 있다.")
    EgenResponse<HospitalInfoItem> getHospitalInfo(
            @Parameter(description = "기관코드", example = "A0000028") @RequestParam String hpid
    );

    @Operation(summary = "응급실 실시간 가용병상정보 조회", description = "응급실이 보유하고 있는 가용병상 정보현황을 시도/시군구별로 조회할 수 있다.")
    EgenResponse<AvailableBedsItem> getAvailableBeds(
            @Parameter(description = "주소(시도)", example = "서울특별시") @RequestParam String stage1,
            @Parameter(description = "주소(시군구)", example = "강남구") @RequestParam String stage2
    );
}
