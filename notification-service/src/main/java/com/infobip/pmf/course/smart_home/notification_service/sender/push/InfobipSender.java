package com.infobip.pmf.course.smart_home.notification_service.sender.push;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient; //webflux?
import java.util.Objects;

@Component
public class InfobipSender 
{
    // api path
    private static final String PUSH_API_PATH = "/push/2/message";
    private static final String API_KEY_PREFIX = "App ";

    private final WebClient webClient;
    private final InfobipSenderConfiguration senderConfig;

    public InfobipSender(WebClient.Builder webClientBuilder, InfobipSenderConfiguration senderConfig) 
    {
        this.webClient = webClientBuilder.build();
        this.senderConfig = senderConfig;
    }

    public void send(String title, String message) 
    {
        Objects.requireNonNull(message);

        // use the WebClient to make a POST request to Infobip's push service
        webClient.post()
                .uri(senderConfig.getBaseUrl() + PUSH_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, API_KEY_PREFIX + senderConfig.getApiKey())
                .bodyValue(createPushRequestBody(title, message))
                .retrieve()
                .toEntity(String.class)
                .doOnError(error -> 
                {
                    throw new RuntimeException("Failed to send push notification via Infobip: " + error.getMessage());
                })
                .block();
    }

    // the body that supports push notifications
    private String createPushRequestBody(String title, String message) 
    {
        return """
            {
                "messages": [
                    {
                        "destinations": [
                            {"to": "%s"}
                        ],
                        "notification": {
                            "title": "%s",
                            "body": "%s"
                        }
                    }
                ]
            }
            """.formatted(senderConfig.getTo(), title, message);
    }
}
