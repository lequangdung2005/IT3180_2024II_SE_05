package com.hust.ittnk68.cnpm.config;

import java.util.Properties;

public class MySQLDefaultConfig implements DefaultConfig {
	private static final Properties defaultProps;

	static {
		defaultProps = new Properties() {{
			put("driver", "com.mysql.cj.jdbc.Driver");
			put("url", "127.0.0.1:3306");
			put("username", "");
			put("password", "");
		}};
	}

	public boolean keyExists(String key) {
		return defaultProps.stringPropertyNames().contains(key);
	}

	public String getProperty(String key) {
		return defaultProps.getProperty(key);
	}

	public Properties getDefaultProperties() {
		Properties ret = new Properties();
		for(String key : defaultProps.stringPropertyNames()) {
			ret.put(key, defaultProps.getProperty(key));
		}
		return ret;
	}
}
