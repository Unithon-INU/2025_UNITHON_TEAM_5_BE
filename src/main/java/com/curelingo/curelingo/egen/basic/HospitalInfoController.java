package com.curelingo.curelingo.egen.basic;

import com.curelingo.curelingo.egen.model.EgenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/egen/info")
public class HospitalInfoController {

    private final HospitalInfoService service;

    public HospitalInfoController(HospitalInfoService service) {
        this.service = service;
    }

    @GetMapping
    public EgenResponse<HospitalInfoItem> getHospitalInfo(
            @RequestParam String hpid
    )throws Exception {
        return service.fetchHospitalInfo(hpid);
    }
}
