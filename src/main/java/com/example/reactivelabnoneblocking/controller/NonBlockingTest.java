package com.example.reactivelabnoneblocking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.example.reactivelabnoneblocking.service.ClientService;

@RestController
@RequestMapping("/reactive-lab")
@RequiredArgsConstructor
public class NonBlockingTest {

    private final ClientService service;

    @PostMapping(path = "/non-blocking",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> testNonBlocking(@RequestBody String inputData) {
        return service.echoClient(inputData);
    }
}
