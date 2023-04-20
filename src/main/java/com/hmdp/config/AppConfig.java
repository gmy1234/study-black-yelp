package com.hmdp.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author gmydl
 * @title: AppConfig
 * @projectName yelp
 * @description: TODO
 * @date 2023/4/20 16:20
 */
@Configuration
@Component
@ConfigurationProperties(prefix = "yelp")
@Data
public class AppConfig {

    @Value("${exclude-path}")
    private String excludePath;

}
