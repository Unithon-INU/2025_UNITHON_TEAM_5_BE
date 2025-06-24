package com.curelingo.curelingo.mongodb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonPropertyOrder({
    "hpid", 
    "dutyName", "dutyNameEn",
    "dutyAddr", "dutyAddrEn", 
    "dutyDivNam", "dutyEryn", 
    "dutyTel1", "dutyTel3", "dutyEtc",
    "dutyTime1s", "dutyTime1c", "dutyTime2s", "dutyTime2c",
    "dutyTime3s", "dutyTime3c", "dutyTime4s", "dutyTime4c", 
    "dutyTime5s", "dutyTime5c", "dutyTime6s", "dutyTime6c",
    "dutyTime7s", "dutyTime7c", "dutyTime8s", "dutyTime8c",
    "wgs84Lat", "wgs84Lon", "rnum"
})
public class HospitalDto {
    private String hpid;
    private String dutyName;      // 한국어 병원명
    private String dutyAddr;      // 한국어 주소
    private String dutyDivNam;
    private String dutyEryn;
    private String dutyTel1;
    private String dutyTel3;
    private String dutyEtc;
    private String dutyTime1s;
    private String dutyTime1c;
    private String dutyTime2s;
    private String dutyTime2c;
    private String dutyTime3s;
    private String dutyTime3c;
    private String dutyTime4s;
    private String dutyTime4c;
    private String dutyTime5s;
    private String dutyTime5c;
    private String dutyTime6s;
    private String dutyTime6c;
    private String dutyTime7s;
    private String dutyTime7c;
    private String dutyTime8s;
    private String dutyTime8c;
    private Double wgs84Lat;
    private Double wgs84Lon;
    private String rnum;

    // 응답에서만 포함되는 영어 필드들 (요청 시에는 무시됨)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dutyNameEn;    // 영어 병원명 (응답용)
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dutyAddrEn;    // 영어 주소 (응답용)

    /**
     * 언어에 따라 병원명을 반환합니다.
     */
    public String getLocalizedName(String language) {
        if ("en".equalsIgnoreCase(language) && dutyNameEn != null) {
            return dutyNameEn;
        }
        return dutyName; // 기본값은 한국어
    }

    /**
     * 언어에 따라 주소를 반환합니다.
     */
    public String getLocalizedAddress(String language) {
        if ("en".equalsIgnoreCase(language) && dutyAddrEn != null) {
            return dutyAddrEn;
        }
        return dutyAddr; // 기본값은 한국어
    }

    /**
     * MongoHospital에서 HospitalDto로 변환하는 팩토리 메서드
     */
    public static HospitalDto from(MongoHospital mongoHospital) {
        return HospitalDto.builder()
                .hpid(mongoHospital.getHpid())
                .dutyName(mongoHospital.getDutyName())
                .dutyNameEn(mongoHospital.getDutyNameEn())
                .dutyAddr(mongoHospital.getDutyAddr())
                .dutyAddrEn(mongoHospital.getDutyAddrEn())
                .dutyDivNam(mongoHospital.getDutyDivNam())
                .dutyEryn(mongoHospital.getDutyEryn())
                .dutyTel1(mongoHospital.getDutyTel1())
                .dutyTel3(mongoHospital.getDutyTel3())
                .dutyEtc(mongoHospital.getDutyEtc())
                .dutyTime1s(mongoHospital.getDutyTime1s())
                .dutyTime1c(mongoHospital.getDutyTime1c())
                .dutyTime2s(mongoHospital.getDutyTime2s())
                .dutyTime2c(mongoHospital.getDutyTime2c())
                .dutyTime3s(mongoHospital.getDutyTime3s())
                .dutyTime3c(mongoHospital.getDutyTime3c())
                .dutyTime4s(mongoHospital.getDutyTime4s())
                .dutyTime4c(mongoHospital.getDutyTime4c())
                .dutyTime5s(mongoHospital.getDutyTime5s())
                .dutyTime5c(mongoHospital.getDutyTime5c())
                .dutyTime6s(mongoHospital.getDutyTime6s())
                .dutyTime6c(mongoHospital.getDutyTime6c())
                .dutyTime7s(mongoHospital.getDutyTime7s())
                .dutyTime7c(mongoHospital.getDutyTime7c())
                .dutyTime8s(mongoHospital.getDutyTime8s())
                .dutyTime8c(mongoHospital.getDutyTime8c())
                .wgs84Lat(mongoHospital.getWgs84Lat())
                .wgs84Lon(mongoHospital.getWgs84Lon())
                .rnum(mongoHospital.getRnum())
                .build();
    }

    /**
     * 요청용 생성자 - 영어 필드 없이 생성
     */
    public static HospitalDto forRequest(String hpid, String dutyName, String dutyAddr, 
                                        String dutyTel1, String dutyEryn) {
        return HospitalDto.builder()
                .hpid(hpid)
                .dutyName(dutyName)
                .dutyAddr(dutyAddr)
                .dutyTel1(dutyTel1)
                .dutyEryn(dutyEryn)
                .build();
    }
}
