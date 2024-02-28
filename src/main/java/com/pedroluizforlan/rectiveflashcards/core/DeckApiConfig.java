package com.pedroluizforlan.rectiveflashcards.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("deck-api")
public record DeckApiConfig(String baseUrl,
                            String authResource,
                            String deckResource) {

    public String getAuthUri(){
        return String.format("%s/%s", baseUrl, authResource);
    }

    public String getDecksUri(){
        return String.format("%s/%s", baseUrl, deckResource);
    }
}
