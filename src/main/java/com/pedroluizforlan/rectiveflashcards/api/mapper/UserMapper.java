package com.pedroluizforlan.rectiveflashcards.api.mapper;


import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.UserPageResponse;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.UserResponse;
import com.pedroluizforlan.rectiveflashcards.domain.document.UserDocument;
import com.pedroluizforlan.rectiveflashcards.domain.dto.UserPageDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserDocument toDocument(final UserRequest userRequest);


    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserDocument toDocument(final UserRequest userRequest, final String id);

    UserResponse toResponse(final UserDocument userDocument);

    UserPageResponse toResponse(final UserPageDocument userPageDocument, final Integer limit);
}
