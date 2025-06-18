package com.curelingo.curelingo.mongodb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController implements HospitalSwagger {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping
    @Override
    public ResponseEntity<String> saveHospital(@RequestBody HospitalDto dto) {
        hospitalService.saveHospital(dto);
        return ResponseEntity.ok("병원 정보가 저장되었습니다.");
    }
}
