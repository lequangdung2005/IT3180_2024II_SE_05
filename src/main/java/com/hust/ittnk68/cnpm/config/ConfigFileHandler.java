package com.hust.ittnk68.cnpm.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.hust.ittnk68.cnpm.exception.ConfigFileException;

public interface ConfigFileHandler {
	Properties getConfigProperties(String filePath) throws FileNotFoundException, IOException;
	Properties getConfigProperties(String filePath, DefaultConfig dc) throws FileNotFoundException, IOException, ConfigFileException;
}
