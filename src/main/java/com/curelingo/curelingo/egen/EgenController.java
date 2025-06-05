package com.curelingo.curelingo.egen;

import com.curelingo.curelingo.egen.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/egen")
@RequiredArgsConstructor
public class EgenController implements EgenSwagger {

    private final EgenService egenService;

    @GetMapping("/emergency/nearby")
    public EgenResponse<NearbyHospitalItem> getNearbyHospitals(@RequestParam double lat, @RequestParam double lon) {
        return egenService.getNearbyHospitals(lat, lon);
    }

    @GetMapping("/emergency/info")
    public EgenResponse<HospitalInfoItem> getHospitalInfo(@RequestParam String hpid) {
        return egenService.getHospitalInfo(hpid);
    }

    @GetMapping("/emergency/beds")
    public EgenResponse<AvailableBedsItem> getAvailableBeds(
            @RequestParam String stage1,
            @RequestParam String stage2
    ) {
        return egenService.getAvailableBeds(stage1, stage2);
    }

    @GetMapping("/clinics")
    public EgenResponse<ClinicItem> getClinics(
            @RequestParam(required = false) String Q0,
            @RequestParam(required = false) String Q1,
            @RequestParam(required = false) String QZ,
            @RequestParam(required = false) String QD,
            @RequestParam(required = false) String QT,
            @RequestParam(required = false) String QN,
            @RequestParam(required = false) String ORD,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer numOfRows
    ) {
        return egenService.getClinics(Q0, Q1, QZ, QD, QT, QN, ORD, pageNo, numOfRows);
    }

    @GetMapping("/clinic/info")
    public EgenResponse<HospitalInfoItem> getClinicInfo(@RequestParam String hpid) {
        return egenService.getClinicInfo(hpid);
    }

    @GetMapping("/clinic/nearby")
    public EgenResponse<NearbyHospitalItem> getNearbyClinics(@RequestParam double lat, @RequestParam double lon) {
        return egenService.getNearbyClinics(lat, lon);
    }
}
