// payload/auth/JwtResponse.java
package travelbot.demo.payloads;

public record JwtResponse(
        String token,
        long expiresAtEpochSeconds
) {
}
