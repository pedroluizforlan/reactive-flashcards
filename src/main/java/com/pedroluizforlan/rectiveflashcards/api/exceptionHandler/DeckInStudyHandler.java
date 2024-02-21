package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroluizforlan.rectiveflashcards.domain.exception.DeckInStudyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class DeckInStudyHandler extends AbstractHandlerException<DeckInStudyException>{
    public DeckInStudyHandler(final ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Mono<Void> handlerException(final ServerWebExchange exchange, final DeckInStudyException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.CONFLICT);
                    return ex.getMessage();
                })
                .map(message -> buildError(HttpStatus.CONFLICT, message))
                .doFirst(()->log.error("==== DeckInStudyException", ex))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }
}
