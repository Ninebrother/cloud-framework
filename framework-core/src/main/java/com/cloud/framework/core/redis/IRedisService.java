package com.cloud.framework.core.redis;

/**
 * @author zhangyl
 * @Date 2019/12/22
 */
public interface IRedisService {
    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime 毫秒
     * @return
     */
    boolean set(String key, Object value, Long expireTime);

    /**
     * 读取缓存
     *
     * @param key
     * @param T
     * @param <T>
     * @return
     */
    <T> T get(String key, Class<T> T);
}
