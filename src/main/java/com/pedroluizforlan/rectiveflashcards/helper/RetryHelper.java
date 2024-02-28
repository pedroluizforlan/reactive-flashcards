package com.pedroluizforlan.rectiveflashcards.helper;

import com.pedroluizforlan.rectiveflashcards.core.RetryConfig;
import com.pedroluizforlan.rectiveflashcards.domain.exception.RetryException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;

import java.util.function.Predicate;

import static com.pedroluizforlan.rectiveflashcards.domain.exception.BaseErrorMessage.GENERIC_MAX_RETRIES;


@Component
@Slf4j
@AllArgsConstructor
public class RetryHelper {
    private final RetryConfig retryConfig;

    public Retry processRetry(final String retryIdentifier, final Predicate<? super Throwable> errorFilter) {
        return Retry.backoff(0L, retryConfig.minDurationSeconds())
                .filter(errorFilter)
                .doBeforeRetry(retrySignal -> log.warn("==== Retrying {} - {} time(s)", retryIdentifier, retrySignal.totalRetries()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new RetryException(GENERIC_MAX_RETRIES.getMessage(), retrySignal.failure()));
    }
}
