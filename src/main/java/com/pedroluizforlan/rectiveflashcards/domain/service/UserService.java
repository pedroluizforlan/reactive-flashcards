package com.pedroluizforlan.rectiveflashcards.domain.service;

import com.pedroluizforlan.rectiveflashcards.domain.document.UserDocument;
import com.pedroluizforlan.rectiveflashcards.domain.exception.EmailAlreadyInUsedException;
import com.pedroluizforlan.rectiveflashcards.domain.exception.NotFoundException;
import com.pedroluizforlan.rectiveflashcards.domain.repository.UserRepository;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.UserQueryService;
import io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.MissingResourceException;
import java.util.Objects;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.EMAIL_ALREADY_USED;

@AllArgsConstructor
@Slf4j
@Service
public class UserService  {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument userDocument){
        return userQueryService.findByEmail(userDocument.email())
                .doFirst(() -> log.info("==== Try to save a follow document {}", userDocument))
                .filter(Objects::isNull)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new EmailAlreadyInUsedException(EMAIL_ALREADY_USED
                                .params(userDocument.email()).getMessage())
                )))
                .onErrorResume(NotFoundException.class, e -> userRepository.save(userDocument));
    }

    public Mono<UserDocument> update(final UserDocument userDocument){
        return verifyEmail(userDocument)
                .then(Mono.defer(() -> userQueryService.findById(userDocument.id())
                    .map(user -> userDocument.toBuilder()
                            .createdAt(user.createdAt())
                            .updatedAt(user.updatedAt())
                            .build())
                    .flatMap(userRepository::save)
                    .doFirst(() -> log.info("==== Try to update a user with follow info {}", userDocument))));
    }

    public Mono<Void> delete(final String id){
        return userQueryService.findById(id)
                .flatMap(userRepository::delete)
                .doFirst(() -> log.info("==== Try to delete a user with follow id {}", id ));
    }

    private Mono<Void> verifyEmail(final UserDocument userDocument){
        return userQueryService.findByEmail(userDocument.email())
                .flatMap(stored -> doVerifyEmail(stored,userDocument));
    }

    private Mono<Void> doVerifyEmail(final UserDocument storedUser, final UserDocument document){
        return Mono.just(storedUser)
                .filter(Objects::isNull)
                .switchIfEmpty(Mono.defer(() -> Mono.just(storedUser)
                        .filter(stored -> stored.id().equals(document.id()))
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new EmailAlreadyInUsedException(EMAIL_ALREADY_USED
                                .params(document.email()).getMessage()))))
                        .onErrorResume(NotFoundException.class, e -> Mono.empty())

                )).then();
    }


}
