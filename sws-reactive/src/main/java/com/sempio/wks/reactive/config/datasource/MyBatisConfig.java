package com.sempio.wks.reactive.config.datasource;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.sempio.wks.reactive.config.datasource.annotaion.WKSMapper;

public class MyBatisConfig {
	public static final String BASE_PACKAGE_PREFIX = "com.sempio.wks.reactive";

	public static final String CONFIG_LOCATION_PATH = "classpath:config/mybatis-config.xml";

	public static final String MAPPER_LOCATIONS_PATH = "classpath:com/sempio/wks/reactive/mapper/*.xml";
	
	protected void configureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
		PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setTypeAliasesPackage(BASE_PACKAGE_PREFIX);
		sessionFactoryBean.setConfigLocation(pathResolver.getResource(CONFIG_LOCATION_PATH));
		sessionFactoryBean.setMapperLocations(pathResolver.getResources(MAPPER_LOCATIONS_PATH));
	}
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE_PREFIX, annotationClass = WKSMapper.class, sqlSessionFactoryRef = "wksSqlSessionFactory")
class wksMybatisConfig extends MyBatisConfig {

	@Bean(name = "wksSqlSessionFactory")
	@Primary
	public SqlSessionFactory wksSqlSessionFactory(@Qualifier("wksDataSource") DataSource wksDataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		configureSqlSessionFactory(sessionFactoryBean, wksDataSource);
		return sessionFactoryBean.getObject();
	}
}
