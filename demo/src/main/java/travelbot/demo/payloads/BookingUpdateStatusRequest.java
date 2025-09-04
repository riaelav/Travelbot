package travelbot.demo.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import travelbot.demo.enums.BookingStatus;

public record BookingUpdateStatusRequest(
        @NotBlank
        @NotNull BookingStatus status

) {
}
