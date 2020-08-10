package com.kinteg.myreact.repo.impl;

import com.kinteg.myreact.domain.Message;
import com.kinteg.myreact.repo.CustomMessageRepo;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class CustomMessageRepoImpl implements CustomMessageRepo {

    private final ConnectionFactory connectionFactory;

    public CustomMessageRepoImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public Mono<Message> getById(Long id) {
        return Mono.from(connectionFactory.create())
                .flatMap(c -> Mono.from(c.createStatement("SELECT id, data FROM message WHERE id = $1")
                        .bind("$1", id)
                        .execute())
                        .doFinally((st) -> c.close()))
                .map(result -> result.map((row, meta) ->
                        new Message(
                                row.get("id", Long.class),
                                row.get("data", String.class)
                        )))
                .flatMap(Mono::from);
    }

    @Override
    public Mono<Message> create(Mono<Message> messageMono) {
        return messageMono.flatMap(message ->
                Mono.from(connectionFactory.create())
                        .flatMap(connection -> Mono.from(connection.beginTransaction())
                                .then(Mono.from(connection.createStatement("INSERT INTO message(id, data) VALUES ($1, $2)")
                                        .bind("$1", message.getId())
                                        .bind("$2", message.getData())
                                        .returnGeneratedValues("id")
                                        .execute()))
                                .map(result -> result.map((row, meta) ->
                                        new Message(row.get("id", Long.class),
                                                message.getData())))
                                .flatMap(pub -> Mono.from(pub))
                                .delayUntil(r -> connection.commitTransaction())
                                .doFinally((signalType -> connection.close()))
                        ));
    }
}
