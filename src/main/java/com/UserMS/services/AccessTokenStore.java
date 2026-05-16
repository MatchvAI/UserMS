package com.UserMS.services;

import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Stores active access-tokens (by JWT jti) in Redis so the gateway can enforce revocation.
 *
 * Key: auth:access:{jti} -> value: subject (email)
 */
@Service
public class AccessTokenStore {

    private static final String KEY_PREFIX = "auth:access:";

    private final StringRedisTemplate redis;

    public AccessTokenStore(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void store(String jti, String subject, Duration ttl) {
        if (jti == null || jti.isBlank()) return;
        if (ttl == null || ttl.isNegative() || ttl.isZero()) return;
        redis.opsForValue().set(KEY_PREFIX + jti, subject, ttl);
    }

    public void revoke(String jti) {
        if (jti == null || jti.isBlank()) return;
        redis.delete(KEY_PREFIX + jti);
    }
}

