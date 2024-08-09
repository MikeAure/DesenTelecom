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
@EnableJpaRepositories(entityManagerFactoryRef = "crmEntityManagerFactory",
        transactionManagerRef = "crmTransactionManager",
        basePackages = {"com.lu.gademo.dao.crm"})
public class CrmJpaConfig {

    @Bean(name = "crmEntityManagerFactory")    //primary实体工厂
    public LocalContainerEntityManagerFactoryBean crmEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("crmDataSource") DataSource crmDataSource
    ) {
        return builder
                .dataSource(crmDataSource)
                .packages("com.lu.gademo.entity.crm")     //换成你自己的实体类所在位置
                .persistenceUnit("crm")
                .build();
    }

    @Bean(name = "crmTransactionManager")
    public PlatformTransactionManager crmTransactionManager(
            @Qualifier("crmEntityManagerFactory") EntityManagerFactory crmEntityManagerFactory
    ) {
        return new JpaTransactionManager(crmEntityManagerFactory);
    }
}
