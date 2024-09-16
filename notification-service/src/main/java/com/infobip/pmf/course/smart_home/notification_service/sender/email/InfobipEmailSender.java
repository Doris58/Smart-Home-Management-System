package com.infobip.pmf.course.smart_home.notification_service.sender.email;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Objects;

@Component
public class InfobipEmailSender 
{
    private static final String EMAIL_API_PATH = "/email/2/send";  // or "/email/3/send" ?

    private final WebClient webClient;
    private final InfobipEmailSenderConfiguration senderConfig;

    public InfobipEmailSender(WebClient.Builder webClientBuilder, InfobipEmailSenderConfiguration senderConfig) 
    {
        this.webClient = webClientBuilder.build();
        this.senderConfig = senderConfig;
    }

    public void sendEmail(String toEmail, String subject, String message) 
    {
        Objects.requireNonNull(toEmail);
        Objects.requireNonNull(subject);
        Objects.requireNonNull(message);

        webClient.post()
                .uri(senderConfig.getBaseUrl() + EMAIL_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "App " + senderConfig.getApiKey())
                .bodyValue(createEmailRequestBody(toEmail, subject, message))
                .retrieve()
                .toEntity(String.class)
                .doOnError(error -> 
                {
                    throw new RuntimeException("Failed to send email via Infobip: " + error.getMessage());
                })
                .block();
    }

    private String createEmailRequestBody(String toEmail, String subject, String message) 
    {
        return """
            {
                "from": "%s",
                "to": "%s",
                "subject": "%s",
                "text": "%s"
            }
            """.formatted(senderConfig.getFromEmail(), toEmail, subject, message);
    }
}
