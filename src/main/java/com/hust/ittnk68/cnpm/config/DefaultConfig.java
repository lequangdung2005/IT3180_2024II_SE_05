package com.hust.ittnk68.cnpm.config;

import java.util.Properties;

public interface DefaultConfig {
	boolean keyExists(String key);
	String getProperty(String key);
	Properties getDefaultProperties();
}
