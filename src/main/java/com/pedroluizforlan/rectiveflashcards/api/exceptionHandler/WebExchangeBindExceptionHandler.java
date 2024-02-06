package com.pedroluizforlan.rectiveflashcards.api.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.ErrorFieldResponse;
import com.pedroluizforlan.rectiveflashcards.api.controller.response.ProblemResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.GENERIC_BAD_REQUEST;

@Slf4j
@Component
public class WebExchangeBindExceptionHandler extends AbstractHandlerException<WebExchangeBindException> {

    private final MessageSource messageSource;

    public WebExchangeBindExceptionHandler(ObjectMapper objectMapper, MessageSource messageSource) {
        super(objectMapper);
        this.messageSource = messageSource;
    }

    @Override
    Mono<Void> handlerException(ServerWebExchange exchange, WebExchangeBindException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, HttpStatus.BAD_REQUEST);
                    return GENERIC_BAD_REQUEST.getMessage();
                })
                .map(message -> buildError(HttpStatus.BAD_REQUEST, message))
                .flatMap(problemResponse -> buildRequestErrorMessage(problemResponse,ex))
                .doFirst(()->log.error("==== WebExchangeBindException", ex))
                .flatMap(problemResponse -> writeResponse(exchange,problemResponse));
    }

    private Mono<ProblemResponse> buildRequestErrorMessage(final ProblemResponse response, final WebExchangeBindException exception){
        return Flux.fromIterable(exception.getAllErrors())
                .map(objectError -> ErrorFieldResponse.builder()
                        .name(objectError instanceof FieldError fieldError ? fieldError.getField(): objectError.getObjectName())
                        .message(messageSource.getMessage(objectError, LocaleContextHolder.getLocale())).build())
                .collectList()
                .map(problemResponses -> response.toBuilder().fields(problemResponses).build());
    }
}
