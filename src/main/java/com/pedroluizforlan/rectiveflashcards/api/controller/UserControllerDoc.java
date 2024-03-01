package com.pedroluizforlan.rectiveflashcards.api.controller;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserPageRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.UserPageResponse;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.UserResponse;
import com.pedroluizforlan.rectiveflashcards.api.mapper.UserMapper;
import com.pedroluizforlan.rectiveflashcards.core.validation.MongoId;
import com.pedroluizforlan.rectiveflashcards.domain.service.UserService;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.UserQueryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("users")
@Slf4j
@AllArgsConstructor
public class UserControllerDoc implements com.pedroluizforlan.rectiveflashcards.api.controller.documentation.UserControllerDoc {
    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest userRequest) {
        return userService.save(userMapper.toDocument(userRequest))
                .doFirst(() -> log.info("==== Saving a user with follow data {}", userRequest))
                .map(userMapper::toResponse);
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> findById(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id) {
        return userQueryService.findById(id)
                .doFirst(() -> log.info("==== Finding a user with follow id {}", id))
                .map(userMapper::toResponse);
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Mono<UserPageResponse> findOnDemand(@Valid final UserPageRequest userPageRequest) {
        return userQueryService.findOnDemand(userPageRequest)
                .doFirst(() -> log.info("==== Finding users on demand with follow request {}", userPageRequest))
                .map(page -> userMapper.toResponse(page, userPageRequest.limit()));
    }

    @Override
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> update(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id,
                                     @Valid @RequestBody final UserRequest userRequest) {
        return userService.update(userMapper.toDocument(userRequest, id))
                .doFirst(() -> log.info("==== Updating a user with follow info [body: {}, id: {}]", userRequest, id))
                .map(userMapper::toResponse);
    }

    @Override
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable @Valid @MongoId(message = "{userController.id") final String id) {
        return userService.delete(id)
                .doFirst(() -> log.info("Deleting a user with follow id {}", id));
    }
}
