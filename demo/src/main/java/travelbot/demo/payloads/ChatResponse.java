package travelbot.demo.payloads;

import java.util.List;

public record ChatResponse(List<ChatChoice> choices) {
}
