package com.lav.dsite.service;

import com.lav.dsite.dto.UserRedisDto;

public interface RedisService {

    void saveUser(UserRedisDto userRedisDto);

    UserRedisDto getUser(String userId);

    void saveJwtBlackList(String jwt, int expiration);

    boolean isJwtBlackList(String jwt);
}