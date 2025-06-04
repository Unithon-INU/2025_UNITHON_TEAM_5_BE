package com.curelingo.curelingo.emergencyhospital;

import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceRequest;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

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
}
