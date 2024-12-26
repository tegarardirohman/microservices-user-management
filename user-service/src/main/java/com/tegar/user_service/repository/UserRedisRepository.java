package com.tegar.user_service.repository;

import com.tegar.user_service.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UserRedisRepository {
    private final RedisTemplate<String, User> redisTemplate;
    private final String KEY_PREFIX = "user:";
    private final long CACHE_TTL_HOURS = 24;

    public Optional<User> findById(String id) {
        User user = redisTemplate.opsForValue().get(KEY_PREFIX + id);
        return Optional.ofNullable(user);
    }

    public void save(User user) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + user.getId(),
                user,
                CACHE_TTL_HOURS,
                TimeUnit.HOURS
        );
    }

    public void deleteById(String id) {
        redisTemplate.delete(KEY_PREFIX + id);
    }
}