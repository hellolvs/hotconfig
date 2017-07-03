package com.qunar.hotconfig.service.impl;

import com.danga.MemCached.MemCachedClient;
import com.qunar.hotconfig.service.MemCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by kun.ji on 2017/4/6.
 */
@Service
public class MemcacheServiceImpl implements MemCacheService {

    @Autowired
    private MemCachedClient memCachedClient;

    @Override
    public void set(String key, Object value, long time) {
        memCachedClient.set(key, value, new Date(time));
    }

    @Override
    public void set(String key, Object value) {
        memCachedClient.set(key, value, new Date(24L * 60 * 60 * 1000));
    }

    @Override
    public Object get(String key) {
        return memCachedClient.get(key);
    }

    @Override
    public void delete(String key) {
        memCachedClient.delete(key);
    }
}
