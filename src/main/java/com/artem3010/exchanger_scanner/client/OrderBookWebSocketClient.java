package com.artem3010.exchanger_scanner.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class OrderBookWebSocketClient implements Retryable {

    @Value("${apiConfig}")
    private String apiConfig;
    @Value("${bybitDomain}")
    private String bybitDomain;

    @PostConstruct
    public void connect() {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        URI uri = URI.create(bybitDomain + apiConfig);
        client.execute(uri, session -> {
                    Mono<Void> out = session.send(Flux.just(session.textMessage(getPayload())));
                    Flux<String> in = session.receive()
                            //TODO маппинг в сущности + красивый output + обновление данных
                            .map(WebSocketMessage::getPayloadAsText)
                            .doOnNext(message -> System.out.println("OrderBook message: " + message));
                    return out.thenMany(in).then();
                })
                .retryWhen(getRetryStrategy())
                .subscribe();
    }

    private String getPayload() {
        Map<String, Object> subscribeRequest = new HashMap<>();
        subscribeRequest.put("op", "subscribe");
        subscribeRequest.put("args", new String[]{"orderbook.50.BTCUSDT"});
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(subscribeRequest);
        } catch (JsonProcessingException e) {
            log.error("Can't generate payload OrderBookWebSocketClient");
            throw new RuntimeException(e);
        }
    }

}
