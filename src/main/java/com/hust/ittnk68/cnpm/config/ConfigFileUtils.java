package com.hust.ittnk68.cnpm.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigFileUtils {

    public static Properties getProps(String filePath) throws FileNotFoundException, IOException {

	try (	InputStream in = ConfigFileUtils.class.getResourceAsStream(filePath); )
	{
	    Properties props = new Properties();
	    props.load (in);
	    return props;
	}
    }

}
