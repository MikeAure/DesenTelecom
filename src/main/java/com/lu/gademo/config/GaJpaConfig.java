package com.lu.gademo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "gaEntityManagerFactory",
        transactionManagerRef = "gaJpaTransactionManager",
        basePackages = {"com.lu.gademo.dao.ga"})
public class GaJpaConfig {

    @Primary
    @Bean(name = "gaEntityManagerFactory")    //primary实体工厂
    public LocalContainerEntityManagerFactoryBean gaEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("gaDataSource") DataSource gaDataSource
    ) {
        return builder
                .dataSource(gaDataSource)
                .packages("com.lu.gademo.entity.ga")     //换成你自己的实体类所在位置
                .persistenceUnit("ga")
                .build();
    }

    @Primary
    @Bean(name = "gaJpaTransactionManager")
    public PlatformTransactionManager gaJpaTransactionManager(
            @Qualifier("gaEntityManagerFactory") EntityManagerFactory gaEntityManagerFactory
    ) {
        return new JpaTransactionManager(gaEntityManagerFactory);
    }


}