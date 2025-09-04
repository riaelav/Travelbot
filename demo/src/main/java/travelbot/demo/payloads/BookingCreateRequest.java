package travelbot.demo.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookingCreateRequest(
        @NotNull Long customerId,
        Long conversationId,
        @NotBlank @Size(max = 10000) String productSummary,
        @NotNull @Min(0) Integer priceCents
) {
}
