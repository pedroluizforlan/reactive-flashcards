package com.pedroluizforlan.rectiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static com.pedroluizforlan.rectiveflashcards.api.controller.request.UserSortBy.NAME;
import static com.pedroluizforlan.rectiveflashcards.api.controller.request.UserSortDirection.ASC;
import static com.pedroluizforlan.rectiveflashcards.api.controller.request.UserSortDirection.DESC;

public record UserPageRequest(@JsonProperty("sentence")
                              String sentence,
                              @PositiveOrZero
                              @JsonProperty("page")
                              Long page,
                              @Min(1)
                              @Max(50)
                              @JsonProperty("limit")
                              Integer limit,
                              @JsonProperty("sortBy")
                              UserSortBy sortBy,
                              @JsonProperty("sortDirection")
                              UserSortDirection sortDirection) {

    @Builder(toBuilder = true)
    public UserPageRequest {
        sortBy = ObjectUtils.defaultIfNull(sortBy, NAME);
        sortDirection = ObjectUtils.defaultIfNull(sortDirection, ASC);
        limit = ObjectUtils.defaultIfNull(limit, 20);
        page = ObjectUtils.defaultIfNull(page, 0L);
    }

    public Long getSkip() {
        return page > 0 ? ((page - 1) * limit) : 0;
    }

    public Sort getSort() {
        return sortDirection.equals(DESC) ? Sort.by(sortBy.getField()).descending() : Sort.by(sortBy.getField()).ascending();
    }
}