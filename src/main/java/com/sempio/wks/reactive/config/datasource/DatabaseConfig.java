package com.sempio.wks.reactive.config.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.sempio.wks.reactive.config.datasource.properties.DatabaseProperties;
import com.sempio.wks.reactive.config.datasource.properties.WkSDatabaseProperties;

public abstract class DatabaseConfig {

	public abstract DataSource dataSource();

	abstract void initialize(org.apache.tomcat.jdbc.pool.DataSource dataSource);

    protected void configureDataSource(org.apache.tomcat.jdbc.pool.DataSource dataSource, DatabaseProperties databaseProperties) {
    	dataSource.setDriverClassName(databaseProperties.getDriverClassName());
    	dataSource.setUrl(databaseProperties.getUrl());
    	dataSource.setUsername(databaseProperties.getUserName());
    	dataSource.setPassword(databaseProperties.getPassword());
        dataSource.setMaxActive(databaseProperties.getMaxActive());
        dataSource.setMaxIdle(databaseProperties.getMaxIdle());
        dataSource.setMinIdle(databaseProperties.getMinIdle());
        dataSource.setMaxWait(databaseProperties.getMaxWait());
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);

		if(databaseProperties.isInitialize())
			initialize(dataSource);
    }
}

@Configuration
@EnableConfigurationProperties(WkSDatabaseProperties.class)
class WKSDatabaseConfig extends DatabaseConfig {
	
	@Autowired
	private WkSDatabaseProperties wksDatabaseProperties;

	@Override
	protected void initialize(org.apache.tomcat.jdbc.pool.DataSource dataSource) {		
	}

	@Bean(name = "wksDataSource", destroyMethod = "close")
	@Primary
	public DataSource dataSource() {
		org.apache.tomcat.jdbc.pool.DataSource userDataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		configureDataSource(userDataSource, wksDatabaseProperties);
		return userDataSource;
	}

	@Bean(name = "wksTransactionManager")
	@Primary
	public PlatformTransactionManager userTransactionManager(@Qualifier("wksDataSource") DataSource wksDataSource) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(wksDataSource);
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}
}
