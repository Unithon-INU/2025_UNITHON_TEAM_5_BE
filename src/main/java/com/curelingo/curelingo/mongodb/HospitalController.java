package com.curelingo.curelingo.mongodb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<HospitalDto> saveHospital(@RequestBody HospitalDto dto) {
        try {
            HospitalDto savedHospital = hospitalService.saveHospital(dto);
            return ResponseEntity.ok(savedHospital);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // 전체 병원 자동 저장
    @PostMapping("/mongo")
    public ResponseEntity<String> saveHospitalMongo() {
        try {
            int count = hospitalService.saveHospitalMongo();
            return ResponseEntity.ok("성공적으로 " + count + "개의 병원 정보가 저장되었습니다.");
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

    @GetMapping
    @Override
    public ResponseEntity<List<HospitalDto>> getAllHospitals(
            @RequestParam(defaultValue = "ko") String language
    ) {
        try {
        List<HospitalDto> hospitals = hospitalService.getAllHospitals(language);
            return ResponseEntity.ok(hospitals);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/emergency")
    @Override
    public ResponseEntity<List<HospitalDto>> getEmergencyHospitals(
            @RequestParam(defaultValue = "ko") String language
    ) {
        try {
            List<HospitalDto> hospitals = hospitalService.getEmergencyHospitals(language);
            return ResponseEntity.ok(hospitals);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
