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
@MapperScan(basePackages = "com.lu.gademo.mapper.dataplatform",
        sqlSessionTemplateRef = "dataPlatformSqlSessionTemplate")
public class DataPlatformMybatisConfig {
    @Bean(name = "dataPlatformSqlSessionFactory")
    @Primary
    public SqlSessionFactory crmSqlSessionFactory(@Qualifier("dataPlatformDataSource") DataSource crmDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(crmDataSource);
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath:mapper/primary/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "dataPlatformMybatisTransactionManager")
    @Primary
    public DataSourceTransactionManager crmMybatisTransactionManager(@Qualifier("dataPlatformDataSource")DataSource crmDataSource) {
        return new DataSourceTransactionManager(crmDataSource);
    }

    @Bean(name = "dataPlatformSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate crmSqlSessionTemplate(@Qualifier("dataPlatformSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
