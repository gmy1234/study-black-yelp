package com.hmdp;

import cn.hutool.core.collection.CollUtil;
import com.hmdp.config.AppConfig;
import com.hmdp.utils.RedisIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Resource
    private RedisIdWorker redisIdWorker;

    private ExecutorService es = Executors.newFixedThreadPool(500);

    @Test
    public void test(){
        String excludePath = appConfig.getExcludePath();
        String[] path = excludePath.split(",");
        ArrayList<String> pathList = CollUtil.toList(path);

    }

    @Test
    public void testID() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);

        Runnable task = () ->{
            for (int i = 0; i < 100; i++) {
                long order = redisIdWorker.nextId("order");
                System.out.println("id" + order);
                latch.countDown();
            }
        };
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("over:" + (end - begin));

    }

}
