package com.pedroluizforlan.rectiveflashcards.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.time.Duration;

@ConfigurationProperties("retry-config")
@ConstructorBinding
public record RetryConfig(Long maxRetries, Long minDuration) {
    public Duration minDurationSeconds() {
        return Duration.ofSeconds(minDuration);
    }
}
