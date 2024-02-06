package com.pedroluizforlan.rectiveflashcards.api.controller;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.UserResponse;
import com.pedroluizforlan.rectiveflashcards.api.mapper.UserMapper;
import com.pedroluizforlan.rectiveflashcards.core.validation.MongoId;
import com.pedroluizforlan.rectiveflashcards.domain.service.UserService;
import com.pedroluizforlan.rectiveflashcards.domain.service.query.UserQueryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;



import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest userRequest){
        return userService.save(userMapper.toDocument(userRequest))
                .doFirst(()->log.info("==== Saving a user with follow data {}", userRequest))
                .map(userMapper::toResponse);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<UserResponse> findById(@PathVariable @Valid @MongoId(message = "{userController.id}") final String id){
        return userQueryService.findById(id)
                .doFirst(() -> log.info("==== finding a user with follow id {}"))
                .map(userMapper::toResponse);

    }
}
