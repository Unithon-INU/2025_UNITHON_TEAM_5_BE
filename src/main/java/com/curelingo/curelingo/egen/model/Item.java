package com.curelingo.curelingo.egen.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    @JacksonXmlProperty(localName = "dutyName")
    private String dutyName;

    @JacksonXmlProperty(localName = "dutyTel3")
    private String dutyTel3;

    @JacksonXmlProperty(localName = "hvgc")
    private String inpatientRoom; // 입원실

    @JacksonXmlProperty(localName = "hvicc")
    private String generalICU; // 일반중환자실

    @JacksonXmlProperty(localName = "hv2")
    private String internalMedicineICU; // 내과중환자실

    @JacksonXmlProperty(localName = "hv3")
    private String surgicalICU; // 외과중환자실

    @JacksonXmlProperty(localName = "hv5")
    private String neurologyWard; // 신경과입원실

    @JacksonXmlProperty(localName = "hvctayn")
    private String ctAvailable; // CT 가용 여부 (Y/N)

    @JacksonXmlProperty(localName = "hvmriayn")
    private String mriAvailable; // MRI 가용 여부 (Y/N)

    @JacksonXmlProperty(localName = "hvventiayn")
    private String ventilatorAvailable; // 인공호흡기 가용 여부 (Y/N)

    @JacksonXmlProperty(localName = "hvamyn")
    private String ambulanceAvailable; // 구급차 가용 여부 (Y/N)

    // Getter/Setter 생략 가능 (필드 접근 설정되어 있음)
}
