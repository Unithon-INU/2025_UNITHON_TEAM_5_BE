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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
     * @param clientTime 클라이언트 현재 시간
     * @return 해당 진료과가 있는 인근 병원 목록
     */
    public List<Map<String, Object>> findNearbyClinicsByDepartment(double lat, double lng, String department, String language, LocalDateTime clientTime) {
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
                
                // 3. 운영 여부 (현재 시간 기준)
                boolean isOpen = isClinicOpen(clinic, clientTime);
                hospitalData.put("isOpen", isOpen);
                
                // 4. 운영시간 정보 (요일 순서대로)
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
    
    /**
     * 클리닉 운영 여부 확인
     * 
     * @param clinic 클리닉 정보
     * @param currentTime 현재 시간
     * @return 운영 중이면 true, 아니면 false
     */
    private boolean isClinicOpen(Clinic clinic, LocalDateTime currentTime) {
        DayOfWeek dayOfWeek = currentTime.getDayOfWeek();
        LocalTime currentLocalTime = currentTime.toLocalTime();
        
        String openTime = null;
        String closeTime = null;
        
        // 요일별 운영시간 가져오기
        switch (dayOfWeek) {
            case MONDAY:
                openTime = clinic.getDutyTime1s();
                closeTime = clinic.getDutyTime1c();
                break;
            case TUESDAY:
                openTime = clinic.getDutyTime2s();
                closeTime = clinic.getDutyTime2c();
                break;
            case WEDNESDAY:
                openTime = clinic.getDutyTime3s();
                closeTime = clinic.getDutyTime3c();
                break;
            case THURSDAY:
                openTime = clinic.getDutyTime4s();
                closeTime = clinic.getDutyTime4c();
                break;
            case FRIDAY:
                openTime = clinic.getDutyTime5s();
                closeTime = clinic.getDutyTime5c();
                break;
            case SATURDAY:
                openTime = clinic.getDutyTime6s();
                closeTime = clinic.getDutyTime6c();
                break;
            case SUNDAY:
                openTime = clinic.getDutyTime7s();
                closeTime = clinic.getDutyTime7c();
                break;
        }
        
        // 운영시간이 없으면 휴무
        if (openTime == null || closeTime == null || openTime.isEmpty() || closeTime.isEmpty()) {
            return false;
        }
        
        try {
            // 시간 문자열을 LocalTime으로 변환 ("0900" -> 09:00)
            LocalTime openLocalTime = parseTimeString(openTime);
            LocalTime closeLocalTime = parseTimeString(closeTime);
            
            // 24시간 운영인 경우 (예: "0000" - "2400" 또는 같은 시간)
            if (openLocalTime.equals(closeLocalTime) || 
                (openTime.equals("0000") && (closeTime.equals("2400") || closeTime.equals("0000")))) {
                return true;
            }
            
            // 일반적인 경우: 시작 시간 <= 현재 시간 < 종료 시간
            if (closeLocalTime.isAfter(openLocalTime)) {
                // 같은 날 내에서 운영 (예: 09:00 - 18:00)
                return !currentLocalTime.isBefore(openLocalTime) && currentLocalTime.isBefore(closeLocalTime);
            } else {
                // 자정을 넘어 운영 (예: 22:00 - 06:00)
                return !currentLocalTime.isBefore(openLocalTime) || currentLocalTime.isBefore(closeLocalTime);
            }
            
        } catch (Exception e) {
            log.warn("[Clinic] 운영시간 파싱 오류 - 병원: {}, 시간: {} - {}", clinic.getName(), openTime, closeTime);
            return false;
        }
    }
    
    /**
     * 시간 문자열을 LocalTime으로 변환
     * 
     * @param timeString "0900", "1830" 형식의 시간 문자열
     * @return LocalTime 객체
     */
    private LocalTime parseTimeString(String timeString) {
        if (timeString == null || timeString.length() != 4) {
            throw new IllegalArgumentException("Invalid time format: " + timeString);
        }
        
        int hour = Integer.parseInt(timeString.substring(0, 2));
        int minute = Integer.parseInt(timeString.substring(2, 4));
        
        // 2400을 0000으로 처리
        if (hour == 24) {
            hour = 0;
        }
        
        return LocalTime.of(hour, minute);
    }
} 