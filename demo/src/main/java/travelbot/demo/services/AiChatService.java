package travelbot.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import travelbot.demo.entities.Conversation;
import travelbot.demo.entities.Message;
import travelbot.demo.enums.MessageRole;
import travelbot.demo.payloads.ChatMessage;
import travelbot.demo.payloads.ChatRequest;
import travelbot.demo.payloads.ChatResponse;
import travelbot.demo.repositories.MessageRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class AiChatService {

    // modello
    private static final String MODEL = "gpt-4o-mini";
    // prompt base
    private static final String SYSTEM_PROMPT =
            "You are TravelBot, a helpful travel assistant. Reply concisely and ask at most one clarifying question if needed.";
    @Autowired
    private WebClient openAiClient;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private MessageRepository messageRepository;

    public String reply(Long conversationId, String userText) {
        Conversation conv = conversationService.findById(conversationId);
        var history = messageRepository.findAllByConversationIdOrderByCreatedAtAsc(conv.getId());

        // prendi solo gli ultimi N messaggi per contenere i costi
        int keep = 12; // ~6 turni
        int from = Math.max(0, history.size() - keep);

        List<ChatMessage> msgs = new ArrayList<>();
        msgs.add(new ChatMessage("system", SYSTEM_PROMPT));
        history.subList(from, history.size()).forEach(m ->
                msgs.add(new ChatMessage(
                        m.getRole() == MessageRole.USER ? "user" : "assistant",
                        m.getBody()
                ))
        );
        msgs.add(new ChatMessage("user", userText));

        ChatRequest req = new ChatRequest(MODEL, msgs);

        ChatResponse res = openAiClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .timeout(Duration.ofSeconds(15))
                .onErrorResume(err -> Mono.error(new RuntimeException("OpenAI error: " + err.getMessage(), err)))
                .block();

        String aiText = (res != null && res.choices() != null && !res.choices().isEmpty())
                ? res.choices().get(0).message().content()
                : "Sorry, I couldn’t generate a reply. Please try again.";

        // salva il messaggio assistant
        messageRepository.save(new Message(conv, MessageRole.ASSISTANT, aiText));
        return aiText;
    }
}
