package com.pedroluizforlan.rectiveflashcards.domain.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;


import java.time.OffsetDateTime;
import java.util.ArrayList;

import java.util.List;


public record StudyDTO(String id,
                       String userId,
                       Boolean complete,
                       StudyDeckDTO studyDeck,
                       List<QuestionDTO> questionList,
                       List<String> remainAnswers,
                       OffsetDateTime createdAt,
                       OffsetDateTime updatedAt) {
    public static StudyDocumentBuilder builder() {
        return new StudyDocumentBuilder();
    }

    public StudyDocumentBuilder toBuilder() {
        return new StudyDocumentBuilder(id, userId, studyDeck, questionList, remainAnswers, createdAt, updatedAt);
    }

    public Boolean hasAnyAnswer() {
        return CollectionUtils.isNotEmpty(remainAnswers);

    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyDocumentBuilder {
        private String id;
        private String userId;
        private StudyDeckDTO studyDeck;
        private List<QuestionDTO> questionList = new ArrayList<>();
        private List<String> remainAnswers;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public StudyDocumentBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public StudyDocumentBuilder userId(final String userId) {
            this.userId = userId;
            return this;
        }


        public StudyDocumentBuilder studyDeck(final StudyDeckDTO studyDeck) {
            this.studyDeck = studyDeck;
            return this;
        }

        public StudyDocumentBuilder questionList(final List<QuestionDTO> question) {
            this.questionList = question;
            return this;
        }

        public StudyDocumentBuilder questionList(final QuestionDTO question) {
            this.questionList.add(question);
            return this;
        }

        public StudyDocumentBuilder remainAnswers(final List<String> remainAnswers) {
            this.remainAnswers = remainAnswers;
            return this;
        }

        public StudyDocumentBuilder createdAt(final OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public StudyDocumentBuilder updatedAt(final OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public StudyDTO build() {
            var rightQuestions = questionList.stream().filter(QuestionDTO::isCorrect).toList();
            var complete = rightQuestions.size() == studyDeck.cards().size();
            return new StudyDTO(id, userId, complete, studyDeck, questionList, remainAnswers, createdAt, updatedAt);
        }
    }
}
