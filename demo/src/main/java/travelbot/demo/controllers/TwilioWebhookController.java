package travelbot.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/twilio")
public class TwilioWebhookController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AiChatService aiChatService;
    @Autowired
    private TwilioService twilioService;

    @PostMapping(value = "/inbound", consumes = "application/x-www-form-urlencoded")
    public void inbound(@RequestBody MultiValueMap<String, String> form) {
        String from = form.getFirst("From");   // es: "whatsapp:+39XXXXXXXXXX"
        String body = form.getFirst("Body");
        if (from == null || body == null || body.isBlank()) return;

        String phone = from.replace("whatsapp:", "").trim(); // normalizza

        Customer customer = customerRepository.findByPhone(phone)
                .orElseGet(() -> customerRepository.save(new Customer(phone, null)));

        Conversation conv = conversationService.findOrStartOpen(customer.getId());

        // salva USER
        messageRepository.save(new Message(conv, MessageRole.USER, body));
        customer.setLastContactedAt(Instant.now());
        customerRepository.save(customer);

        // chiama OpenAI + salva ASSISTANT
        String reply = aiChatService.reply(conv.getId(), body);

        // rispondi su WhatsApp
        twilioService.sendWhatsApp(phone, reply);
    }
}
