package com.lu.gademo.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Security;

@Component
public class BouncyCastleProviderRegistrar {
    @PostConstruct
    public void init() {
        // 判断是否已存在该提供者，若不存在则添加
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
            System.out.println("BouncyCastleProvider 已添加");
        } else {
            System.out.println("BouncyCastleProvider 已存在");
        }
    }

}
