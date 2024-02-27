package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Objects;

public record AnswerQuestionResponse(@JsonProperty("asked")
                                     String asked,
                                     @JsonProperty("askedIn")
                                     OffsetDateTime askedIn,
                                     @JsonProperty("answered")
                                     String answered,
                                     @JsonProperty("answeredIn")
                                     OffsetDateTime answeredIn,
                                     @JsonProperty("expected")
                                     String expected) {

    @Builder(toBuilder = true)
    public AnswerQuestionResponse {
    }
}
