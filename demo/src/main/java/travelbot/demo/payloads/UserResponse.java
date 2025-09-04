package travelbot.demo.payloads;

public record UserResponse(
        Long id,
        String username,
        String email
) {
}
