package com.hmdp.strategy;

import ch.qos.logback.core.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author gmydl
 * @title: SimpleRedisLock
 * @projectName yelp
 * @description: 手动实现分布式锁
 * @date 2023/4/24 11:30
 */
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRedisLock implements Lock{

    private StringRedisTemplate stringRedisTemplate;

    private String lockName;

    public static final String KEY_PREFIX = "lick:";

    @Override
    public boolean tryLock(Long timeoutSec) {
        long threadId = Thread.currentThread().getId();
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + lockName, threadId + "",
                timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(flag);
    }

    @Override
    public void unLock() {
        stringRedisTemplate.delete(KEY_PREFIX + lockName);
        log.info("已释放锁 -> {}", KEY_PREFIX + lockName);
    }
}
