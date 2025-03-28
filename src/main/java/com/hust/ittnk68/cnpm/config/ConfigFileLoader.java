package com.hust.ittnk68.cnpm.config;

import java.util.Properties;

import com.hust.ittnk68.cnpm.exception.ConfigFileException;

public class ConfigFileLoader {
    private Properties props;
    public ConfigFileLoader(String filePath, DefaultConfig dc) {
	try {
	    Properties inputProps = ConfigFileUtils.getProps(filePath);
	    if(dc == null) {
		this.props = inputProps;
	    }
	    else {
		this.props = dc.getDefaultProperties();
		for(String key : inputProps.stringPropertyNames()) {
			if(!this.props.stringPropertyNames().contains(key)) {
				throw new ConfigFileException("Khong ton tai khoa <" + key + ">");
			}
			this.props.setProperty(key, inputProps.getProperty(key));
		}
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
    public ConfigFileLoader(String filePath) {
	this (filePath, null);
    }

    public String getProperty(String key) {
	return props.getProperty(key);
    }
}
