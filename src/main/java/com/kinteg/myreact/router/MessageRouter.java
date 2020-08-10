package com.kinteg.myreact.router;

import com.kinteg.myreact.handler.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class MessageRouter {

    @Bean
    public RouterFunction<ServerResponse> route(MessageHandler messageHandler) {
        return RouterFunctions
                .route(
                        GET("/message")
                                .and(RequestPredicates.accept(APPLICATION_JSON)), messageHandler::getMessageById)
                .andRoute(
                        POST("/message")
                                .and(RequestPredicates.accept(APPLICATION_JSON)), messageHandler::create);
    }

}
