package com.curelingo.curelingo.mongodb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController implements HospitalSwagger {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    // 단일 병원 저장
    @PostMapping
    @Override
    public ResponseEntity<String> saveHospital(@RequestBody HospitalDto dto) {
        hospitalService.saveHospital(dto);
        return ResponseEntity.ok("병원 정보가 저장되었습니다.");
    }

    // 전체 병원 자동 저장
    @PostMapping("/mongo")
    public ResponseEntity<String> saveHospitalMongo(@RequestParam(required = false) String qd) {
        try {
            int count = hospitalService.saveHospitalMongo(qd);
            String message = qd != null ? 
                "진료과목 코드 '" + qd + "'로 " + count + "개의 병원 정보가 저장되었습니다." :
                "전체 " + count + "개의 병원 정보가 저장되었습니다.";
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("병원 데이터 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 모든 병원 데이터 삭제
    @DeleteMapping
    @Override
    public ResponseEntity<String> deleteAllHospitals() {
        try {
            hospitalService.deleteAllHospitals();
            return ResponseEntity.ok("모든 병원 데이터가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("병원 데이터 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 중복 데이터 체크
    @GetMapping("/check-duplicates")
    public ResponseEntity<String> checkDuplicateData() {
        try {
            String result = hospitalService.checkDuplicateData();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("중복 데이터 체크 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 중복 데이터 정리
    @PostMapping("/clean-duplicates")
    public ResponseEntity<String> cleanDuplicateData() {
        try {
            String result = hospitalService.cleanDuplicateData();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("중복 데이터 정리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // hpid로 병원 상세정보 조회
    @GetMapping("/{hpid}")
    @Override
    public ResponseEntity<HospitalDto> getHospitalDetail(
            @PathVariable String hpid,
            @RequestParam(required = false) String currentTime
    ) {
        try {
            // 현재 시간 파싱
            LocalDateTime clientTime = null;
            if (currentTime != null && !currentTime.isEmpty()) {
                try {
                    clientTime = LocalDateTime.parse(currentTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } catch (Exception e) {
                    clientTime = LocalDateTime.now();
                }
            } else {
                clientTime = LocalDateTime.now();
            }
            
            HospitalDto hospitalDetail = hospitalService.getHospitalDetailByHpid(hpid, clientTime);
            if (hospitalDetail != null) {
                return ResponseEntity.ok(hospitalDetail);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
