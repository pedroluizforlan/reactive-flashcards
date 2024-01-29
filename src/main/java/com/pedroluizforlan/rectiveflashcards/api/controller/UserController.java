package com.pedroluizforlan.rectiveflashcards.api.controller;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserRequest;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.UserResponse;
import com.pedroluizforlan.rectiveflashcards.api.mapper.UserMapper;
import com.pedroluizforlan.rectiveflashcards.domain.service.UserService;
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
    private final UserMapper userMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest userRequest){
        return userService.save(userMapper.toDocument(userRequest))
                .doFirst(()->log.info("==== Saving a user with follow data {}", userRequest))
                .map(userMapper::toResponse);
    }
}
