package com.alexsysSolutions.alexsis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai.groq")
@Getter
@Setter
public class GroqProperties {

//    private String apiKey = "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";
    private String apiKey;
    private String url;
    private String model;

}