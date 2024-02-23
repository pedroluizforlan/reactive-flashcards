package com.pedroluizforlan.rectiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record AnsweredQuestionRequest(@JsonProperty("answered")
                                      @Size(min = 1, max = 255)
                                      @NotBlank
                                      String answered) {
    @Builder(toBuilder = true)
    public AnsweredQuestionRequest { }
}
