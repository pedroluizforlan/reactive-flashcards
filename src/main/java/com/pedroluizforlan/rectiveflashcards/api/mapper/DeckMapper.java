package com.pedroluizforlan.rectiveflashcards.api.mapper;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.DeckRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.DeckResponse;
import com.pedroluizforlan.rectiveflashcards.domain.document.DeckDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeckMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt ",ignore = true)
    DeckDocument toDocument(final DeckRequest request);

    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt ",ignore = true)
    DeckDocument toDocument(final DeckRequest request, final String id);

    DeckResponse toResponse(final DeckDocument document);
}
