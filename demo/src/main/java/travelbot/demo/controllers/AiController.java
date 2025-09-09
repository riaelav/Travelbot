package travelbot.demo.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import travelbot.demo.services.AiChatService;

@RestController
@RequestMapping("/ai")
@Validated
public class AiController {

    @Autowired
    private AiChatService aiChatService;

    @PostMapping("/reply")
    @ResponseStatus(HttpStatus.OK)
    public ChatResponseDto reply(@RequestBody @Validated ChatRequestDto body) {
        String reply = aiChatService.reply(body.conversationId(), body.text());
        return new ChatResponseDto(reply);
    }

    public record ChatRequestDto(@NotNull Long conversationId, @NotBlank String text) {
    }

    public record ChatResponseDto(String reply) {
    }
}
