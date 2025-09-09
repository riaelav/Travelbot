package travelbot.demo.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.whatsapp-from}")
    private String fromWhatsApp;

    public String sendWhatsApp(String toWhatsAppNumber, String body) {
        
        Twilio.init(accountSid, authToken);

        PhoneNumber to = new PhoneNumber(
                toWhatsAppNumber.startsWith("whatsapp:") ? toWhatsAppNumber : "whatsapp:" + toWhatsAppNumber
        );
        PhoneNumber from = new PhoneNumber(fromWhatsApp);

        return Message.creator(to, from, body).create().getSid();
    }
}
