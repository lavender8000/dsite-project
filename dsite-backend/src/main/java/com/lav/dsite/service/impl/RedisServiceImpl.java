package com.lav.dsite.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lav.dsite.dto.UserRedisDto;
import com.lav.dsite.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER_KEY_PREFIX = "user:";

    private static final String BLACK_LIST_PREFIX = "blacklisted_jwt:";

    @Override
    public void saveUser(UserRedisDto userRedisDto) {
        String key = USER_KEY_PREFIX + userRedisDto.getId();
        redisTemplate.opsForValue().set(key, userRedisDto);
    }

    @Override
    public UserRedisDto getUser(String userId) {
        String key = USER_KEY_PREFIX + userId;
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return objectMapper.convertValue(value, UserRedisDto.class);
    }

    @Override
    public void saveJwtBlackList(String jwt, int expiration) {
        redisTemplate.opsForValue().set(BLACK_LIST_PREFIX + jwt, "", expiration, TimeUnit.SECONDS);
    }

    @Override
    public boolean isJwtBlackList(String jwt) {
        return redisTemplate.hasKey(BLACK_LIST_PREFIX + jwt);
    }

}
