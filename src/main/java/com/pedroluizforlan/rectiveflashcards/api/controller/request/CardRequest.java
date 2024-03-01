package com.pedroluizforlan.rectiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record CardRequest(@JsonProperty("front")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "pergunta do card", example = "azul")
                          String front,
                          @JsonProperty("back")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "resposta do card", example = "blue")
                          String back) {

    @Builder(toBuilder = true)
    public CardRequest {
    }
}
