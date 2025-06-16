package com.curelingo.curelingo.emergencyhospital.stub;

import com.curelingo.curelingo.emergencyhospital.domain.Hospital;
import java.util.List;

public class HospitalStubData {
    public static List<Hospital> getAll() {
        return List.of(
                Hospital.builder()
                        .hpid("A1124291")
                        .name("강남광동병원")
                        .tel("02-2222-4888")
                        .addr("서울 강남구 봉은사로 612 (삼성동)")
                        .lat(37.514281)
                        .lng(127.062147)
                        .build(),
                Hospital.builder()
                        .hpid("A1122465")
                        .name("강남와이케이병원")
                        .tel("02-6967-8200")
                        .addr("서울 강남구 학동로 233 (논현동)")
                        .lat(37.515498)
                        .lng(127.034699)
                        .build(),
                Hospital.builder()
                        .hpid("A1121286")
                        .name("강남유나이티드병원")
                        .tel("02-1644-0075")
                        .addr("서울 강남구 남부순환로 2609 (도곡동)")
                        .lat(37.484963)
                        .lng(127.035096)
                        .build(),
                Hospital.builder()
                        .hpid("A2000001")
                        .name("송파성모병원")
                        .tel("02-1234-5678")
                        .addr("서울 송파구 송파대로 387")
                        .lat(37.502223)
                        .lng(127.112370)
                        .build(),
                Hospital.builder()
                        .hpid("A2000002")
                        .name("마포성심병원")
                        .tel("02-2233-4455")
                        .addr("서울 마포구 마포대로 203")
                        .lat(37.539465)
                        .lng(126.945504)
                        .build(),
                Hospital.builder()
                        .hpid("A3000001")
                        .name("영등포병원")
                        .tel("02-5678-1234")
                        .addr("서울 영등포구 영등포로 123")
                        .lat(37.522390)
                        .lng(126.905896)
                        .build(),
                Hospital.builder()
                        .hpid("A4000001")
                        .name("분당서울대병원")
                        .tel("031-787-1114")
                        .addr("경기 성남시 분당구 구미로 173번길 82")
                        .lat(37.359531)
                        .lng(127.113247)
                        .build(),
                Hospital.builder()
                        .hpid("A5000001")
                        .name("부천순천향병원")
                        .tel("032-621-5114")
                        .addr("경기 부천시 조마루로 170")
                        .lat(37.494544)
                        .lng(126.777922)
                        .build()
        );
    }
}
