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
import travelbot.demo.enums.MessageRole;
import travelbot.demo.repositories.CustomerRepository;
import travelbot.demo.repositories.MessageRepository;
import travelbot.demo.services.AiChatService;
import travelbot.demo.services.ConversationService;
import travelbot.demo.services.TwilioService;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/twilio")
public class TwilioWebhookController {

    private final CustomerRepository customerRepository;
    private final ConversationService conversationService;
    private final MessageRepository messageRepository;
    private final AiChatService aiChatService;
    private final TwilioService twilioService;

    public TwilioWebhookController(
            CustomerRepository customerRepository,
            ConversationService conversationService,
            MessageRepository messageRepository,
            AiChatService aiChatService,
            TwilioService twilioService
    ) {
        this.customerRepository = customerRepository;
        this.conversationService = conversationService;
        this.messageRepository = messageRepository;
        this.aiChatService = aiChatService;
        this.twilioService = twilioService;
    }

    @PostMapping(
            value = "/inbound",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> inbound(@RequestBody MultiValueMap<String, String> form) {
        final String from = form.getFirst("From");
        final String body = form.getFirst("Body");
        final String messageSid = form.getFirst("MessageSid");
        if (from == null || body == null || body.isBlank()) {

            return ResponseEntity.ok("<Response/>");
        }

        final String phone = from.replace("whatsapp:", "").trim();


        CompletableFuture.runAsync(() -> {
            try {


                Customer customer = customerRepository.findByPhone(phone)
                        .orElseGet(() -> customerRepository.save(new Customer(phone, null)));

                Conversation conv = conversationService.findOrStartOpen(customer.getId());

                // salva USER
                messageRepository.save(new Message(conv, MessageRole.USER, body));
                customer.setLastContactedAt(Instant.now());
                customerRepository.save(customer);

                // chiama OpenAI + salva ASSISTANT
                String reply = aiChatService.reply(conv.getId(), body);

                // invia risposta su WhatsApp
                twilioService.sendWhatsApp(phone, reply);

            } catch (Exception e) {

                e.printStackTrace();
                try {
                    twilioService.sendWhatsApp(phone, "Ops! Si è verificato un errore temporaneo. Riprova tra poco 🙏");
                } catch (Exception ignored) {
                }
            }
        });

        // Risposta IMMEDIATA
        return ResponseEntity.ok("<Response/>");
    }
}
