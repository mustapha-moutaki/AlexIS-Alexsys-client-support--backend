package com.alexsysSolutions.alexsis.dto.request.ai;

import java.util.List;

public class OpenAiRequest {

    private String model = "gpt-4o-mini";

    private List<Message> messages;

    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public String getContent() { return content; }
    }

    public OpenAiRequest(String systemPrompt, String userMessage) {
        this.messages = List.of(
                new Message("system", systemPrompt),
                new Message("user", userMessage)
        );
    }

    public String getModel() { return model; }
    public List<Message> getMessages() { return messages; }
}