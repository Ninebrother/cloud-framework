package com.cloud.framework.core.redis.impl;

import com.cloud.framework.core.redis.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyl on 2019/12/14.
 */
@Service
public class RedisService implements IRedisService {
    public static final Long EXPIRE_TIME = 10000L;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(String key, String value) {
        return this.set(key, value, EXPIRE_TIME);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(String key, String value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value, expireTime, TimeUnit.MILLISECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
