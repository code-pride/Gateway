package pl.codepride.dailyadvisor.gateway.service;

import reactor.core.publisher.Mono;

public interface RedisService {

    public Mono<Boolean> checkIfKeyExists(String key);
}
