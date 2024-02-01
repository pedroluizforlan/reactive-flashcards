package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.GENERIC_NOT_FOUND;

@Slf4j
@Component
public class ResponseStatusExceptionHandler extends AbstractHandlerException<ResponseStatusException>{
    public ResponseStatusExceptionHandler(final ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Mono<Void> handlerException(final ServerWebExchange exchange, final ResponseStatusException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.NOT_FOUND);
                    return GENERIC_NOT_FOUND.getMessage();
                })
                .map(message -> buildError(HttpStatus.NOT_FOUND, message))
                .doFirst(()->log.error("==== ResponseStatusException", ex))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }
}
