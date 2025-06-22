package com.curelingo.curelingo.emergencyhospital;

import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceRequest;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceResponse;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyBedStatus;
import com.curelingo.curelingo.emergencyhospital.dto.NearbyHospitalDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/emergency")
@RequiredArgsConstructor
public class EmergencyAdvisorController implements EmergencyAdvisorSwagger {

    private final EmergencyAdvisorService emergencyAdvisorService;

    @PostMapping("/suggestion")
    public ResponseEntity<EmergencyAdviceResponse> recommend(@RequestBody EmergencyAdviceRequest request) {
        EmergencyAdviceResponse response = emergencyAdvisorService.getRecommendedHospital(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyHospitalDto>> getNearbyHospitals(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radiusKm
    ) {
        List<NearbyHospitalDto> hospitals = emergencyAdvisorService.findNearbyERs(lat, lng, radiusKm);
        return ResponseEntity.ok(hospitals);
    }

    @GetMapping("/beds")
    public List<EmergencyBedStatus> getNearbyEmergencyBedStatus(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radiusKm
    ) {
        return emergencyAdvisorService.findNearbyEmergencyBeds(lat, lng, radiusKm);
    }
}
