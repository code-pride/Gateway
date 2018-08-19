package pl.codepride.dailyadvisor.gateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pl.codepride.dailyadvisor.gateway.service.RedisService;
import reactor.core.publisher.Mono;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Boolean> checkIfKeyExists(String key) {
        return redisTemplate.hasKey(key);
    }
}
