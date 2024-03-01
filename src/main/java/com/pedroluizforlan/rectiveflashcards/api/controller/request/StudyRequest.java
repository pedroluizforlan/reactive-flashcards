package com.pedroluizforlan.rectiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pedroluizforlan.rectiveflashcards.core.validation.MongoId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record StudyRequest(@MongoId
                           @JsonProperty("userId")
                           @Schema(description = "identificador do usuário", example = "65e20235ca1efb41ef951eb4", format = "UUID")
                           String userId,
                           @MongoId
                           @JsonProperty("deckId")
                           @Schema(description = "identificador do deck", example = "6re2qw35ca145141ef951ecw", format = "UUID")
                           String deckId) {

    @Builder(toBuilder = true)
    public StudyRequest {
    }
}
