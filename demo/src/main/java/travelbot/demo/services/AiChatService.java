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
    private static final String SYSTEM_PROMPT = """
            Sei TravelBot: assistente viaggi gentile e professionale. Rispondi in italiano.
            Fornisci sempre 3 proposte (Budget / Standard / Premium), ciascuna con: destinazione, durata, periodo consigliato, stima prezzo (o “stima indicativa/da verificare”) e 2–3 punti forti.
            Stile: sintetico, pratico, non invadente; titoletti + bullet; max ~1200 caratteri; niente code block; ≤1 emoji.
            Chiedi al massimo UNA chiarificazione se mancano dati essenziali (date/budget/preferenze). Non inventare prezzi/disponibilità. Se richiesta off-topic, rifiuta gentilmente e reindirizza ai viaggi.
            Chiudi sempre con UNA sola CTA. Se l’utente ha espresso un budget, aggiungi in coda: "Prenota qui: https://tuodominio.it/prenota".
            """;


    @Autowired
    private WebClient openAiClient;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private MessageRepository messageRepository;

    public String reply(Long conversationId, String userText) {
        Conversation conv = conversationService.findById(conversationId);
        var history = messageRepository.findAllByConversationIdOrderByCreatedAtAsc(conv.getId());


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
