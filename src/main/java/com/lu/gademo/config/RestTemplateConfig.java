package com.lu.gademo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置读取超时时间，建立连接后如果5秒没有返回结果则抛出异常
        factory.setReadTimeout(5000);
        // 设置连接超时时间，如果15秒没有建立连接则抛出异常
        factory.setConnectTimeout(10000);
        // 设置代理
        //factory.setProxy(null);
        return factory;
    }
}
