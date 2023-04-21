package com.hmdp;

import cn.hutool.core.util.PhoneUtil;
import com.hmdp.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class YelpApplicationTests {

    @Resource
    private AppConfig appConfig;


    public static void main(String[] args) {
        String phone = "15664091819";
        boolean isPhone = PhoneUtil.isPhone(phone);
        System.out.println(isPhone);
    }

    @Test
    public void testAppConfig(){
        final String excludePath = appConfig.getExcludePath();
        final String[] split = excludePath.split(",");
        System.out.println(split);
    }


}
