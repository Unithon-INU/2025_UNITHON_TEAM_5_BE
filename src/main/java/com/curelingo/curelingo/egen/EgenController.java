package com.curelingo.curelingo.egen;

import com.curelingo.curelingo.egen.dto.EgenResponse;
import com.curelingo.curelingo.egen.dto.NearbyHospitalItem;
import com.curelingo.curelingo.egen.dto.HospitalInfoItem;
import com.curelingo.curelingo.egen.dto.AvailableBedsItem;
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
}
