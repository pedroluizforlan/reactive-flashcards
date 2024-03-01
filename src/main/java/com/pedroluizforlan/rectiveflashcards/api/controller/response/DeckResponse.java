package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Set;

public record DeckResponse(@JsonProperty("id")
                           @Schema(description = "identificador do deck", format = "UUID", example = "65c23cf42f32560f96676a66")
                           String id,
                           @JsonProperty("name")
                           @Schema(description = "nome do deck", example = "estudo de inglês")
                           String name,
                           @JsonProperty("description")
                           @Schema(description = "descrição do deck", example = "deck de estudo de inglês para iniciantes")
                           String description,
                           @JsonProperty("cards")
                           @Schema(description = "cards que compõem um deck")
                           Set<CardResponse> cards) {
    @Builder(toBuilder = true)
    public DeckResponse {
    }
}
