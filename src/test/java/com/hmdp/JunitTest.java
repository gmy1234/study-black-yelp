package com.hmdp;

import com.hmdp.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @Description:
 * @Author gmyDL
 * @Date 2023/4/20 21:09
 */
@Slf4j
@SpringBootTest
public class JunitTest {

    @Autowired
    AppConfig appConfig;

    @Value("${yelp.exclude-path}")
    String path;

    @Value("${yelp.exclude}")
    String age;

    @Test
    public void test(){
        System.out.println(age);
    }

}
