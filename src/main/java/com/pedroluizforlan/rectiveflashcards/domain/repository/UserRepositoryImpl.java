package com.pedroluizforlan.rectiveflashcards.domain.repository;

import com.pedroluizforlan.rectiveflashcards.api.controller.request.UserPageRequest;
import com.pedroluizforlan.rectiveflashcards.domain.document.UserDocument;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@Slf4j
@AllArgsConstructor
public class UserRepositoryImpl {


    public ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<UserDocument> findOnDemand(final UserPageRequest userPageRequest) {
        return Mono.just(new Query())
                .zipWhen(query -> buildWhere(userPageRequest.sentence()))
                .map(tuple -> {
                    var whereClause = new Criteria();
                    whereClause.orOperator(tuple.getT2());
                    return tuple.getT1().addCriteria(whereClause);
                })
                .map(query -> query.with(userPageRequest.getSort()).skip(userPageRequest.getSkip()).limit(userPageRequest.limit()))
                .doFirst(() -> log.info("==== Finding users on demand with follow request {}", userPageRequest))
                .flatMapMany(query -> reactiveMongoTemplate.find(query, UserDocument.class));
    }

    public Mono<Long> count(final UserPageRequest userPageRequest){
        return Mono.just(new Query())
                .flatMap(query -> buildWhere(query, userPageRequest.sentence()))
                .doFirst(() -> log.info("==== Counting users with follow request {}", userPageRequest))
                .flatMap(query -> reactiveMongoTemplate.count(query,UserDocument.class));
    }

    private Mono<Query> buildWhere(final Query query, final String sentence){
        return Mono.just(query)
                .filter(q -> StringUtils.isNotBlank(sentence))
                .switchIfEmpty(Mono.defer(() -> Mono.just(query)))
                .flatMapMany(q -> Flux.fromIterable(List.of("name","email")))
                .map(dbFields -> where(dbFields).regex(sentence, "i"))
                .collectList()
                .map(setWhereClause(query));
    }

    private Function<List<Criteria>, Query> setWhereClause(Query query) {
        return criteria -> {
            var whereClause = new Criteria();
            whereClause.orOperator(criteria);
            return query.addCriteria(whereClause);
        };
    }
}
