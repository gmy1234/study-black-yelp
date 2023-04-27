package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gmydl
 * @title: RedisConfig
 * @projectName yelp
 * @description: TODO
 * @date 2023/4/27 09:44
 */

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String pwd;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + port)
                .setPassword(pwd);
        return Redisson.create(config);
    }

}
