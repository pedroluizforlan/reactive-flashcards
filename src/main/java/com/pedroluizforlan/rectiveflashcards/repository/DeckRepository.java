package com.pedroluizforlan.rectiveflashcards.repository;

import com.pedroluizforlan.rectiveflashcards.document.DeckDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends ReactiveMongoRepository<DeckDocument, String> {
}
