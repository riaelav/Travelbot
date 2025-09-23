package travelbot.demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import travelbot.demo.entities.Conversation;
import travelbot.demo.entities.Customer;
import travelbot.demo.entities.Message;
import travelbot.demo.enums.LeadValue;
import travelbot.demo.enums.MessageRole;
import travelbot.demo.repositories.ConversationRepository;
import travelbot.demo.repositories.CustomerRepository;
import travelbot.demo.repositories.MessageRepository;
import travelbot.demo.services.AiChatService;
import travelbot.demo.services.ConversationService;
import travelbot.demo.services.KeywordLeadService;
import travelbot.demo.services.TwilioService;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/twilio")
public class TwilioWebhookController {

    private final CustomerRepository customerRepository;
    private final ConversationService conversationService;
    private final ConversationRepository conversationRepository; // <--- per salvare la priority
    private final MessageRepository messageRepository;
    private final AiChatService aiChatService;
    private final TwilioService twilioService;
    private final KeywordLeadService keywordLeadService;         // <--- iniezione servizio keywords

    public TwilioWebhookController(
            CustomerRepository customerRepository,
            ConversationService conversationService,
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            AiChatService aiChatService,
            TwilioService twilioService,
            KeywordLeadService keywordLeadService
    ) {
        this.customerRepository = customerRepository;
        this.conversationService = conversationService;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.aiChatService = aiChatService;
        this.twilioService = twilioService;
        this.keywordLeadService = keywordLeadService;
    }

    @PostMapping(
            value = "/inbound",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> inbound(@RequestBody MultiValueMap<String, String> form) {
        final String from = form.getFirst("From");
        final String body = form.getFirst("Body");
        if (from == null || body == null || body.isBlank()) {
            return ResponseEntity.ok("<Response/>");
        }

        final String phone = from.replace("whatsapp:", "").trim();

        // Esegui asincrono per rispondere subito a Twilio
        CompletableFuture.runAsync(() -> {
            try {
                // 1) Customer + Conversation aperta/nuova
                Customer customer = customerRepository.findByPhone(phone)
                        .orElseGet(() -> customerRepository.save(new Customer(phone, null)));

                Conversation conv = conversationService.findOrStartOpen(customer.getId());

                // 2) Salva messaggio USER
                messageRepository.save(new Message(conv, MessageRole.USER, body));
                customer.setLastContactedAt(Instant.now());
                customerRepository.save(customer);

                // 3) SE parole chiave → alza a HIGH
                if (conv.getLeadValue() != LeadValue.HIGH && keywordLeadService.isHigh(body)) {
                    conv.setLeadValue(LeadValue.HIGH);
                    conversationRepository.save(conv);
                }

                // 4) Chiedi risposta all'AI e invia via Twilio
                String reply = aiChatService.reply(conv.getId(), body);
                twilioService.sendWhatsApp(phone, reply);

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    twilioService.sendWhatsApp(phone, "Ops! Si è verificato un errore temporaneo. Riprova tra poco 🙏");
                } catch (Exception ignored) {
                }
            }
        });

        // Twilio riceve subito una risposta
        return ResponseEntity.ok("<Response/>");
    }
}
