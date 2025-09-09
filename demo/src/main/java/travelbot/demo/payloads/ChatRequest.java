package travelbot.demo.payloads;

import java.util.List;

public record ChatRequest(String model, List<ChatMessage> messages) {
}