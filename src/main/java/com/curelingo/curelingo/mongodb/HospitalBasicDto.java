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
    "dutyAddr", "dutyAddrEn"
})
public class HospitalBasicDto {
    private String hpid;
    private String dutyName;      // 한국어 병원명
    private String dutyAddr;      // 한국어 주소

    // 응답에서만 포함되는 영어 필드들
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dutyNameEn;    // 영어 병원명
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dutyAddrEn;    // 영어 주소

    /**
     * MongoHospital에서 HospitalBasicDto로 변환하는 팩토리 메서드
     */
    public static HospitalBasicDto from(MongoHospital mongoHospital) {
        return HospitalBasicDto.builder()
                .hpid(mongoHospital.getHpid())
                .dutyName(mongoHospital.getDutyName())
                .dutyNameEn(mongoHospital.getDutyNameEn())
                .dutyAddr(mongoHospital.getDutyAddr())
                .dutyAddrEn(mongoHospital.getDutyAddrEn())
                .build();
    }

    /**
     * 언어에 따라 적절한 HospitalBasicDto를 생성하는 팩토리 메서드
     */
    public static HospitalBasicDto from(MongoHospital mongoHospital, String language) {
        boolean useEnglish = "en".equalsIgnoreCase(language);
        
        if (useEnglish) {
            // 영어 조회: 한국어 원본 + 영어 번역 모두 표시
            return HospitalBasicDto.builder()
                    .hpid(mongoHospital.getHpid())
                    .dutyName(mongoHospital.getDutyName())  // 한국어 원본
                    .dutyNameEn(mongoHospital.getDutyNameEn())  // 영어 번역
                    .dutyAddr(mongoHospital.getDutyAddr())  // 한국어 원본
                    .dutyAddrEn(mongoHospital.getDutyAddrEn())  // 영어 번역
                    .build();
        } else {
            // 한국어 조회: 한국어만
            return HospitalBasicDto.builder()
                    .hpid(mongoHospital.getHpid())
                    .dutyName(mongoHospital.getDutyName())
                    .dutyNameEn(null)  // 한국어 조회시에는 영어 필드 제외
                    .dutyAddr(mongoHospital.getDutyAddr())
                    .dutyAddrEn(null)  // 한국어 조회시에는 영어 필드 제외
                    .build();
        }
    }
} 