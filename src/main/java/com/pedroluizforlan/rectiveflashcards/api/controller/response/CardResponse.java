package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record CardResponse(@JsonProperty("front")
                           @Schema(description = "pergunta do card", example = "azul")
                           String front,
                           @JsonProperty("back")
                           @Schema(description = "resposta do card", example = "blue")
                           String back) {

    @Builder(toBuilder = true)
    public CardResponse {
    }
}
