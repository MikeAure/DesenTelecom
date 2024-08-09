package com.lu.gademo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "gaDataSource")
    @Qualifier("gaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ga")
    @Primary
    public DataSource gaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "crmDataSource")
    @Qualifier("crmDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.crm")
    public DataSource crmDataSource() {
        return DataSourceBuilder.create().build();
    }
}
