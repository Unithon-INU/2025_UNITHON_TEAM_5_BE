package com.curelingo.curelingo.mongodb;

import com.curelingo.curelingo.mongodb.dto.HospitalBasicDto;
import com.curelingo.curelingo.mongodb.dto.HospitalDto;
import com.curelingo.curelingo.mongodb.dto.HospitalRequestDto;
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
    public ResponseEntity<HospitalDto> saveHospital(@RequestBody HospitalRequestDto requestDto) {
        try {
            HospitalDto savedHospital = hospitalService.saveHospital(requestDto);
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

    // hpid로 병원명과 주소 조회
    @GetMapping("/{hpid}")
    @Override
    public ResponseEntity<HospitalBasicDto> getHospitalBasicInfo(
            @PathVariable String hpid,
            @RequestParam(defaultValue = "ko") String language
    ) {
        try {
            System.out.println("컨트롤러 - 요청 받음: hpid=" + hpid + ", language=" + language);
            
            HospitalBasicDto hospital = hospitalService.getHospitalBasicInfo(hpid, language);
            if (hospital == null) {
                System.out.println("컨트롤러 - 병원 없음, 404 반환");
                return ResponseEntity.notFound().build();
            }
            
            System.out.println("컨트롤러 - 성공적으로 응답 반환");
            return ResponseEntity.ok(hospital);
        } catch (Exception e) {
            System.err.println("컨트롤러 에러: " + e.getMessage());
            e.printStackTrace();
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
