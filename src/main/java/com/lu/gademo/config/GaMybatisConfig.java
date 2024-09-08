package com.lu.gademo.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.lu.gademo.mapper.ga",
        sqlSessionTemplateRef = "gaSqlSessionTemplate")
public class GaMybatisConfig {
    @Bean(name = "gaSqlSessionFactory")
    @Primary
    public SqlSessionFactory gaSqlSessionFactory(@Qualifier("gaDataSource")DataSource gaDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(gaDataSource);
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath:mapper/primary/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "gaMybatisTransactionManager")
    @Primary
    public DataSourceTransactionManager gaMybatisTransactionManager(@Qualifier("gaDataSource")DataSource gaDataSource) {
        return new DataSourceTransactionManager(gaDataSource);
    }

    @Bean(name = "gaSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate gaSqlSessionTemplate(@Qualifier("gaSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
