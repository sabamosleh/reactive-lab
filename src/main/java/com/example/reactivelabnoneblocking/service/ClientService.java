package com.example.reactivelabnoneblocking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Service
@Slf4j
public class ClientService {

    private final WebClient webClient;

    public ClientService() {
        ConnectionProvider connectionProvider = ConnectionProvider.builder("myConnectionPool")
                .maxConnections(1024 * 1024)
                .pendingAcquireMaxCount(-1)
                .build();

        ReactorClientHttpConnector clientHttpConnector =
                new ReactorClientHttpConnector(HttpClient.create(connectionProvider));

        webClient = WebClient.builder()
                .clientConnector(clientHttpConnector)
                .baseUrl("http://localhost:8081")
                .build();
    }

    public Mono<String> echoClient(String inputData) {
        log.debug("echoClient({}) just started...", inputData);

        return webClient
                .post()
                .uri("/reactive-lab/echo")
                .body(BodyInserters.fromValue(inputData))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, "*/*")
                .exchangeToMono(clientResponse -> {
                    log.debug("echoClient({}) finished, HTTP status: [{}].", inputData, clientResponse.statusCode());
                    return clientResponse.bodyToMono(String.class);
                });
    }
}
