package com.alexsysSolutions.alexsis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan// enable ConfigurationProperties
@SpringBootApplication
public class AlexsisApplication {

	public static void main(String[] args) {

		SpringApplication.run(AlexsisApplication.class, args);
	}
}
