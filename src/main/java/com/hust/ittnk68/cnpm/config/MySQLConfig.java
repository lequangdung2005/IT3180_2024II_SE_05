package com.hust.ittnk68.cnpm.config;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Properties;

import com.hust.ittnk68.cnpm.exception.ConfigFileException;

public class MySQLConfig {
	private final String configFilePath = "MySQL.conf";

	private String driver;
	private String url;
	private String username;
	private String password;
	

	public MySQLConfig() throws NoSuchFileException, IOException, ConfigFileException {
		MySQLConfigFileHandler cfh = new MySQLConfigFileHandler();
		MySQLDefaultConfig dc = new MySQLDefaultConfig();
		Properties props = cfh.getConfigProperties("MySQL.conf", dc);

		driver = props.getProperty("driver");
		url = props.getProperty("url");
		username = props.getProperty("username");
		password = props.getProperty("password");
	}

	public String getConfigFilePath() {
		return configFilePath;
	}
	public String getDriver() {
		return driver;
	}
	public String getUrl() {
		return url;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}

}
