package com.pedroluizforlan.rectiveflashcards.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("deck-api")
public record DeckApiConfig(String baseUrl,
                            String authResource,
                            String deckResource) {
}
