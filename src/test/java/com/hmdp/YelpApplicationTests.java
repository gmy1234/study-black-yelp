package com.hmdp;

import cn.hutool.core.util.PhoneUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class YelpApplicationTests {


    public static void main(String[] args) {
        String phone = "15664091819";
        boolean isPhone = PhoneUtil.isPhone(phone);
        System.out.println(isPhone);
    }


}
