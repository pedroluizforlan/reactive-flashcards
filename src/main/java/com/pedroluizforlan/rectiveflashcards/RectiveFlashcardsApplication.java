package com.pedroluizforlan.rectiveflashcards;

import com.pedroluizforlan.rectiveflashcards.core.RetryConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@ConfigurationPropertiesScan(basePackageClasses = RetryConfig.class)
@SpringBootApplication
@EnableReactiveMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class RectiveFlashcardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RectiveFlashcardsApplication.class, args);
    }

}
