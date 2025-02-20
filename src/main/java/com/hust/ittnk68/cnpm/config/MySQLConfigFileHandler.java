package com.hust.ittnk68.cnpm.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.hust.ittnk68.cnpm.exception.ConfigFileException;

public class MySQLConfigFileHandler implements ConfigFileHandler {
	public Properties getConfigProperties(String filePath) throws FileNotFoundException, IOException {
		return ConfigFileUtils.getProps(filePath);
	}
	public Properties getConfigProperties(String filePath, DefaultConfig dc) throws FileNotFoundException, IOException, ConfigFileException {
		Properties inputProps = getConfigProperties(filePath);
		Properties props = dc.getDefaultProperties();
		for(String key : inputProps.stringPropertyNames()) {
			if(!props.stringPropertyNames().contains(key)) {
				throw new ConfigFileException("Khong ton tai khoa <" + key + ">");
			}
			props.setProperty(key, inputProps.getProperty(key));
		}
		return props;
	}
}
