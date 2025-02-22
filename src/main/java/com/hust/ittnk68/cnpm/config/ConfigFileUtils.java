package com.hust.ittnk68.cnpm.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileUtils {
    public static Properties getProps(String filePath) throws FileNotFoundException, IOException {
	String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	String configPath = String.format("%s/%s", rootPath, filePath);
	Properties props = new Properties();
        props.load(new FileInputStream(configPath));
        return props;
    }
}
