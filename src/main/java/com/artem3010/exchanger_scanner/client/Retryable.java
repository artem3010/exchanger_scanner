package com.artem3010.exchanger_scanner.client;

import reactor.util.retry.Retry;

import java.time.Duration;

public interface Retryable {
    Retry retrySpec = Retry
            .backoff(Long.MAX_VALUE, Duration.ofSeconds(3))
            .doAfterRetry((throwable) -> {
                System.out.println("Retry orderbook...");
            })
            //TODO возможно можно заменить на WebSocketClientHandshakeException, т.к. вроде только оно выбрасывается при ошибке подключения, надо исследовать
            .filter(throwable -> throwable instanceof RuntimeException)
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                System.err.println("Retries exhausted");
                return retrySignal.failure();
            }); // Ретраим с увеличивающимся дилеем

    default Retry getRetryStrategy() {
        return retrySpec;
    }

}
