package com.curelingo.curelingo.clinic;

import com.curelingo.curelingo.clinic.dto.NearbyClinicDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clinic")
@RequiredArgsConstructor
public class ClinicController implements ClinicSwagger {

    private final ClinicService clinicService;



    @GetMapping("/nearby/department")
    public ResponseEntity<List<NearbyClinicDto>> getNearbyClinicsByDepartment(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam String department
    ) {
        log.info("[ClinicController] 진료과별 병원 검색 요청 - 위치: ({}, {}), 반경: 3km, 진료과: {}", lat, lng, department);
        List<NearbyClinicDto> clinics = clinicService.findNearbyClinicsByDepartment(lat, lng, department);
        log.info("[ClinicController] 검색 결과: {}개 {} 진료과 병원", clinics.size(), department);
        return ResponseEntity.ok(clinics);
    }


} 