package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record ErrorFieldResponse(@JsonProperty("name")
                                 @Schema(description = "nome do campo com erro", example = "name")
                                 String name,
                                 @JsonProperty("message")
                                 @Schema(description = "descrição do erro", example = "Informe um nome para o deck")
                                 String message) {
    @Builder(toBuilder = true)
    public ErrorFieldResponse { }
}
