package com.curelingo.curelingo.egen;

import com.curelingo.curelingo.egen.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Egen API", description = "Egen API 데이터를 제공합니다.")
public interface EgenSwagger {

    @Operation(summary = "응급의료기관 위치정보 조회", description = "응급의료기관 위치정보를 경도/위도별 반경내 정보를 조회할 수 있다.")
    EgenResponse<NearbyHospitalItem> getNearbyHospitals(
            @Parameter(description = "위도", example = "37.64207169115689") @RequestParam double lat,
            @Parameter(description = "경도", example = "126.83145733035825") @RequestParam double lon,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(required = false) Integer pageNo,
            @Parameter(description = "페이지당 건수", example = "10") @RequestParam(required = false) Integer numOfRows
    );

    @Operation(summary = "응급의료기관 기본정보 조회", description = "기관 ID를 기준으로 응급의료기관의 상세정보를 조회할 수 있다.")
    EgenResponse<HospitalInfoItem> getHospitalInfo(
            @Parameter(description = "기관코드", example = "A1100026") @RequestParam String hpid,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(required = false) Integer pageNo,
            @Parameter(description = "페이지당 건수", example = "10") @RequestParam(required = false) Integer numOfRows
    );

    @Operation(summary = "응급실 실시간 가용병상정보 조회", description = "응급실이 보유하고 있는 가용병상 정보현황을 시도/시군구별로 조회할 수 있다.")
    EgenResponse<AvailableBedsItem> getAvailableBeds(
            @Parameter(description = "주소(시도)", example = "서울특별시") @RequestParam String stage1,
            @Parameter(description = "주소(시군구)", example = "강남구") @RequestParam String stage2,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(required = false) Integer pageNo,
            @Parameter(description = "페이지당 건수", example = "10") @RequestParam(required = false) Integer numOfRows
    );

    @Operation(summary = "병/의원 목록정보 조회", description = "병/의원 정보를 시도/시군구/진료요일/기관별/진료과목별로 조회할 수 있다.")
    EgenResponse<ClinicItem> getClinics(
            @Parameter(description = "주소(시도)", example = "서울특별시") @RequestParam(required = false) String Q0,
            @Parameter(description = "주소(시군구)", example = "강남구") @RequestParam(required = false) String Q1,
            @Parameter(description = "기관구분", example = "B") @RequestParam(required = false) String QZ,
            @Parameter(description = "진료과목", example = "D001") @RequestParam(required = false) String QD,
            @Parameter(description = "진료요일", example = "1") @RequestParam(required = false) String QT,
            @Parameter(description = "기관명", example = "삼성병원") @RequestParam(required = false) String QN,
            @Parameter(description = "정렬기준", example = "NAME") @RequestParam(required = false) String ORD,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(required = false) Integer pageNo,
            @Parameter(description = "페이지당 건수", example = "10") @RequestParam(required = false) Integer numOfRows
    );

    @Operation(summary = "병/의원 위치정보 조회", description = "병/의원 위치정보를 경도/위도별 반경내 정보를 조회할 수 있다.")
    EgenResponse<NearbyHospitalItem> getNearbyClinics(
            @Parameter(description = "위도", example = "37.64207169115689") @RequestParam double lat,
            @Parameter(description = "경도", example = "126.83145733035825") @RequestParam double lon,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(required = false) Integer pageNo,
            @Parameter(description = "페이지당 건수", example = "10") @RequestParam(required = false) Integer numOfRows
    );

    @Operation(summary = "병/의원별 기본정보 조회", description = "병/의원별 상세정보를 조회할 수 있다.")
    EgenResponse<HospitalInfoItem> getClinicInfo(
            @Parameter(description = "기관코드", example = "A1100026") @RequestParam String hpid,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(required = false) Integer pageNo,
            @Parameter(description = "페이지당 건수", example = "10") @RequestParam(required = false) Integer numOfRows
    );

    @Operation(summary = "병/의원 FullData 내려받기", description = "진료요일 등의 전체 병의원 정보를 조회하는 병/의원 FullData를 내려받을 수 있다.")
    EgenResponse<HospitalFullInfoItem> getFullData(
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(required = false) Integer pageNo,
            @Parameter(description = "페이지당 건수", example = "10") @RequestParam(required = false) Integer numOfRows
    );
}
