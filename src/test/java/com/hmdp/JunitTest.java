package com.hmdp;

import com.hmdp.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @Description:
 * @Author gmyDL
 * @Date 2023/4/20 21:09
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class JunitTest {

    @Resource
    AppConfig appConfig;

    @Value("${yelp.exclude-path}")
    String path;

    @Test
    public void test(){
        System.out.println(path);
        String excludePath = appConfig.getExcludePath();
        System.out.println(excludePath);
    }

}
