package com.ambev.order.order_service.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisDuplicateService {

    private static final String DUPLICATE_KEY_PREFIX = "order:duplicate:";
    private static final Duration TTL = Duration.ofHours(24);

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean isDuplicate(String externalId) {
        String key = DUPLICATE_KEY_PREFIX + externalId;
        Boolean exists = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(exists);
    }

    public void markAsProcessed(String externalId) {
        String key = DUPLICATE_KEY_PREFIX + externalId;
        redisTemplate.opsForValue().set(key, "processed", TTL);
        log.debug("Marcado como processado no Redis: {}", externalId);
    }

    public void remove(String externalId) {
        String key = DUPLICATE_KEY_PREFIX + externalId;
        redisTemplate.delete(key);
        log.debug("Removido do Redis: {}", externalId);
    }
}

