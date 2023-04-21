package com.hmdp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author gmydl
 * @title: AppConfig
 * @projectName yelp
 * @description: myAppConfig
 * @date 2023/4/20 16:20
 */
@Configuration
@Component("myAppConfig")
@ConfigurationProperties(prefix = "yelp")
@Data
public class AppConfig {

    private String excludePath;

}
