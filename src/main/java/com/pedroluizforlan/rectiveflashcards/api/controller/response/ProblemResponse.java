package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ProblemResponse(@JsonProperty("status")
                              @Schema(description = "http status retornado", example = "400")
                              Integer status,
                              @JsonProperty("timestamp")
                              @Schema(description = "momento que o erro aconteceu", format = "datetime", example = "2024-03-01T14:44:41Z")
                              OffsetDateTime timestamp,
                              @JsonProperty("errorDescription")
                              @Schema(description = "descrição do erro", example = "Sua requisição tem informações inválidas")
                              String errorDescription,
                              @JsonProperty("fields")
                              @Schema(description = "caso a requisição tenha parametros inválidos, aqui sera informados os erros referentes a requisição")
                              List<ErrorFieldResponse> fields) {
    @Builder(toBuilder = true)
    public ProblemResponse{ }
}
