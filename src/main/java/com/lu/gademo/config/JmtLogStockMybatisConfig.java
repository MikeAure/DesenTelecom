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
@MapperScan(basePackages = "com.lu.gademo.mapper.jmtLogStock",
        sqlSessionTemplateRef = "jmtLogStockSqlSessionTemplate")
public class JmtLogStockMybatisConfig {
    @Bean(name = "jmtLogStockSqlSessionFactory")
    @Primary
    public SqlSessionFactory jmtLogStockSqlSessionFactory(@Qualifier("jmtLogStockDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "jmtLogStockMybatisTransactionManager")
    @Primary
    public DataSourceTransactionManager jmtLogStockMybatisTransactionManager(@Qualifier("jmtLogStockDataSource")DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "jmtLogStockSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate dataPlatformSqlSessionTemplate(@Qualifier("jmtLogStockSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
