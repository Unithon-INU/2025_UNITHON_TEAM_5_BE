package com.curelingo.curelingo.egen.basic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HospitalInfoItem {
    @JacksonXmlProperty(localName = "dutyName")
    private String dutyName;

    @JacksonXmlProperty(localName = "dutyTel1")
    private String dutyTel1; // 대표전화

    @JacksonXmlProperty(localName = "dutyTel3")
    private String dutyTel3; // 응급실전화

    @JacksonXmlProperty(localName = "dutyAddr")
    private String dutyAddr; // 주소

    @JacksonXmlProperty(localName = "dutyTime1c")
    private String dutyTime1c; // 진료시간(월요일)

    @JacksonXmlProperty(localName = "dutyTime2c")
    private String dutyTime2c; // 진료시간(화요일)

    @JacksonXmlProperty(localName = "dutyTime3c")
    private String dutyTime3c; // 진료시간(수요일)

    @JacksonXmlProperty(localName = "dutyTime4c")
    private String dutyTime4c; // 진료시간(목요일)

    @JacksonXmlProperty(localName = "dutyTime5c")
    private String dutyTime5c; // 진료시간(금요일)

    @JacksonXmlProperty(localName = "dutyTime6c")
    private String dutyTime6c; // 진료시간(토요일)

    @JacksonXmlProperty(localName = "dutyTime7c")
    private String dutyTime7c; // 진료시간(일요일)

    @JacksonXmlProperty(localName = "dutyTime8c")
    private String dutyTime8c; // 진료시간(공휴일)
}
