package com.ambev.order.order_service.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistributedLockService {

    private static final String LOCK_KEY_PREFIX = "lock:order:";
    private static final Duration DEFAULT_LOCK_TIMEOUT = Duration.ofSeconds(30);

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean tryLock(String externalId) {
        return tryLock(externalId, DEFAULT_LOCK_TIMEOUT);
    }

    public boolean tryLock(String externalId, Duration timeout) {
        String key = LOCK_KEY_PREFIX + externalId;
        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(key, "locked", timeout);
        
        boolean result = Boolean.TRUE.equals(acquired);
        if (result) {
            log.debug("Lock adquirido para: {}", externalId);
        } else {
            log.debug("Lock não adquirido para: {}", externalId);
        }
        return result;
    }

    public void releaseLock(String externalId) {
        String key = LOCK_KEY_PREFIX + externalId;
        redisTemplate.delete(key);
        log.debug("Lock liberado para: {}", externalId);
    }
}

