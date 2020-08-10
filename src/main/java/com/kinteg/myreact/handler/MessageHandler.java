package com.kinteg.myreact.handler;

import com.kinteg.myreact.domain.Message;
import com.kinteg.myreact.repo.CustomMessageRepo;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MessageHandler {

    private final CustomMessageRepo customMessageRepo;

    public MessageHandler(CustomMessageRepo customMessageRepo) {
        this.customMessageRepo = customMessageRepo;
    }

    public Mono<ServerResponse> getMessageById(ServerRequest serverRequest) {
        Long id = serverRequest.queryParam("id")
                .map(Long::valueOf)
                .orElse(1L);

        Mono<Message> messageMono = customMessageRepo.getById(id);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageMono, Message.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Message> messageBeforeSave = request.bodyToMono(Message.class);
        Mono<Message> messageAfterSave = customMessageRepo.create(messageBeforeSave);
        System.out.println("fgdf");

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageAfterSave, Message.class);
    }

}
