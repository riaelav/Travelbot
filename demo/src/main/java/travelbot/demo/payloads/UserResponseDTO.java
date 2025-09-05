package travelbot.demo.payloads;

public record UserResponseDTO(
        Long id,
        String username,
        String email
) {
}
