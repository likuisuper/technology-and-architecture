package com.cxylk.service.impl;

import com.cxylk.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Classname RedisServiceImpl
 * @Description 注入StringRedisTemplate对redis进行操作
 * @Author likui
 * @Date 2020/12/6 15:19
 **/
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key,value);
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean expire(String key, long expire) {
        return stringRedisTemplate.expire(key,expire, TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key,delta);
    }
}
