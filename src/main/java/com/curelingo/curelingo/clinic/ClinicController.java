package com.curelingo.curelingo.clinic;

import com.curelingo.curelingo.clinic.dto.NearbyClinicDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/clinic")
@RequiredArgsConstructor
public class ClinicController implements ClinicSwagger {

    private final ClinicService clinicService;



    @GetMapping("/department")
    public ResponseEntity<List<Map<String, Object>>> getNearbyClinicsByDepartment(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam String department,
            @RequestParam(defaultValue = "ko") String language
    ) {
        log.info("[ClinicController] 진료과별 병원 검색 요청 - 위치: ({}, {}), 반경: 3km, 진료과: {}, 언어: {}", lat, lng, department, language);
        List<Map<String, Object>> clinics = clinicService.findNearbyClinicsByDepartment(lat, lng, department, language);
        log.info("[ClinicController] 검색 결과: {}개 {} 진료과 병원", clinics.size(), department);
        return ResponseEntity.ok(clinics);
    }


} 