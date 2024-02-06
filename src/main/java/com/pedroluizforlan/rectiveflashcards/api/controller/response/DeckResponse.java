package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.CardResponse;
import com.pedroluizforlan.rectiveflashcards.api.controller.request.DeckRequest;
import lombok.Builder;

import java.util.Set;

public record DeckResponse(@JsonProperty("id")
                           String id,
                           @JsonProperty("name")
                           String name,
                           @JsonProperty("description")
                           String descripton,
                           @JsonProperty("cards")
                           Set<CardResponse> cardResponseSet) {
    @Builder(toBuilder = true)
    public DeckResponse { }
}
