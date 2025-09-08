package travelbot.demo.payloads;

import jakarta.validation.constraints.NotNull;
import travelbot.demo.enums.BookingStatus;

public record BookingUpdateStatusRequest(
        @NotNull(message = "status is required") BookingStatus status
) {
}
