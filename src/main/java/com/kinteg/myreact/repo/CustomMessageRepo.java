package com.kinteg.myreact.repo;

import com.kinteg.myreact.domain.Message;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomMessageRepo {

    Mono<Message> getById(Long id);

    Mono<Message> create(Mono<Message> messageMono);

}
