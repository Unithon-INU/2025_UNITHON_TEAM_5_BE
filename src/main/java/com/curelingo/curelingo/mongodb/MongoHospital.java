package com.curelingo.curelingo.mongodb;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "hospital")
public class MongoHospital {

    @Id
    private String hpid; // 기관ID

    private final String dutyName; // 기관명
    private final String dutyAddr; // 주소
    private final String dutyDivNam; // 병원분류명
    private final String dutyEryn; // 응급실운영여부
    private final String dutyTel1; // 대표전화1
    private final String dutyTel3; // 응급실전화
    private final String dutyEtc; // 비고

    private final String dutyTime1s; // 진료시간(월요일) open
    private final String dutyTime1c; // 진료시간(월요일) close
    private final String dutyTime2s; // 진료시간(화요일) open
    private final String dutyTime2c; // 진료시간(화요일) close
    private final String dutyTime3s; // 진료시간(수요일) open
    private final String dutyTime3c; // 진료시간(수요일) close
    private final String dutyTime4s; // 진료시간(목요일) open
    private final String dutyTime4c; // 진료시간(목요일) close
    private final String dutyTime5s; // 진료시간(금요일) open
    private final String dutyTime5c; // 진료시간(금요일) close
    private final String dutyTime6s; // 진료시간(토요일) open
    private final String dutyTime6c; // 진료시간(토요일) close
    private final String dutyTime7s; // 진료시간(일요일) open
    private final String dutyTime7c; // 진료시간(일요일) close
    private final String dutyTime8s; // 진료시간(공휴일) open
    private final String dutyTime8c; // 진료시간(공휴일) close

    private final Double wgs84Lat; // 병원위도
    private final Double wgs84Lon; // 병원경도
    private final String rnum; // 일련번호

    @Builder
    public MongoHospital(
            String hpid,
            String dutyName,
            String dutyAddr,
            String dutyDivNam,
            String dutyEryn,
            String dutyTel1,
            String dutyTel3,
            String dutyEtc,
            String dutyTime1s,
            String dutyTime1c,
            String dutyTime2s,
            String dutyTime2c,
            String dutyTime3s,
            String dutyTime3c,
            String dutyTime4s,
            String dutyTime4c,
            String dutyTime5s,
            String dutyTime5c,
            String dutyTime6s,
            String dutyTime6c,
            String dutyTime7s,
            String dutyTime7c,
            String dutyTime8s,
            String dutyTime8c,
            Double wgs84Lat,
            Double wgs84Lon,
            String rnum
    ) {
        this.hpid = hpid;
        this.dutyName = dutyName;
        this.dutyAddr = dutyAddr;
        this.dutyDivNam = dutyDivNam;
        this.dutyEryn = dutyEryn;
        this.dutyTel1 = dutyTel1;
        this.dutyTel3 = dutyTel3;
        this.dutyEtc = dutyEtc;
        this.dutyTime1s = dutyTime1s;
        this.dutyTime1c = dutyTime1c;
        this.dutyTime2s = dutyTime2s;
        this.dutyTime2c = dutyTime2c;
        this.dutyTime3s = dutyTime3s;
        this.dutyTime3c = dutyTime3c;
        this.dutyTime4s = dutyTime4s;
        this.dutyTime4c = dutyTime4c;
        this.dutyTime5s = dutyTime5s;
        this.dutyTime5c = dutyTime5c;
        this.dutyTime6s = dutyTime6s;
        this.dutyTime6c = dutyTime6c;
        this.dutyTime7s = dutyTime7s;
        this.dutyTime7c = dutyTime7c;
        this.dutyTime8s = dutyTime8s;
        this.dutyTime8c = dutyTime8c;
        this.wgs84Lat = wgs84Lat;
        this.wgs84Lon = wgs84Lon;
        this.rnum = rnum;
    }
}
