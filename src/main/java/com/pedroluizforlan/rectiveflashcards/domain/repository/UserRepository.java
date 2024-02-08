package com.pedroluizforlan.rectiveflashcards.domain.repository;

import com.pedroluizforlan.rectiveflashcards.domain.document.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {
    Mono<UserDocument> findByEmail(final String email);
}
