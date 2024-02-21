package com.pedroluizforlan.rectiveflashcards.api.mapper;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.StudyRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.QuestionResponse;
import com.pedroluizforlan.rectiveflashcards.domain.document.Question;
import com.pedroluizforlan.rectiveflashcards.domain.document.StudyDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studyDeck.deckId", source = "deckId")
    @Mapping(target = "studyDeck.cards", ignore = true)
    @Mapping(target = "questionList", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StudyDocument toDocument(final StudyRequest request);

    QuestionResponse toResponse(final Question question, final String id);

}
