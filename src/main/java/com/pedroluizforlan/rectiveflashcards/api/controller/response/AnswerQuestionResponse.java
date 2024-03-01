package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Objects;

public record AnswerQuestionResponse(@JsonProperty("asked")
                                     @Schema(description = "pergunta", example = "blue")
                                     String asked,
                                     @JsonProperty("askedIn")
                                     @Schema(description = "horário que a pergunta foi formada", format = "datetime", example = "2024-03-01T14:44:41Z")
                                     OffsetDateTime askedIn,
                                     @JsonProperty("answered")
                                     @Schema(description = "resposta fornecida", example = "azul")
                                     String answered,
                                     @JsonProperty("answeredIn")
                                     @Schema(description = "horário da resposta", format = "datetime", example = "2024-03-01T14:44:41Z")
                                     OffsetDateTime answeredIn,
                                     @JsonProperty("expected")
                                     @Schema(description = "resposta esperada", example = "azul")
                                     String expected) {

    @Builder(toBuilder = true)
    public AnswerQuestionResponse {
    }
}
