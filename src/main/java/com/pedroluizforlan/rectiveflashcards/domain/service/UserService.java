package com.pedroluizforlan.rectiveflashcards.domain.service;

import com.pedroluizforlan.rectiveflashcards.domain.document.UserDocument;
import com.pedroluizforlan.rectiveflashcards.domain.repository.UserRepository;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Service
public class UserService  {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument userDocument){
        return userRepository.save(userDocument)
                .doFirst(() -> log.info("==== Try to save a follow document {}", userDocument));
    }

    public Mono<UserDocument> update(final UserDocument userDocument){
        return userQueryService.findById(userDocument.id())
                .map(user -> userDocument.toBuilder()
                        .createdAt(user.createdAt())
                        .updatedAt(user.updatedAt())
                        .build())
                .flatMap(userRepository::save)
                .doFirst(() -> log.info("==== Try to update a user with follow info {}", userDocument));
    }
}
