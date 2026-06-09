package com.alexsysSolutions.alexsis;

import com.alexsysSolutions.alexsis.config.OpenAiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties(OpenAiProperties.class) // to handle the properties configuration
@ConfigurationPropertiesScan// enable ConfigurationProperties
@EnableScheduling // to enable @Sheduled in the service
@SpringBootApplication
@EnableAsync // enable async in the applicaiton
public class AlexsisApplication {

	public static void main(String[] args) {

		SpringApplication.run(AlexsisApplication.class, args);
	}
}
