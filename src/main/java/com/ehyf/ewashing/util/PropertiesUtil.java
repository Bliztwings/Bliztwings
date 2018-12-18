package com.ehyf.ewashing.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesUtil {

	/**
	 * 当前解析的属性文件
	 */
	private Properties properties = null;
	/**
	 * 构造函数
	 * @param propFileClassPath 文件路径.
	 */
	public PropertiesUtil(final String propFileClassPath) {
		String path = propFileClassPath;
		if (StringUtils.isEmptyString(path)) {
			throw new RuntimeException("属性文件参数不不对");
		}
		if(!path.startsWith("/")) {
			path = "/" + path;
		}
		
		final InputStream in = this.getClass().getResourceAsStream(path);		
		this.properties = new Properties();
		try {
			this.properties.load(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 构造函数
	 * @param propfile 属性文件.
	 */
	public PropertiesUtil(final File propfile) {
		try {
			final InputStream in = new FileInputStream(propfile);
			this.properties = new Properties();
			this.properties.load(in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取属性文件
	 * @param propertyName 属性key.
	 * @return 属性值
	 */
	public String getProperty(final String propertyName) {
		return this.properties.getProperty(propertyName);
	}
	
	public Properties getProperties() {
		return this.properties;
	}
	
	
}
