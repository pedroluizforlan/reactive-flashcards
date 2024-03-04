package com.pedroluizforlan.rectiveflashcards.core.factorybot.document;

import com.github.javafaker.Faker;
import com.pedroluizforlan.rectiveflashcards.domain.document.Card;
import com.pedroluizforlan.rectiveflashcards.domain.document.DeckDocument;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;


import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pedroluizforlan.rectiveflashcards.core.factorybot.RandomData.getFaker;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeckDocumentFactoryBot {

    public static DeckDocumentFactoryBotBuilder builder() {
        return new DeckDocumentFactoryBotBuilder();
    }

    public static class DeckDocumentFactoryBotBuilder {

        private String id;
        private String name;
        private String description;
        private final Set<Card> cards = new HashSet<>();
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private final Faker faker = getFaker();

        public DeckDocumentFactoryBotBuilder() {
            this.id = ObjectId.get().toString();
            this.name = faker.name().name();
            generateCards();
            this.description = faker.yoda().quote();
            this.createdAt = OffsetDateTime.now();
            this.updatedAt = OffsetDateTime.now();
        }

        private void generateCards() {
            cards.addAll(Stream.generate(() -> Card.builder()
                            .front(faker.cat().name())
                            .back(faker.color().name())
                            .build())
                    .limit(faker.number().numberBetween(3, 8))
                    .collect(Collectors.toSet()));
        }

        public DeckDocumentFactoryBotBuilder preInsert() {
            this.id = null;
            this.createdAt = null;
            this.updatedAt = null;
            return this;
        }

        public DeckDocumentFactoryBotBuilder preUpdate(final String id,
                                                       final OffsetDateTime createdAt,
                                                       final OffsetDateTime updatedAt) {
            this.id = id;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            return this;
        }

        public DeckDocument build() {
            return DeckDocument.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .cards(cards)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();
        }

    }

}
