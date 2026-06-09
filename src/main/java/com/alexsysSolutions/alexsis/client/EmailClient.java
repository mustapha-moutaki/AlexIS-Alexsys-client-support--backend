package com.alexsysSolutions.alexsis.client;

import com.alexsysSolutions.alexsis.dto.email.EmailDtoRequest;
import lombok.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service

public class EmailClient {

    private final WebClient webClient;

    public EmailClient(WebClient emailWebClient) {
        this.webClient = emailWebClient;
    }

    public void sendEmail(String to, String subject, String body) {

        EmailDtoRequest request = new EmailDtoRequest();
        request.setTo(to);
        request.setSubject(subject);
        request.setBody(body);

        webClient.post()
                .uri("/api/v1/emails/send")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error ->
                        System.out.println("Email failed: " + error.getMessage())
                )
                .subscribe();
        // use async
    }


}