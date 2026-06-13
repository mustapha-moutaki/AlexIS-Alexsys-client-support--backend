package com.alexsysSolutions.alexsis.service.impl;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void SendUserCreatedEvent(
            String email,
            String username,
            String adminEmail
    ) {

        String event = """
    {
        "email": "%s",
        "username": "%s",
        "adminEmail": "%s"
    }
    """.formatted(email, username, adminEmail);

        kafkaTemplate.send("user-events", event);
    }
}


















