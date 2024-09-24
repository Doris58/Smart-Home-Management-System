package com.infobip.pmf.course.smart_home.notification_service.sender.email;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Objects;

import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;

@Component
public class InfobipEmailSender 
{
    private static final String EMAIL_API_PATH = "/email/3/send";  // or "/email/2/send" ?

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
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "App " + senderConfig.getApiKey())
                .body(BodyInserters.fromMultipartData(createEmailRequestBody(toEmail, subject, message)))
                .retrieve()
                .toEntity(String.class)
                .doOnSuccess(response -> 
                {
                    System.out.println("Infobip Response: " + response.getBody());
                })
                .doOnError(error -> 
                {
                    throw new RuntimeException("Failed to send email via Infobip: " + error.getMessage());
                })
                .block();
    }

    private MultiValueMap<String, String> createEmailRequestBody(String toEmail, String subject, String message) 
    {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("from", senderConfig.getFromEmail());
        body.add("to", toEmail);
        body.add("subject", subject);
        body.add("text", message);
        return body;
    }
}



/* 
    public void sendEmail(String toEmail, String subject, String message) 
    {
        Objects.requireNonNull(toEmail);
        Objects.requireNonNull(subject);
        Objects.requireNonNull(message);

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("from", senderConfig.getFromEmail());
        bodyBuilder.part("to", toEmail);
        bodyBuilder.part("subject", subject);
        bodyBuilder.part("text", message);

        webClient.post()
                .uri(senderConfig.getBaseUrl() + EMAIL_API_PATH)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, "App " + senderConfig.getApiKey())
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .toEntity(String.class)
                .doOnError(error -> 
                {
                    throw new RuntimeException("Failed to send email via Infobip: " + error.getMessage());
                })
                .block();
    }
     */





    

        

