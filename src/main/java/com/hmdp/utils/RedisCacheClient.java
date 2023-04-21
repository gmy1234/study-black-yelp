package com.hmdp.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.constants.RedisConstants;
import com.hmdp.entity.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.hmdp.constants.RedisConstants.CACHE_NULL_TTL;

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



    public void set(String key, Object value, Long time, TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }


    public void setWithLogicExpire(String key, Object value, Long time, TimeUnit unit){
        // 设置逻辑过期时间
        RedisData redisData = RedisData.builder()
                .data(value)
                .expireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)))
                .build();
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }


    /**
     * redis读数据，避免缓存穿透
     * @param keyPrefix key 前缀
     * @param id 数据id
     * @param tClass 数据Class
     * @param dbFallback 读数据库操作函数
     * @param time 时间
     * @param unit 时间单位
     * @return 数据
     * @param <T> 数据Class 或者null
     * @param <ID> ID的类型
     */
    public <T, ID> T getPassThrough(String keyPrefix, ID id, Class<T> tClass, Function<ID, T> dbFallback, Long time, TimeUnit unit){
        String key = keyPrefix + id;
        String TJson = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(TJson)){
            return  JSONUtil.toBean(TJson, tClass);
        }
        // 命中的是否为""
        if ("".equals(TJson)) {
            return null;
        }

        // 不存在，查数据库，并写入空值
        T dbInfo = dbFallback.apply(id);
        if (Objects.isNull(dbInfo)){
            this.set(key, "", time, unit);
            return null;
        }

        //存在 写入
        this.set(key, dbInfo, time, unit);
        return dbInfo;
    }

    public static final ExecutorService CHCHE_REBUILD_EXCEUTOR = Executors.newFixedThreadPool(10);

    /**
     * 缓存击穿
     * @param keyPrefix
     * @param id
     * @param tClass
     * @param dbFallback
     * @param time
     * @param unit
     * @return
     * @param <T>
     * @param <ID>
     */
    public <T, ID> T getMutix(String keyPrefix, ID id, Class<T> tClass, Function<ID, T> dbFallback, Long time, TimeUnit unit){
        String key = keyPrefix + id;
        String TJson = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(TJson)){
            return  JSONUtil.toBean(TJson, tClass);
        }
        // 命中的是否为""
        if ("".equals(TJson)) {
            return null;
        }
        // redis不存在

       // todo
        return null;
    }



    private boolean tryLock(String key){
        Boolean flag = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, "1", 10, TimeUnit.MICROSECONDS);

        return BooleanUtil.isTrue(flag);
    }

    private void unLock(String key){
        stringRedisTemplate.delete(key);
    }













}
