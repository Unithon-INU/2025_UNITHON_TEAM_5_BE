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
    public EgenResponse<NearbyHospitalItem> getNearbyHospitals(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer numOfRows
    ) {
        return egenService.getNearbyHospitals(lat, lon, pageNo, numOfRows);
    }

    @GetMapping("/emergency/info")
    public EgenResponse<HospitalInfoItem> getHospitalInfo(
            @RequestParam String hpid,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer numOfRows
    ) {
        return egenService.getHospitalInfo(hpid, pageNo, numOfRows);
    }

    @GetMapping("/emergency/beds")
    public EgenResponse<AvailableBedsItem> getAvailableBeds(
            @RequestParam(required = false) String stage1,
            @RequestParam(required = false) String stage2,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer numOfRows
    ) {
        return egenService.getAvailableBeds(stage1, stage2, pageNo, numOfRows);
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

    @GetMapping("/clinic/nearby")
    public EgenResponse<NearbyHospitalItem> getNearbyClinics(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer numOfRows
    ) {
        return egenService.getNearbyClinics(lat, lon, pageNo, numOfRows);
    }

    @GetMapping("/clinic/info")
    public EgenResponse<HospitalInfoItem> getClinicInfo(
            @RequestParam String hpid,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer numOfRows
    ) {
        return egenService.getClinicInfo(hpid, pageNo, numOfRows);
    }

    @GetMapping("/clinic/fulldata")
    public EgenResponse<HospitalFullInfoItem> getFullData(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer numOfRows
    ) {
        return egenService.getFullData(pageNo, numOfRows);
    }
}
