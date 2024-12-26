package com.tegar.auth_service.service;

import com.tegar.auth_service.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUserService {

    private final RedisTemplate<String, Object> redisTemplate;

    // save User to redis
    public void save(User user) {
        redisTemplate.opsForValue().set(user.getEmail(), user);
    }

    // get user from redis
    public User findByEmail(String email) {
        return (User) redisTemplate.opsForValue().get(email);
    }
}
