package com.lu.gademo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Gademo4Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Gademo4Application.class, args);

    }
}
