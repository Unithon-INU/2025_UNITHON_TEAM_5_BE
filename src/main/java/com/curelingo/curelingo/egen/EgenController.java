package com.curelingo.curelingo.egen;

import com.curelingo.curelingo.egen.model.EgenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/egen")
@RestController
public class EgenController {

    private final EgenService egenService;

    public EgenController(EgenService egenService) {
        this.egenService = egenService;
    }

    @GetMapping("")
    public EgenResponse getEgen(
            @RequestParam String stage1,
            @RequestParam String stage2
    ) {
        return egenService.fetchEgenData(stage1, stage2);
    }
}
