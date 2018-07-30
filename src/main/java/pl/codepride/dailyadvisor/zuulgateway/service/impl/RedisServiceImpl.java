package pl.codepride.dailyadvisor.zuulgateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pl.codepride.dailyadvisor.zuulgateway.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveKey(String key) {
        redisTemplate.opsForValue().set(key, "");
    }

    @Override
    public boolean checkIfKeyExists(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public String getKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
