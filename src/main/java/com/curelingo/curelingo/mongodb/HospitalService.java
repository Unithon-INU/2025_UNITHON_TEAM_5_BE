package com.curelingo.curelingo.mongodb;

import com.curelingo.curelingo.egen.dto.ClinicItem;
import com.curelingo.curelingo.egen.EgenService;
import com.curelingo.curelingo.egen.dto.EgenResponse;
import com.curelingo.curelingo.mongodb.repository.HospitalRepository;
import com.curelingo.curelingo.translation.TranslationService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final EgenService egenService;
    private final TranslationService translationService;

    public HospitalService(HospitalRepository hospitalRepository, EgenService egenService, TranslationService translationService) {
        this.hospitalRepository = hospitalRepository;
        this.egenService = egenService;
        this.translationService = translationService;
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

        // 영문 번역
        String dutyNameEn = translationService.translateToEnglish(dto.getDutyName());
        String dutyAddrEn = translationService.translateToEnglish(dto.getDutyAddr());

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
                .dutyNameEn(dutyNameEn)
                .dutyAddrEn(dutyAddrEn)
                .build();

        hospitalRepository.save(mongoHospital);
    }

    public int saveHospitalMongo(String qd) {
        // QD 파라미터가 없으면 모든 진료과(D001~D032) 자동 수집
        if (qd == null || qd.trim().isEmpty()) {
            return saveAllDepartments();
        }
        
        // 특정 진료과만 저장
        return saveSingleDepartment(qd);
    }
    
    /**
     * 모든 진료과(D001~D032)를 자동으로 순회하면서 데이터 저장
     */
    private int saveAllDepartments() {
        int totalSavedCount = 0;
        System.out.println("=== 모든 진료과(D001~D032) 자동 수집 시작 ===");
        
        for (int i = 1; i <= 32; i++) {
            String departmentCode = String.format("D%03d", i); // D001, D002, ..., D032
            
            // 진료과 수집 전 현재 상태 확인
            long beforeCount = hospitalRepository.count();
            System.out.println("\n--- " + departmentCode + " 진료과 수집 시작 (현재 총 병원 수: " + beforeCount + "개) ---");
            
            try {
                int count = saveSingleDepartment(departmentCode);
                totalSavedCount += count;
                
                long afterCount = hospitalRepository.count();
                System.out.println("--- " + departmentCode + " 진료과 완료: " + count + "개 새 병원, 총 " + afterCount + "개 (+" + (afterCount - beforeCount) + ") ---");
                
                // 진료과 간 간격 (API 부하 방지)
                Thread.sleep(1000);
                
            } catch (Exception e) {
                System.err.println(departmentCode + " 진료과 처리 중 오류: " + e.getMessage());
                e.printStackTrace();
                // 오류가 있어도 다음 진료과는 계속 처리
            }
        }
        
        System.out.println("\n=== 모든 진료과 수집 완료: 총 " + totalSavedCount + "개 병원 저장 ===");
        return totalSavedCount;
    }
    
    /**
     * 특정 진료과만 저장
     */
    private int saveSingleDepartment(String qd) {
        int totalSavedCount = 0;
        int pageNo = 1;
        int numOfRows = 1000; // 한 번에 1000개씩 가져오기
        boolean hasMoreData = true;

        System.out.println("진료과목 코드: " + qd + "로 병원 데이터 수집 시작");

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

                System.out.println("페이지 " + pageNo + ": " + items.size() + "개 데이터 처리 중...");

                int pageSavedCount = 0;
                int pageNewCount = 0;
                int pageUpdatedCount = 0;
                int pageSkippedCount = 0;
                
                for (ClinicItem item : items) {
                    // 필수 필드 검증
                    if (item.getHpid() == null || item.getHpid().trim().isEmpty() || "null".equals(item.getHpid())) {
                        continue; // hpid가 null이거나 빈 값이면 건너뛰기
                    }

                    String hpid = item.getHpid();
                    String hospitalName = safeString(item.getDutyName());
                    
                    // 기존 병원 데이터 조회
                    MongoHospital existing = hospitalRepository.findById(hpid).orElse(null);
                    
                    // 디버깅: 처음 5개 병원에 대해 상세 로그
                    if ((pageNewCount + pageUpdatedCount + pageSkippedCount) < 5) {
                        System.out.println("  [DEBUG] hpid: " + hpid + ", 병원명: " + hospitalName);
                        System.out.println("  [DEBUG] 기존 병원 존재: " + (existing != null));
                        if (existing != null) {
                            System.out.println("  [DEBUG] 기존 진료과: " + existing.getDepartments());
                            System.out.println("  [DEBUG] 현재 진료과 " + qd + " 포함 여부: " + 
                                (existing.getDepartments() != null && existing.getDepartments().contains(qd)));
                        }
                    }
                    
                    if (existing != null) {
                        // 기존 병원이 있는 경우 - 진료과 체크
                        List<String> existingDepts = existing.getDepartments();
                        if (existingDepts != null && existingDepts.contains(qd)) {
                            // 이미 해당 진료과가 있음
                            pageSkippedCount++;
                            if (pageSkippedCount <= 3) { // 처음 3개만 로그 출력
                                System.out.println("  [SKIP] " + hospitalName + " (hpid:" + hpid + ") - 진료과 " + qd + " 이미 존재");
                            }
                            continue;
                        } else {
                            // 기존 병원에 새 진료과 추가
                            if (pageUpdatedCount <= 3) { // 처음 3개만 로그 출력
                                System.out.println("  [UPDATE] " + hospitalName + " (hpid:" + hpid + ") - 진료과 " + qd + " 추가 (기존: " + 
                                    (existingDepts != null ? existingDepts : "[]") + ")");
                            }
                        }
                    } else {
                        // 새로운 병원
                        if (pageNewCount <= 3) { // 처음 3개만 로그 출력
                            System.out.println("  [NEW] " + hospitalName + " (hpid:" + hpid + ") - 진료과 " + qd + " 신규 저장");
                        }
                    }
                    
                    List<String> departments = new ArrayList<>();
                    if (existing != null && existing.getDepartments() != null) {
                        // 기존 진료과 목록 복사
                        departments.addAll(existing.getDepartments());
                    }
                    
                    // 현재 요청한 진료과 추가 (이미 위에서 중복 체크했으므로 바로 추가)
                    if (qd != null && !qd.trim().isEmpty()) {
                        departments.add(qd);
                    }

                    // 영문 번역 (기존 데이터가 있으면 기존 영문 필드 사용, 없으면 번역)
                    String dutyNameEn, dutyAddrEn;
                    if (existing != null) {
                        dutyNameEn = existing.getDutyNameEn();
                        dutyAddrEn = existing.getDutyAddrEn();
                    } else {
                        String originalName = safeString(item.getDutyName());
                        String originalAddr = safeString(item.getDutyAddr());
                        
                        System.out.println("  [번역] 병원명: '" + originalName + "' -> 번역 시작");
                        System.out.println("  [번역] 주소: '" + originalAddr + "' -> 번역 시작");
                        
                        dutyNameEn = translationService.translateToEnglish(originalName);
                        dutyAddrEn = translationService.translateToEnglish(originalAddr);
                        
                        System.out.println("  [번역] 병원명 결과: '" + originalName + "' -> '" + dutyNameEn + "'");
                        System.out.println("  [번역] 주소 결과: '" + originalAddr + "' -> '" + dutyAddrEn + "'");
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
                            .dutyNameEn(dutyNameEn)
                            .dutyAddrEn(dutyAddrEn)
                            .build();

                    // 저장 전 최종 검증
                    try {
                        MongoHospital saved = hospitalRepository.save(mongoHospital);
                        
                        if (existing == null) {
                            pageSavedCount++; // 새로 저장된 병원만 카운트
                            pageNewCount++;
                        } else {
                            pageUpdatedCount++;
                        }
                        
                        // 저장 후 검증 (처음 몇 개만)
                        if ((pageNewCount + pageUpdatedCount) <= 3) {
                            MongoHospital verification = hospitalRepository.findById(hpid).orElse(null);
                            if (verification != null) {
                                System.out.println("    저장 확인: " + verification.getDutyName() + " - 진료과: " + verification.getDepartments());
                            }
                        }
                        
                    } catch (Exception e) {
                        System.err.println("  [ERROR] 저장 실패 - " + hospitalName + " (hpid:" + hpid + "): " + e.getMessage());
                    }
                }

                totalSavedCount += pageSavedCount;
                System.out.println("페이지 " + pageNo + " 처리 완료 - 새로운 병원: " + pageNewCount + "개, 진료과 추가: " + pageUpdatedCount + "개, 스킵: " + pageSkippedCount + "개");

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

        // 진료과 수집 후 전체 통계
        long afterCount = hospitalRepository.count();
        long hospitalsWith_qd = hospitalRepository.findAll().stream()
                .filter(h -> h.getDepartments() != null && h.getDepartments().contains(qd))
                .count();
        
        System.out.println("\n=== 진료과 " + qd + " 수집 완료 ===");
        System.out.println("- 총 처리 페이지: " + (pageNo - 1) + "개");
        System.out.println("- 새로운 병원: " + totalSavedCount + "개");
        System.out.println("- 현재 DB 총 병원 수: " + afterCount + "개");
        System.out.println("- " + qd + " 진료과를 가진 병원 수: " + hospitalsWith_qd + "개");
        
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

    /**
     * 중복 데이터 체크 및 통계 조회
     */
    public String checkDuplicateData() {
        StringBuilder report = new StringBuilder();
        
        // 전체 병원 수
        long totalCount = hospitalRepository.count();
        report.append("=== 병원 데이터 중복 체크 결과 ===\n");
        report.append("전체 병원 수: ").append(totalCount).append("개\n\n");
        
        // 모든 병원 데이터 조회
        List<MongoHospital> allHospitals = hospitalRepository.findAll();
        
        // hpid 중복 체크
        Set<String> uniqueHpids = new HashSet<>();
        Map<String, Integer> hpidCounts = new HashMap<>();
        
        for (MongoHospital hospital : allHospitals) {
            String hpid = hospital.getHpid();
            uniqueHpids.add(hpid);
            hpidCounts.put(hpid, hpidCounts.getOrDefault(hpid, 0) + 1);
        }
        
        report.append("고유한 hpid 수: ").append(uniqueHpids.size()).append("개\n");
        
        // 중복된 hpid 찾기
        List<String> duplicateHpids = hpidCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        if (!duplicateHpids.isEmpty()) {
            report.append("⚠️ 중복된 hpid 발견: ").append(duplicateHpids.size()).append("개\n");
            for (String hpid : duplicateHpids.subList(0, Math.min(10, duplicateHpids.size()))) {
                report.append("  - ").append(hpid).append(" (").append(hpidCounts.get(hpid)).append("번 중복)\n");
            }
            if (duplicateHpids.size() > 10) {
                report.append("  ... 및 ").append(duplicateHpids.size() - 10).append("개 더\n");
            }
        } else {
            report.append("✅ hpid 중복 없음\n");
        }
        
        // 병원명 중복 체크
        Map<String, Integer> nameCounts = new HashMap<>();
        for (MongoHospital hospital : allHospitals) {
            String name = hospital.getDutyName();
            if (name != null) {
                nameCounts.put(name, nameCounts.getOrDefault(name, 0) + 1);
            }
        }
        
        List<String> duplicateNames = nameCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        report.append("\n병원명 중복 현황:\n");
        report.append("고유한 병원명 수: ").append(nameCounts.size()).append("개\n");
        if (!duplicateNames.isEmpty()) {
            report.append("중복된 병원명: ").append(duplicateNames.size()).append("개\n");
            for (String name : duplicateNames.subList(0, Math.min(5, duplicateNames.size()))) {
                report.append("  - ").append(name).append(" (").append(nameCounts.get(name)).append("개)\n");
            }
        }
        
        // 진료과 통계
        Map<String, Integer> departmentCounts = new HashMap<>();
        for (MongoHospital hospital : allHospitals) {
            if (hospital.getDepartments() != null) {
                for (String dept : hospital.getDepartments()) {
                    departmentCounts.put(dept, departmentCounts.getOrDefault(dept, 0) + 1);
                }
            }
        }
        
        report.append("\n진료과 통계:\n");
        report.append("진료과 종류: ").append(departmentCounts.size()).append("개\n");
        departmentCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(entry -> report.append("  - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("개 병원\n"));
        
        String result = report.toString();
        System.out.println(result);
        return result;
    }

    /**
     * 중복된 hpid 데이터 정리 (첫 번째만 남기고 나머지 삭제)
     */
    /**
     * hpid 기준으로 병원 상세정보 조회
     *
     * @param hpid 병원 고유 ID
     * @return 병원 상세정보 (HospitalDto)
     */
    public HospitalDto getHospitalDetailByHpid(String hpid) {
        MongoHospital mongoHospital = hospitalRepository.findById(hpid).orElse(null);
        
        if (mongoHospital == null) {
            return null;
        }
        
        return HospitalDto.builder()
                .hpid(mongoHospital.getHpid())
                .dutyName(mongoHospital.getDutyName())
                .dutyAddr(mongoHospital.getDutyAddr())
                .dutyDivNam(mongoHospital.getDutyDivNam())
                .departments(mongoHospital.getDepartments())
                .dutyEryn(mongoHospital.getDutyEryn())
                .dutyTel1(mongoHospital.getDutyTel1())
                .dutyTel3(mongoHospital.getDutyTel3())
                .dutyEtc(mongoHospital.getDutyEtc())
                .dutyNameEn(mongoHospital.getDutyNameEn())
                .dutyAddrEn(mongoHospital.getDutyAddrEn())
                .dutyTime1s(mongoHospital.getDutyTime1s())
                .dutyTime1c(mongoHospital.getDutyTime1c())
                .dutyTime2s(mongoHospital.getDutyTime2s())
                .dutyTime2c(mongoHospital.getDutyTime2c())
                .dutyTime3s(mongoHospital.getDutyTime3s())
                .dutyTime3c(mongoHospital.getDutyTime3c())
                .dutyTime4s(mongoHospital.getDutyTime4s())
                .dutyTime4c(mongoHospital.getDutyTime4c())
                .dutyTime5s(mongoHospital.getDutyTime5s())
                .dutyTime5c(mongoHospital.getDutyTime5c())
                .dutyTime6s(mongoHospital.getDutyTime6s())
                .dutyTime6c(mongoHospital.getDutyTime6c())
                .dutyTime7s(mongoHospital.getDutyTime7s())
                .dutyTime7c(mongoHospital.getDutyTime7c())
                .dutyTime8s(mongoHospital.getDutyTime8s())
                .dutyTime8c(mongoHospital.getDutyTime8c())
                .wgs84Lat(mongoHospital.getWgs84Lat())
                .wgs84Lon(mongoHospital.getWgs84Lon())
                .rnum(mongoHospital.getRnum())
                .build();
    }

    public String cleanDuplicateData() {
        List<MongoHospital> allHospitals = hospitalRepository.findAll();
        Map<String, List<MongoHospital>> groupedByHpid = allHospitals.stream()
                .collect(Collectors.groupingBy(MongoHospital::getHpid));
        
        int deletedCount = 0;
        int mergedCount = 0;
        
        for (Map.Entry<String, List<MongoHospital>> entry : groupedByHpid.entrySet()) {
            List<MongoHospital> hospitals = entry.getValue();
            
            if (hospitals.size() > 1) {
                // 첫 번째 병원을 기준으로 진료과 병합
                MongoHospital primary = hospitals.get(0);
                Set<String> allDepartments = new HashSet<>();
                
                for (MongoHospital hospital : hospitals) {
                    if (hospital.getDepartments() != null) {
                        allDepartments.addAll(hospital.getDepartments());
                    }
                }
                
                // 병합된 데이터로 업데이트
                MongoHospital merged = MongoHospital.builder()
                        .hpid(primary.getHpid())
                        .dutyName(primary.getDutyName())
                        .dutyAddr(primary.getDutyAddr())
                        .dutyDivNam(primary.getDutyDivNam())
                        .dutyEryn(primary.getDutyEryn())
                        .dutyTel1(primary.getDutyTel1())
                        .dutyTel3(primary.getDutyTel3())
                        .dutyEtc(primary.getDutyEtc())
                        .dutyTime1s(primary.getDutyTime1s())
                        .dutyTime1c(primary.getDutyTime1c())
                        .dutyTime2s(primary.getDutyTime2s())
                        .dutyTime2c(primary.getDutyTime2c())
                        .dutyTime3s(primary.getDutyTime3s())
                        .dutyTime3c(primary.getDutyTime3c())
                        .dutyTime4s(primary.getDutyTime4s())
                        .dutyTime4c(primary.getDutyTime4c())
                        .dutyTime5s(primary.getDutyTime5s())
                        .dutyTime5c(primary.getDutyTime5c())
                        .dutyTime6s(primary.getDutyTime6s())
                        .dutyTime6c(primary.getDutyTime6c())
                        .dutyTime7s(primary.getDutyTime7s())
                        .dutyTime7c(primary.getDutyTime7c())
                        .dutyTime8s(primary.getDutyTime8s())
                        .dutyTime8c(primary.getDutyTime8c())
                        .wgs84Lat(primary.getWgs84Lat())
                        .wgs84Lon(primary.getWgs84Lon())
                        .rnum(primary.getRnum())
                        .departments(new ArrayList<>(allDepartments))
                        .build();
                
                // 중복 데이터 삭제
                for (int i = 1; i < hospitals.size(); i++) {
                    hospitalRepository.delete(hospitals.get(i));
                    deletedCount++;
                }
                
                // 병합된 데이터 저장
                hospitalRepository.save(merged);
                mergedCount++;
            }
        }
        
        String result = String.format("중복 데이터 정리 완료: %d개 중복 레코드 삭제, %d개 병원 데이터 병합", 
                deletedCount, mergedCount);
        System.out.println(result);
        return result;
    }
}
