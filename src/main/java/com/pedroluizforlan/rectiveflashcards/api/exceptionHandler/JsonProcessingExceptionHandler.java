package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.GENERIC_METHOD_NOT_ALLOW;

@Slf4j
@Component
public class JsonProcessingExceptionHandler extends AbstractHandlerException<JsonProcessingException> {

    public JsonProcessingExceptionHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    Mono<Void> handlerException(ServerWebExchange exchange,JsonProcessingException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.getMessage();
                })
                .map(message -> buildError(HttpStatus.METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== JsonProcessingException: Failed to map exception for the request {} ", exchange.getRequest().getPath().value(), ex))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }
}
