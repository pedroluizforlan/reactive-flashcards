package com.pedroluizforlan.rectiveflashcards.api.controller.request;

import lombok.Builder;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static com.pedroluizforlan.rectiveflashcards.api.controller.request.UserSortBy.NAME;
import static com.pedroluizforlan.rectiveflashcards.api.controller.request.UserSortDirection.ASC;
import static com.pedroluizforlan.rectiveflashcards.api.controller.request.UserSortDirection.DESC;

public record UserPageRequest(String sentence,
                              Long page,
                              Integer limit,
                              UserSortBy sortBy,
                              UserSortDirection sortDirection) {

    @Builder(toBuilder = true)
    public UserPageRequest {
        if(Objects.isNull(sortBy)){
            sortBy = NAME;
        }

        if(Objects.isNull(sortDirection)){
            sortDirection = ASC;
        }
    }

    public Long getSkip(){
        return page > 0 ? ((page - 1)*limit) : 0;
    }

    public Sort getSort(){
        return sortDirection.equals(DESC) ? Sort.by(sortBy.getField()).descending() : Sort.by(sortBy.getField()).ascending();
    }
}