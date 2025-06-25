package com.curelingo.curelingo.mongodb;

import com.curelingo.curelingo.egen.dto.ClinicItem;
import com.curelingo.curelingo.egen.EgenService;
import com.curelingo.curelingo.egen.dto.EgenResponse;
import com.curelingo.curelingo.mongodb.repository.HospitalRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final EgenService egenService;

    public HospitalService(HospitalRepository hospitalRepository, EgenService egenService) {
        this.hospitalRepository = hospitalRepository;
        this.egenService = egenService;
    }

    public void saveHospital(HospitalDto dto) {
        MongoHospital existing = null;
        if (hospitalRepository.existsByHpid(dto.getHpid())) {
            existing = hospitalRepository.findById(dto.getHpid()).orElse(null);
        }

        List<String> departments = new ArrayList<>();
        if (existing != null && existing.getDepartments() != null) {
            departments.addAll(existing.getDepartments());
        }
        if (dto.getDepartments() != null) {
            for (String dept : dto.getDepartments()) {
                if (!departments.contains(dept)) {
                    departments.add(dept);
                }
            }
        }

        MongoHospital mongoHospital = MongoHospital.builder()
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
                .departments(departments)
                .build();

        hospitalRepository.save(mongoHospital);
    }

    public int saveHospitalMongo(String qd) {
        int totalSavedCount = 0;
        int pageNo = 1;
        int numOfRows = 1000; // 한 번에 1000개씩 가져오기
        boolean hasMoreData = true;

        System.out.println("진료과목 코드: " + (qd != null ? qd : "전체") + "로 병원 데이터 수집 시작");

        while (hasMoreData) {
            try {
                // EgenService를 통해 clinics 데이터 가져오기
                EgenResponse<ClinicItem> response = egenService.getClinics(null, null, null, qd, null, null, null, pageNo, numOfRows);
                
                if (response == null || response.getBody() == null || response.getBody().getItems() == null) {
                    System.out.println("페이지 " + pageNo + "에서 데이터를 가져올 수 없습니다.");
                    break;
                }

                List<ClinicItem> items = response.getBody().getItems().getItem();
                
                if (items == null || items.isEmpty()) {
                    System.out.println("페이지 " + pageNo + "에서 더 이상 데이터가 없습니다.");
                    hasMoreData = false;
                    break;
                }

                System.out.println("페이지 " + pageNo + "에서 " + items.size() + "개의 병원 데이터를 처리 중...");

                int pageSavedCount = 0;
                for (ClinicItem item : items) {
                    // 필수 필드 검증
                    if (item.getHpid() == null || item.getHpid().trim().isEmpty() || "null".equals(item.getHpid())) {
                        continue; // hpid가 null이거나 빈 값이면 건너뛰기
                    }

                    String hpid = item.getHpid();
                    
                    // 기존 병원 데이터 조회
                    MongoHospital existing = hospitalRepository.findById(hpid).orElse(null);
                    
                    List<String> departments = new ArrayList<>();
                    if (existing != null && existing.getDepartments() != null) {
                        // 기존 진료과 목록 복사
                        departments.addAll(existing.getDepartments());
                    }
                    
                    // 현재 요청한 진료과 추가 (중복 방지)
                    if (qd != null && !qd.trim().isEmpty() && !departments.contains(qd)) {
                        departments.add(qd);
                    }

                    // Hospital 엔티티 생성 (기존 데이터 유지하면서 진료과만 업데이트)
                    MongoHospital mongoHospital = MongoHospital.builder()
                            .hpid(item.getHpid())
                            .dutyName(existing != null ? existing.getDutyName() : safeString(item.getDutyName()))
                            .dutyAddr(existing != null ? existing.getDutyAddr() : safeString(item.getDutyAddr()))
                            .dutyDivNam(existing != null ? existing.getDutyDivNam() : safeString(item.getDutyDivNam()))
                            .dutyEryn(existing != null ? existing.getDutyEryn() : safeString(item.getDutyEryn()))
                            .dutyTel1(existing != null ? existing.getDutyTel1() : safeString(item.getDutyTel1()))
                            .dutyTel3(existing != null ? existing.getDutyTel3() : safeString(item.getDutyTel3()))
                            .dutyEtc(existing != null ? existing.getDutyEtc() : safeString(item.getDutyEtc()))
                            .dutyTime1s(existing != null ? existing.getDutyTime1s() : safeString(item.getDutyTime1s()))
                            .dutyTime1c(existing != null ? existing.getDutyTime1c() : safeString(item.getDutyTime1c()))
                            .dutyTime2s(existing != null ? existing.getDutyTime2s() : safeString(item.getDutyTime2s()))
                            .dutyTime2c(existing != null ? existing.getDutyTime2c() : safeString(item.getDutyTime2c()))
                            .dutyTime3s(existing != null ? existing.getDutyTime3s() : safeString(item.getDutyTime3s()))
                            .dutyTime3c(existing != null ? existing.getDutyTime3c() : safeString(item.getDutyTime3c()))
                            .dutyTime4s(existing != null ? existing.getDutyTime4s() : safeString(item.getDutyTime4s()))
                            .dutyTime4c(existing != null ? existing.getDutyTime4c() : safeString(item.getDutyTime4c()))
                            .dutyTime5s(existing != null ? existing.getDutyTime5s() : safeString(item.getDutyTime5s()))
                            .dutyTime5c(existing != null ? existing.getDutyTime5c() : safeString(item.getDutyTime5c()))
                            .dutyTime6s(existing != null ? existing.getDutyTime6s() : safeString(item.getDutyTime6s()))
                            .dutyTime6c(existing != null ? existing.getDutyTime6c() : safeString(item.getDutyTime6c()))
                            .dutyTime7s(existing != null ? existing.getDutyTime7s() : safeString(item.getDutyTime7s()))
                            .dutyTime7c(existing != null ? existing.getDutyTime7c() : safeString(item.getDutyTime7c()))
                            .dutyTime8s(existing != null ? existing.getDutyTime8s() : safeString(item.getDutyTime8s()))
                            .dutyTime8c(existing != null ? existing.getDutyTime8c() : safeString(item.getDutyTime8c()))
                            .wgs84Lat(existing != null ? existing.getWgs84Lat() : item.getWgs84Lat())
                            .wgs84Lon(existing != null ? existing.getWgs84Lon() : item.getWgs84Lon())
                            .rnum(existing != null ? existing.getRnum() : safeString(item.getRnum()))
                            .departments(departments)
                            .build();

                    hospitalRepository.save(mongoHospital);
                    
                    if (existing == null) {
                        pageSavedCount++; // 새로 저장된 병원만 카운트
                    } else {
                        System.out.println("병원 " + hpid + "에 진료과 '" + qd + "' 추가됨");
                    }
                }

                totalSavedCount += pageSavedCount;
                System.out.println("페이지 " + pageNo + "에서 " + pageSavedCount + "개의 새로운 병원이 저장되었습니다.");

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

        System.out.println("총 " + totalSavedCount + "개의 새로운 병원이 저장되었습니다.");
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
