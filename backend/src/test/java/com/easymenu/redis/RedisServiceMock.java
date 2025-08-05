package com.easymenu.redis;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@Profile("test")
public class RedisServiceMock extends RedisService {

    public RedisServiceMock() {
        super(null, null);
    }

    @Override
    public <T> void set(String key, T value, Duration ttl) {

    }

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<List<T>> getList(String key, Class<T> elementType) {
        return Optional.empty();
    }

    @Override
    public void evict(String key) {

    }
}
