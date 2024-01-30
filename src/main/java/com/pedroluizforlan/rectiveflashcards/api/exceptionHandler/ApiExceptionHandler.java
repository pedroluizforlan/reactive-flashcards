package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.ProblemResponse;
import com.pedroluizforlan.rectiveflashcards.domain.exception.ReactiveFlashCardsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.GENERIC_EXCEPTION;
import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.GENERIC_METHOD_NOT_ALLOW;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono
                .error(ex)
                .onErrorResume(MethodNotAllowedException.class, e-> handleMethodNotAllowedException(exchange, e))
                .onErrorResume(ReactiveFlashCardsException.class, e-> handleReactiveFlashCardsException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e-> handleJsonProcessingException(exchange,e))
                .onErrorResume(Exception.class, e-> handleException(exchange, e))

                .then();
    }

    private Mono<Void> handleMethodNotAllowedException(final ServerWebExchange exchange, final MethodNotAllowedException exception){
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.getMessage();
                })
                .map(message -> buildError(HttpStatus.METHOD_NOT_ALLOWED, message))
                .doFirst(()->log.error("==== MethodNotAllowedException: Method [{}] is not allowed at [{}] ", exchange.getRequest().getMethod(), exchange.getRequest().getPath().value(), exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<Void> handleReactiveFlashCardsException(final ServerWebExchange exchange, final ReactiveFlashCardsException exception){
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                })
                .map(message -> buildError(HttpStatus.INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== ReactiveFlashCardsException ", exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<Void> handleJsonProcessingException(final ServerWebExchange exchange, final JsonProcessingException exception){
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW.getMessage();
                })
                .map(message -> buildError(HttpStatus.METHOD_NOT_ALLOWED, message))
                .doFirst(() -> log.error("==== JsonProcessingException: Failed to map exception for the request {} ", exchange.getRequest().getMethod(), exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<Void> handleException(final ServerWebExchange exchange, final Exception exception){
        return Mono.fromCallable(() -> {
            prepareExchange(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
            return GENERIC_EXCEPTION.getMessage();
        })
                .map(message -> buildError(HttpStatus.INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== Exception ", exception))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, ProblemResponse problemResponse) {
        return exchange.getResponse()
                .writeWith(Mono
                        .fromCallable(() -> new DefaultDataBufferFactory().wrap(objectMapper.writeValueAsBytes(problemResponse))));
    }

    private void prepareExchange(final ServerWebExchange exchange, final HttpStatus statusCode){
        exchange.getResponse().setStatusCode(statusCode);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

    private ProblemResponse buildError(final HttpStatus stats, final String errorDescription){
        return ProblemResponse
                .builder()
                .status(stats.value())
                .errorDescription(errorDescription)
                .build();
    }

}
