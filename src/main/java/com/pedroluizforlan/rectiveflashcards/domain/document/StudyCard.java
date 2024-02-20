package com.pedroluizforlan.rectiveflashcards.domain.document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

public record StudyCard(String front,
                        String back) {

    public static StudyCardBuilder builder(){
        return new StudyCardBuilder();

    }
    public StudyCardBuilder toBuilder(){
        return new StudyCardBuilder(front,back);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudyCardBuilder{
        private String front;
        private String back;

        public StudyCardBuilder front(final String front){
            this.front = front;
            return this;
        }

        public StudyCardBuilder back(final String back){
            this.back = back;
            return this;
        }

        public StudyCard build(){
            return new StudyCard(front, back);
        }
    }
}
