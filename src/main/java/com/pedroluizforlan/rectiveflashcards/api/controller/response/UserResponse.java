package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record UserResponse(@JsonProperty("id")
                           @Schema(description = "identificador do usuário", format = "UUID", example = "65c23cf42f32560f96676a66")
                           String id,
                           @JsonProperty("name")
                           @Schema(description = "nome do usuário", example = "Pedro")
                           String name,
                           @JsonProperty("email")
                           @Schema(description = "email do usuário", example = "pedro@pedro.com.br", format = "email")
                           String email) {
    @Builder(toBuilder = true)
    public UserResponse{}
}
