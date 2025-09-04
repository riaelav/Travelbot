// payload/auth/LoginRequest.java
package travelbot.demo.payloads;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
