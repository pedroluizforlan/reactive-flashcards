package com.pedroluizforlan.rectiveflashcards.domain.dto;

import lombok.Builder;

public record StudyCardDTO(String front,
                           String back) {

    @Builder(toBuilder = true)
    public StudyCardDTO {
    }
}
