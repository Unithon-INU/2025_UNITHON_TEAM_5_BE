package com.curelingo.curelingo.mongodb;

import com.curelingo.curelingo.egen.dto.HospitalFullInfoItem;
import com.curelingo.curelingo.egen.EgenService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final EgenService egenService;

    public HospitalService(HospitalRepository hospitalRepository, EgenService egenService) {
        this.hospitalRepository = hospitalRepository;
        this.egenService = egenService;
    }

    public void saveHospital(HospitalDto dto) {
        if (hospitalRepository.existsByHpid(dto.getHpid())) {
            return; // 중복 저장 방지
        }

        Hospital hospital = Hospital.builder()
                .hpid(dto.getHpid())
                .dutyName(dto.getDutyName())
                .dutyAddr(dto.getDutyAddr())
                .dutyDivNam(dto.getDutyDivNam())
                .dutyEryn(dto.getDutyEryn())
                .dutyTel1(dto.getDutyTel1())
                .dutyTel3(dto.getDutyTel3())
                .dutyEtc(dto.getDutyEtc())
                .dutyTime1s(dto.getDutyTime1s())
                .dutyTime1c(dto.getDutyTime1c())
                .dutyTime2s(dto.getDutyTime2s())
                .dutyTime2c(dto.getDutyTime2c())
                .dutyTime3s(dto.getDutyTime3s())
                .dutyTime3c(dto.getDutyTime3c())
                .dutyTime4s(dto.getDutyTime4s())
                .dutyTime4c(dto.getDutyTime4c())
                .dutyTime5s(dto.getDutyTime5s())
                .dutyTime5c(dto.getDutyTime5c())
                .dutyTime6s(dto.getDutyTime6s())
                .dutyTime6c(dto.getDutyTime6c())
                .dutyTime7s(dto.getDutyTime7s())
                .dutyTime7c(dto.getDutyTime7c())
                .dutyTime8s(dto.getDutyTime8s())
                .dutyTime8c(dto.getDutyTime8c())
                .wgs84Lat(dto.getWgs84Lat())
                .wgs84Lon(dto.getWgs84Lon())
                .rnum(dto.getRnum())
                .build();

        hospitalRepository.save(hospital);
    }

    public int saveHospitalMongo() {
        int totalSavedCount = 0;
        int pageNo = 1;
        int numOfRows = 1000; // 한 번에 1000개씩 가져오기
        boolean hasMoreData = true;

        while (hasMoreData) {
            try {
                // EgenService를 통해 데이터 가져오기
                var response = egenService.getFullData(pageNo, numOfRows);
                
                if (response == null || response.getBody() == null || response.getBody().getItems() == null) {
                    System.out.println("페이지 " + pageNo + "에서 데이터를 가져올 수 없습니다.");
                    break;
                }

                List<HospitalFullInfoItem> items = response.getBody().getItems().getItem();
                
                if (items == null || items.isEmpty()) {
                    System.out.println("페이지 " + pageNo + "에서 더 이상 데이터가 없습니다.");
                    hasMoreData = false;
                    break;
                }

                System.out.println("페이지 " + pageNo + "에서 " + items.size() + "개의 병원 데이터를 처리 중...");

                int pageSavedCount = 0;
                for (HospitalFullInfoItem item : items) {
                    // 필수 필드 검증
                    if (item.getHpid() == null || item.getHpid().trim().isEmpty() || "null".equals(item.getHpid())) {
                        continue; // hpid가 null이거나 빈 값이면 건너뛰기
                    }

                    // 중복 체크
                    if (hospitalRepository.existsByHpid(item.getHpid())) {
                        continue;
                    }

                    // Hospital 엔티티 생성
                    Hospital hospital = Hospital.builder()
                            .hpid(item.getHpid())
                            .dutyName(safeString(item.getDutyName()))
                            .dutyAddr(safeString(item.getDutyAddr()))
                            .dutyDivNam(safeString(item.getDutyDivNam()))
                            .dutyEryn(safeString(item.getDutyEryn()))
                            .dutyTel1(safeString(item.getDutyTel1()))
                            .dutyTel3(safeString(item.getDutyTel3()))
                            .dutyEtc(safeString(item.getDutyEtc()))
                            .dutyTime1s(safeString(item.getDutyTime1s()))
                            .dutyTime1c(safeString(item.getDutyTime1c()))
                            .dutyTime2s(safeString(item.getDutyTime2s()))
                            .dutyTime2c(safeString(item.getDutyTime2c()))
                            .dutyTime3s(safeString(item.getDutyTime3s()))
                            .dutyTime3c(safeString(item.getDutyTime3c()))
                            .dutyTime4s(safeString(item.getDutyTime4s()))
                            .dutyTime4c(safeString(item.getDutyTime4c()))
                            .dutyTime5s(safeString(item.getDutyTime5s()))
                            .dutyTime5c(safeString(item.getDutyTime5c()))
                            .dutyTime6s(safeString(item.getDutyTime6s()))
                            .dutyTime6c(safeString(item.getDutyTime6c()))
                            .dutyTime7s(safeString(item.getDutyTime7s()))
                            .dutyTime7c(safeString(item.getDutyTime7c()))
                            .dutyTime8s(safeString(item.getDutyTime8s()))
                            .dutyTime8c(safeString(item.getDutyTime8c()))
                            .wgs84Lat(item.getWgs84Lat())
                            .wgs84Lon(item.getWgs84Lon())
                            .rnum(safeString(item.getRnum()))
                            .build();

                    hospitalRepository.save(hospital);
                    pageSavedCount++;
                }

                totalSavedCount += pageSavedCount;
                System.out.println("페이지 " + pageNo + "에서 " + pageSavedCount + "개의 병원이 저장되었습니다.");

                // 다음 페이지로 이동
                pageNo++;

                // API 호출 간격 조절 (서버 부하 방지)
                Thread.sleep(100);

            } catch (Exception e) {
                System.err.println("페이지 " + pageNo + " 처리 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }

        System.out.println("총 " + totalSavedCount + "개의 병원이 저장되었습니다.");
        return totalSavedCount;
    }

    /**
     * null 값을 안전하게 문자열로 변환하는 헬퍼 메서드
     */
    private String safeString(String value) {
        if (value == null || "null".equals(value) || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    /**
     * 모든 병원 데이터 삭제
     */
    public void deleteAllHospitals() {
        hospitalRepository.deleteAll();
        System.out.println("모든 병원 데이터가 삭제되었습니다.");
    }
}
