package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;

public record QuestionResponse(@JsonProperty("id")
                               @Schema(description = "identificador do estudo", example = "6bc23cf42ac1260f96676a66", format = "UUID")
                               String id,
                               @JsonProperty("asked")
                               @Schema(description = "pergunta atual do estudo", example = "blue")
                               String asked,
                               @JsonProperty("askedIn")
                               @Schema(description = "horário da pergunta gerada", format = "datetime", example = "2024-03-01T14:44:41Z")
                               OffsetDateTime askedIn) {

    @Builder(toBuilder = true)
    public QuestionResponse {
    }
}
