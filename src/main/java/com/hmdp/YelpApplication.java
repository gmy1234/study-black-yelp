package com.hmdp;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;


@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.hmdp.mapper")
@SpringBootApplication
@Slf4j
public class YelpApplication {

    public static void main(String[] args) throws UnknownHostException {
//        SpringApplication.run(YelpApplication.class, args);
        ApplicationContext ctx = SpringApplication.run(YelpApplication.class, args);
        Environment env = ctx.getEnvironment();
        String[] activeProfiles = env.getActiveProfiles();
        activeProfiles = activeProfiles != null ? activeProfiles : new String[]{"dev"};
        log.info("profiles.size {}, current profiles {}", activeProfiles.length, activeProfiles);
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Local: \t\thttp://localhost:{}\n\t"
                        + "External: \thttp://{}:{}\n\t"
                        +
                env.getProperty("spring.application.name"), env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port"), InetAddress.getLocalHost().getHostAddress(),env.getProperty("server.port"));

    }

}
