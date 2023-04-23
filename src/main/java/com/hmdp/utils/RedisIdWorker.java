package com.hmdp.utils;

import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author gmydl
 * @title: RedisIdWorker
 * @projectName yelp
 * @description: TODO
 * @date 2023/4/23 09:31
 */
@Component
public class RedisIdWorker {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final long BIRTH_TIMESTAMP = LocalDateTime
            .of(2023,1,1,0,0,0).toEpochSecond(ZoneOffset.UTC);

    public static final int COUNT_BITS = 32;


    public long nextId(String keyPrefix){
        // 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowTimeStamp = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowTimeStamp - BIRTH_TIMESTAMP;

        // 生成序列号
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long count = stringRedisTemplate.opsForValue()
                .increment("icr" + keyPrefix + date);

        return timestamp << COUNT_BITS | count;
    }


}
