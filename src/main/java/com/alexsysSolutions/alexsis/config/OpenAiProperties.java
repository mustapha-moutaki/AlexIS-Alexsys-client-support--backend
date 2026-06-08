package com.alexsysSolutions.alexsis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "gemini.api")
public class OpenAiProperties {

    private String key;
    @Getter
    private String url;
    private String model;
    private String timeout;


}
