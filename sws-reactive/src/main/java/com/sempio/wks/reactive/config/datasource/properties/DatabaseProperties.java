package com.sempio.wks.reactive.config.datasource.properties;

public interface DatabaseProperties {		
	public String getDriverClassName();
	
	public String getUrl();
	
	public String getUserName();
	
	public String getPassword();
	
	public boolean isInitialize();
	
	public int getInitialSize();
	
	public int getMaxActive();
	
	public int getMaxIdle();
	
	public int getMinIdle();
	
	public int getMaxWait();
}
