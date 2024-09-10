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
@MapperScan(basePackages = "com.lu.gademo.mapper.userlog",
        sqlSessionTemplateRef = "userLogSqlSessionTemplate")
public class UserLogMybatisConfig {
    @Bean(name = "userLogSqlSessionFactory")
    @Primary
    public SqlSessionFactory userLogSqlSessionFactory(@Qualifier("userLogDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath:mapper/primary/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "userLogMybatisTransactionManager")
    @Primary
    public DataSourceTransactionManager userLogMybatisTransactionManager(@Qualifier("userLogDataSource")DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "userLogSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate userLogSqlSessionTemplate(@Qualifier("userLogSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
