package com.curelingo.curelingo.egen.nearby;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/egen/nearby")
@RequiredArgsConstructor
public class NearbyHospitalController {

    private final NearbyHospitalService nearbyHospitalService;

    @GetMapping
    public List<NearbyHospitalItem> getNearbyHospitals(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        return nearbyHospitalService.getNearbyHospitals(lat, lon);
    }
}
