package com.hmdp.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gmydl
 * @title: RedisCacheClient
 * @projectName yelp
 * @description: TODO
 * @date 2023/4/21 16:33
 */

@Component
@Slf4j
public class RedisCacheClient {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public void set(String key, Object value){
        
    }
















}
