package com.lu.gademo.config;

import com.lu.gademo.trace.client.user.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Driver driver1() {
        return new Driver("driver1", "123456", 2, 34.247, 108.946);
    }
}
