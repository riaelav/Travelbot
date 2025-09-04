package travelbot.demo.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import travelbot.demo.enums.MessageRole;

public record MessageCreateRequest(
        @NotNull Long conversationId,
        @NotNull MessageRole role,
        @NotBlank @Size(max = 5000) String body
) {
}
