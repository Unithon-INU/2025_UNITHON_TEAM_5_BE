package com.curelingo.curelingo.gemini.chatbot;

import com.curelingo.curelingo.gemini.chatbot.dto.ChatBotRequest;
import com.curelingo.curelingo.gemini.chatbot.dto.ChatBotResponse;
import com.curelingo.curelingo.gemini.chatbot.ChatBotSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatbot")
public class ChatBotController implements ChatBotSwagger {

    private final ChatBotService chatBotService;

    @PostMapping
    @Override
    public ChatBotResponse chat(@RequestBody ChatBotRequest request) {
        return chatBotService.chat(request);
    }
}