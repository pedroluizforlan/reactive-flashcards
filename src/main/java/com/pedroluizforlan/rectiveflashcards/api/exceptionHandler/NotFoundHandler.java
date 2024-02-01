package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroluizforlan.rectiveflashcards.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class NotFoundHandler extends AbstractHandlerException<NotFoundException>{

    public NotFoundHandler(final ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Mono<Void> handlerException(final ServerWebExchange exchange,final NotFoundException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.NOT_FOUND);
                    return ex.getMessage();
                })
                .map(message -> buildError(HttpStatus.NOT_FOUND, message))
                .doFirst(()->log.error("==== NotFoundException", ex))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }
}