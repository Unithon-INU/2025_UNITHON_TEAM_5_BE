package com.curelingo.curelingo.clinic;

import com.curelingo.curelingo.clinic.domain.Clinic;
import com.curelingo.curelingo.clinic.mapper.ClinicMapper;
import com.curelingo.curelingo.location.H3ResolutionUtil;
import com.curelingo.curelingo.location.H3Service;
import com.curelingo.curelingo.mongodb.MongoHospital;
import com.curelingo.curelingo.mongodb.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClinicService {

    private final H3Service h3Service;
    private final HospitalRepository hospitalRepository;

    /**
     * 진료과별 필터링된 인근 병원 검색 (반경 3km 고정)
     *
     * @param lat        현재 위도
     * @param lng        현재 경도
     * @param department 진료과 코드 (D001~D032)
     * @param language   언어 (ko: 한국어, en: 영어)
     * @return 해당 진료과가 있는 인근 병원 목록
     */
    public List<Map<String, Object>> findNearbyClinicsByDepartment(double lat, double lng, String department, String language) {
        double radiusKm = 3.0; // 반경 3km 고정
        
        int res = H3ResolutionUtil.chooseResolution(radiusKm);
        int k = H3ResolutionUtil.kFromRadius(radiusKm, res);
        log.info("[Clinic] 진료과 검색 - 반경 {}km, 진료과: {} → res={}, k={}", radiusKm, department, res, k);

        // 모든 병원 조회
        List<MongoHospital> mongoHospitals = hospitalRepository.findAll();
        
        // 해당 진료과가 있는 병원만 필터링
        List<Clinic> clinicsWithDepartment = mongoHospitals.stream()
                .filter(mongoHospital -> mongoHospital.getDepartments() != null && 
                                       mongoHospital.getDepartments().contains(department))
                .map(ClinicMapper::toDomain)
                .toList();

        log.info("[Clinic] {} 진료과 보유 병원 수: {}", department, clinicsWithDepartment.size());

        String userCell = h3Service.latLngToCell(lat, lng, res);
        List<String> neighborCells = h3Service.gridDisk(userCell, k);
        log.info("[Clinic] gridDisk 결과 셀 개수: {}", neighborCells.size());

        // H3 셀 기반 1차 필터링
        List<Clinic> candidates = new ArrayList<>();
        for (Clinic clinic : clinicsWithDepartment) {
            String clinicCell = h3Service.latLngToCell(clinic.getLat(), clinic.getLng(), res);
            if (neighborCells.contains(clinicCell)) {
                candidates.add(clinic);
            }
        }
        log.info("[Clinic] H3 후보 {} 진료과 병원 개수: {}", department, candidates.size());

        // 정확한 거리 계산 및 2차 필터링
        List<Map<String, Object>> result = new ArrayList<>();
        for (Clinic clinic : candidates) {
            double distanceKm = h3Service.calcDistanceKm(lat, lng, clinic.getLat(), clinic.getLng());
            log.debug("[Clinic] {} {} 진료과 거리: {}km", clinic.getName(), department, distanceKm);
            
            if (distanceKm <= radiusKm) {
                // LinkedHashMap을 사용하여 필드 순서 보장
                Map<String, Object> hospitalData = new LinkedHashMap<>();
                
                // 1. 기본 정보
                hospitalData.put("hpid", clinic.getHpid());
                
                if ("en".equals(language)) {
                    // 영어 응답
                    hospitalData.put("nameEn", clinic.getNameEn() != null ? clinic.getNameEn() : clinic.getName());
                    hospitalData.put("addressEn", clinic.getAddrEn() != null ? clinic.getAddrEn() : clinic.getAddr());
                } else {
                    // 한국어 응답 (기본값)
                    hospitalData.put("name", clinic.getName());
                    hospitalData.put("address", clinic.getAddr());
                }
                
                hospitalData.put("tel", clinic.getTel());
                
                // 2. 위치 정보
                hospitalData.put("lat", clinic.getLat());
                hospitalData.put("lng", clinic.getLng());
                hospitalData.put("distanceKm", distanceKm);
                
                // 3. 운영시간 정보 (요일 순서대로)
                hospitalData.put("dutyTime1s", clinic.getDutyTime1s()); // 월요일 시작
                hospitalData.put("dutyTime1c", clinic.getDutyTime1c()); // 월요일 종료
                hospitalData.put("dutyTime2s", clinic.getDutyTime2s()); // 화요일 시작
                hospitalData.put("dutyTime2c", clinic.getDutyTime2c()); // 화요일 종료
                hospitalData.put("dutyTime3s", clinic.getDutyTime3s()); // 수요일 시작
                hospitalData.put("dutyTime3c", clinic.getDutyTime3c()); // 수요일 종료
                hospitalData.put("dutyTime4s", clinic.getDutyTime4s()); // 목요일 시작
                hospitalData.put("dutyTime4c", clinic.getDutyTime4c()); // 목요일 종료
                hospitalData.put("dutyTime5s", clinic.getDutyTime5s()); // 금요일 시작
                hospitalData.put("dutyTime5c", clinic.getDutyTime5c()); // 금요일 종료
                hospitalData.put("dutyTime6s", clinic.getDutyTime6s()); // 토요일 시작
                hospitalData.put("dutyTime6c", clinic.getDutyTime6c()); // 토요일 종료
                hospitalData.put("dutyTime7s", clinic.getDutyTime7s()); // 일요일 시작
                hospitalData.put("dutyTime7c", clinic.getDutyTime7c()); // 일요일 종료
                hospitalData.put("dutyTime8s", clinic.getDutyTime8s()); // 공휴일 시작
                hospitalData.put("dutyTime8c", clinic.getDutyTime8c()); // 공휴일 종료
                
                result.add(hospitalData);
            }
        }

        log.info("[Clinic] 반경 {}km 이내 {} 진료과 최종 병원 개수: {}", radiusKm, department, result.size());
        
        // 거리 기준 오름차순 정렬
        result.sort(Comparator.comparingDouble(data -> (Double) data.get("distanceKm")));
        return result;
    }
} 