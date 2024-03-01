package com.pedroluizforlan.rectiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

public record DeckRequest(@JsonProperty("name")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "nome do deck", example = "estudo de inglês")
                          String name,
                          @JsonProperty("description")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "descrição do deck", example = "deck de estudo de inglês para iniciantes")
                          String description,
                          @Valid
                          @Size(min = 3)
                          @NotNull
                          @JsonProperty("cards")
                          @Schema(description = "cards que compõem um deck")
                          Set<CardRequest> cards) {

    @Builder(toBuilder = true)
    public DeckRequest {
    }
}
