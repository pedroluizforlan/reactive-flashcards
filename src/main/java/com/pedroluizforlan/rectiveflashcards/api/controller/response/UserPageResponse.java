package com.pedroluizforlan.rectiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

public record UserPageResponse(@JsonProperty("currentPage")
                               @Schema(description = "pagina retornada", example = "1")
                               Long currentPage,
                               @JsonProperty("totalPages")
                               @Schema(description = "total de páginas ", example = "20")
                               Long totalPages,
                               @JsonProperty("totalItems")
                               @Schema(description = "total de registros páginados", example = "15")
                               Long totalItems,
                               @JsonProperty("content")
                               @Schema(description = "usuários da página")
                               List<UserResponse> content) {
    public static UserPageResponseBuilder builder() {
        return new UserPageResponseBuilder();
    }

    public UserPageResponseBuilder toBuilder(final Integer limit) {
        return new UserPageResponseBuilder(limit, currentPage, totalPages, content);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserPageResponseBuilder {
        private Integer limit;
        private Long currentPage;
        private Long totalItems;
        private List<UserResponse> content;


        public UserPageResponseBuilder limit(final Integer limit) {
            this.limit = limit;
            return this;

        }

        public UserPageResponseBuilder currentPage(final Long currentPage) {
            this.currentPage = currentPage;
            return this;

        }

        public UserPageResponseBuilder totalItems(final Long totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public UserPageResponseBuilder content(final List<UserResponse> content) {
            this.content = content;
            return this;
        }

        public UserPageResponse build() {
            var totalPages = (totalItems / limit) + ((totalItems % limit > 0) ? 1 : 0);
            return new UserPageResponse(currentPage, totalPages, totalItems, content);
        }
    }
}
