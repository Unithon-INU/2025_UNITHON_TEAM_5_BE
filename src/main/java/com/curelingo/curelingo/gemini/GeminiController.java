package com.curelingo.curelingo.gemini;

import com.curelingo.curelingo.gemini.chatbot.dto.ChatBotRequest;
import com.curelingo.curelingo.gemini.chatbot.dto.ChatBotResponse;
import com.curelingo.curelingo.gemini.chatbot.ChatBotService;
import com.curelingo.curelingo.gemini.emergencyadvisor.GeminiEmergencyAdvisorService;
import com.curelingo.curelingo.gemini.emergencyadvisor.GeminiEmergencyRecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gemini")
public class GeminiController implements GeminiSwagger {

    private final ChatBotService chatBotService;
    private final GeminiEmergencyAdvisorService emergencyAdvisorService;

    @PostMapping("/chatbot")
    public ChatBotResponse chat(@RequestBody ChatBotRequest request) {
        return chatBotService.chat(request);
    }

    @GetMapping("/recommend-emergency")
    public GeminiEmergencyRecommendationResponse recommendEmergency(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radiusKm,
            @RequestParam(defaultValue = "ko") String language
    ) {
        return emergencyAdvisorService.recommendNearbyEmergency(lat, lng, radiusKm, language);
    }
}