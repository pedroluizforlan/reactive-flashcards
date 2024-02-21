package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

import java.util.Set;

public record DeckResponse(@JsonProperty("id")
                           String id,
                           @JsonProperty("name")
                           String name,
                           @JsonProperty("description")
                           String descripton,
                           @JsonProperty("cards")
                           Set<CardResponse> cards) {
    @Builder(toBuilder = true)
    public DeckResponse { }
}