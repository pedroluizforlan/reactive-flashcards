package com.pedroluizforlan.rectiveflashcards.core.factorybot.request;

import com.github.javafaker.Faker;
import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserPageRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserSortBy;
import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserSortDirection;

import static com.pedroluizforlan.rectiveflashcards.core.factorybot.RandomData.getFaker;
import static com.pedroluizforlan.rectiveflashcards.core.factorybot.RandomData.randomEnum;

public class UserPageRequestFactoryBot {

    public static UserPageRequestFactoryBotBuilder builder(){
        return new UserPageRequestFactoryBotBuilder();
    }

    public static class UserPageRequestFactoryBotBuilder{

        private final String sentence;
        private Long page;
        private Integer limit;
        private final UserSortBy sortBy;
        private final UserSortDirection sortDirection;
        private final Faker faker = getFaker();

        public UserPageRequestFactoryBotBuilder() {
            this.sentence = faker.lorem().sentence();
            this.page = faker.number().numberBetween(0L, 3L);
            this.limit = faker.number().numberBetween(20, 40);
            this.sortBy = randomEnum(UserSortBy.class);
            this.sortDirection = randomEnum(UserSortDirection.class);
        }

        public UserPageRequestFactoryBotBuilder negativePage(){
            this.page = faker.number().numberBetween(Long.MIN_VALUE, 0);
            return this;
        }

        public UserPageRequestFactoryBotBuilder lessThanZeroLimit(){
            this.limit = faker.number().numberBetween(Integer.MIN_VALUE, 1);
            return this;
        }

        public UserPageRequestFactoryBotBuilder greaterThanFiftyLimit(){
            this.limit = faker.number().numberBetween(51, Integer.MAX_VALUE);
            return this;
        }

        public UserPageRequest build(){
            return UserPageRequest.builder()
                    .sentence(sentence)
                    .page(page)
                    .limit(limit)
                    .sortBy(sortBy)
                    .sortDirection(sortDirection)
                    .build();
        }

    }

}
