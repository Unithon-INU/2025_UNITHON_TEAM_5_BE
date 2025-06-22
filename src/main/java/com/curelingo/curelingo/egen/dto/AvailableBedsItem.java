package com.curelingo.curelingo.egen.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AvailableBedsItem {
    private String hpid;          // 기관코드
    private String dutyName;      // 기관명
    private String dutyTel3;      // 응급실전화
    private String hvidate;       // 입력일시

    private Integer hvec;         // 일반병상 (가용)
    private Integer hvs01;        // 일반병상 (기준)
    private Integer hv28;         // 소아 병상 (가용)
    private Integer hvs02;        // 소아 병상 (기준)
    private Integer hvoc;         // 수술실 (가용)
    private Integer hvs22;        // 수술실 (기준)
    private Integer hvgc;         // 입원실 일반 (가용)
    private Integer hvs38;        // 입원실 일반 (기준)
    private Integer hv9;          // [중환자실] 외상 (가용)
    private Integer hvs14;        // [중환자실] 외상 (기준)
    private Integer hv38;         // [입원실] 외상전용 (가용)
    private Integer hvs21;        // [입원실] 외상전용 (기준)
    private Integer hv60;         // 외상소생실 (가용)
    private Integer hvs60;        // 외상소생실 (기준)
}
