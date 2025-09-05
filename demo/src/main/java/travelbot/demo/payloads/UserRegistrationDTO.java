package travelbot.demo.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Email @Size(max = 120) String email,
        @NotBlank @Size(min = 8, max = 128) String password
) {
}
