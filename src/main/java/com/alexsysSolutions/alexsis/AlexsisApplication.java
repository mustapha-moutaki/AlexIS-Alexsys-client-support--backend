package com.alexsysSolutions.alexsis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConfigurationPropertiesScan// enable ConfigurationProperties
@EnableScheduling // to enable @Sheduled in the service
@SpringBootApplication
public class AlexsisApplication {

	public static void main(String[] args) {

		SpringApplication.run(AlexsisApplication.class, args);
	}
}
