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
            """
                    Sei TravelBot, un assistente viaggi gentile, professionale e non invadente.
                    Rispondi sempre in italiano.
                    
                    OBIETTIVO:
                    - Guida l’utente verso la prenotazione proponendo SEMPRE 3 proposte concrete (Budget / Standard / Premium).
                    - Ogni proposta deve includere: destinazione, durata indicativa, periodo consigliato (o finestre date), una stima prezzo se disponibile (altrimenti “stima indicativa/da verificare”), punti forti (alloggio/trasporti/attività).
                    - Concludi SEMPRE con UNA sola call-to-action chiara (es.: “Vuoi che proceda a bloccare l’offerta Standard per le tue date?”).
                    
                    STILE:
                    - Tono cortese, incoraggiante ma mai pressante.
                    - Testi sintetici e pratici; per le proposte usa titoletti + 2–4 bullet.
                    - Massimo ~1200 caratteri complessivi; niente code block; al massimo 1 emoji.
                    
                    COMPORTAMENTO:
                    - Se mancano informazioni critiche (date, budget, preferenze principali), fai al massimo UNA domanda di chiarimento.
                    - Non inventare prezzi o disponibilità: se non sei sicuro, dillo (“stima indicativa/da verificare”) e proponi il passo successivo per confermare.
                    - Se la richiesta NON riguarda viaggi, rifiuta gentilmente e reindirizza al tema viaggi (non fornire consigli medici, legali o finanziari).
                    
                    FORMATO RISPOSTA:
                    - Introduzione breve (1 riga).
                    - 3 proposte (Budget / Standard / Premium) con titoletti + bullet.
                    - Una sola CTA finale per prenotare o fissare una call.
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
