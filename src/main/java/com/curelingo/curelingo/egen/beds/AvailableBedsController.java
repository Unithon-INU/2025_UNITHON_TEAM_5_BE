package com.curelingo.curelingo.egen.beds;

import com.curelingo.curelingo.egen.model.EgenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/egen/beds")
@RestController
public class AvailableBedsController {

    private final AvailableBedsService service;

    public AvailableBedsController(AvailableBedsService service) {
        this.service = service;
    }

    @GetMapping
    public EgenResponse<AvailableBedsItem> getAvailableBeds(
            @RequestParam String stage1,
            @RequestParam String stage2
    ) {
        return service.fetchAvailableBeds(stage1, stage2);
    }
}
