package com.pedroluizforlan.rectiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
                              @Schema(description = "texto para filtrar por nome e email (case insensitive)", example = "maria")
                              String sentence,
                              @PositiveOrZero
                              @JsonProperty("page")
                              @Schema(description = "página solicitada", example = "1", defaultValue = "0")
                              Long page,
                              @Min(1)
                              @Max(50)
                              @JsonProperty("limit")
                              @Schema(description = "tamanho da página", example = "30", defaultValue = "20")
                              Integer limit,
                              @JsonProperty("sortBy")
                              @Schema(description = "campo para ordenação", enumAsRef = true, defaultValue = "NAME")
                              UserSortBy sortBy,
                              @JsonProperty("sortDirection")
                              @Schema(description = "sentido da ordenação", enumAsRef = true, defaultValue = "ASC")
                              UserSortDirection sortDirection) {

    @Builder(toBuilder = true)
    public UserPageRequest {
        sortBy = ObjectUtils.defaultIfNull(sortBy, NAME);
        sortDirection = ObjectUtils.defaultIfNull(sortDirection, ASC);
        limit = ObjectUtils.defaultIfNull(limit, 20);
        page = ObjectUtils.defaultIfNull(page, 0L);
    }

    @Schema(hidden = true)
    public Long getSkip() {
        return page > 0 ? ((page - 1) * limit) : 0;
    }

    @Schema(hidden = true)
    public Sort getSort() {
        return sortDirection.equals(DESC) ? Sort.by(sortBy.getField()).descending() : Sort.by(sortBy.getField()).ascending();
    }
}