package com.pedroluizforlan.rectiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pedroluizforlan.rectiveflashcards.core.validation.MongoId;
import lombok.Builder;

public record StudyRequest(@MongoId
                           @JsonProperty("userId")
                           String userId,
                           @MongoId
                           @JsonProperty("deckId")
                           String deckId) {

    @Builder(toBuilder = true)
    public StudyRequest{ }
}
