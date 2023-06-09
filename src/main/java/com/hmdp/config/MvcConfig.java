package com.hmdp.config;

import cn.hutool.core.collection.CollUtil;
import com.hmdp.interceptor.LoginInterceptor;
import com.hmdp.interceptor.RefreshInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author gmydl
 * @title: MvcConfig
 * @projectName yelp
 * @description: 自定义拦截器
 * @date 2023/4/20 16:12
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    AppConfig appConfig;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 拦截所有请求,并刷新token
        registry.addInterceptor(new RefreshInterceptor(stringRedisTemplate))
                .excludePathPatterns("/**")
                .order(0);

        // 拦截登陆请求
        String excludePath = appConfig.getExcludePath();
        String[] path = excludePath.split(",");
        ArrayList<String> pathList = CollUtil.toList(path);
        registry.addInterceptor(new LoginInterceptor())
//                .excludePathPatterns(pathList)
                .excludePathPatterns("/**")
                .order(1);
    }
}
