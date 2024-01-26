package com.pedroluizforlan.rectiveflashcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class RectiveFlashcardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RectiveFlashcardsApplication.class, args);
	}

}
