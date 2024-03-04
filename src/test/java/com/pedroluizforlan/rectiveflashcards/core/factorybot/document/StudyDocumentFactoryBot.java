package com.pedroluizforlan.rectiveflashcards.core.factorybot.document;

import com.github.javafaker.Faker;
import com.pedroluizforlan.rectiveflashcards.domain.document.Card;
import com.pedroluizforlan.rectiveflashcards.domain.document.Question;
import com.pedroluizforlan.rectiveflashcards.domain.document.StudyDeck;
import com.pedroluizforlan.rectiveflashcards.domain.document.StudyDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.pedroluizforlan.rectiveflashcards.core.factorybot.RandomData.getFaker;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudyDocumentFactoryBot {

    public static StudyDocumentFactoryBotBuilder builder(final String userId, final String deckId, final Set<Card> cards) {
        return new StudyDocumentFactoryBotBuilder(userId, deckId, cards);
    }

    public static class StudyDocumentFactoryBotBuilder {
        private String id;
        private String userId;
        private StudyDeck studyDeck;
        private List<Question> questions = new ArrayList<>();
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private final Faker faker = getFaker();

        public StudyDocumentFactoryBotBuilder(final String userId, final String deckId, final Set<Card> cards) {
            this.id = ObjectId.get().toString();
            this.userId = userId;
            this.studyDeck = this.studyDeck.toBuilder().deckId(deckId).build();
            this.createdAt = OffsetDateTime.now();
            this.updatedAt = OffsetDateTime.now();
        }

        public StudyDocument build(){
            return StudyDocument.builder()
                    .id(id)
                    .userId(userId)
                    .studyDeck(studyDeck)
                    .questions(questions)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();
        }
    }
}
