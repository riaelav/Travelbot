package travelbot.demo.payloads;

import jakarta.validation.constraints.NotNull;

public record ConversationCreateRequest(
        @NotNull Long customerId
) {
}
