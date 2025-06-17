package SRMS.FinalProject.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.twilio.Twilio;


@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.api.key}")
    private String apiKey;

    @Value("${twilio.api.secret}")
    private String apiSecret;

    @PostConstruct
    public void init() {
        Twilio.init(apiKey, apiSecret, accountSid);
        System.out.println("✅ Twilio initialized with API Key");
    }
}
