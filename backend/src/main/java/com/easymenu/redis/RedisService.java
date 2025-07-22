package com.easymenu.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(RedisTemplate<String, String> redisTemplate,
                             @Qualifier("redisObjectMapper") ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> void set(String key, T value, Duration ttl) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, ttl);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize cache value", e);
        }
    }

    public <T> Optional<T> get(String key, Class<T> type) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return Optional.empty();
        try {
            return Optional.of(objectMapper.readValue(json, type));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public <T> Optional<List<T>> getList(String key, Class<T> elementType) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return Optional.empty();
        try {
            JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
            return Optional.of(objectMapper.readValue(json, listType));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void evict(String key) {
        redisTemplate.delete(key);
    }
}
