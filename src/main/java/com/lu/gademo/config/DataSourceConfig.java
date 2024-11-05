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
    // 设置Primary作为默认Bean
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

    @Bean(name = "dataPlatformDataSource")
    @Qualifier("dataPlatformDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dataplatform")
    public DataSource dataPlatformDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "userLogDataSource")
    @Qualifier("userLogDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.userlog")
    public DataSource userLogDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jmtLogStockDataSource")
    @Qualifier("jmtLogStockDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.jmtlogstock")
    public DataSource jmtLogStockLogDataSource() {
        return DataSourceBuilder.create().build();
    }
}
