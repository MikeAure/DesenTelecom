package com.lu.gademo.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.lu.gademo.mapper.crm",
        sqlSessionTemplateRef = "crmSqlSessionTemplate")
public class CrmMybatisConfig {
    @Bean(name = "crmSqlSessionFactory")
    @Primary
    public SqlSessionFactory crmSqlSessionFactory(@Qualifier("crmDataSource") DataSource crmDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(crmDataSource);
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath:mapper/primary/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "crmMybatisTransactionManager")
    @Primary
    public DataSourceTransactionManager crmMybatisTransactionManager(@Qualifier("crmDataSource")DataSource crmDataSource) {
        return new DataSourceTransactionManager(crmDataSource);
    }

    @Bean(name = "crmSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate crmSqlSessionTemplate(@Qualifier("crmSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
