package com.qunar.hotconfig.service;

/**
 * Created by kun.ji on 2017/4/6.
 */
public interface MemCacheService {

    void set(String key, Object value, long time);

    void set(String key, Object value);

    Object get(String key);

    void delete(String key);

}
