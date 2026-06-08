package com.alexsysSolutions.alexsis.dto.request.ai;

public class GeminiRequest {

    public java.util.List<Content> contents;

    public GeminiRequest(String text) {
        this.contents = java.util.List.of(
                new Content(text)
        );
    }

    public static class Content {
        public java.util.List<Part> parts;

        public Content(String text) {
            this.parts = java.util.List.of(new Part(text));
        }
    }

    public static class Part {
        public String text;

        public Part(String text) {
            this.text = text;
        }
    }
}
