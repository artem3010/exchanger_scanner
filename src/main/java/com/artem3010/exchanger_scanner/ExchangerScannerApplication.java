package com.artem3010.exchanger_scanner;

import com.bybit.api.client.restApi.BybitApiAsyncAssetRestClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExchangerScannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangerScannerApplication.class, args);
    }


}
