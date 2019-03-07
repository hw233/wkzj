package com.jtang.core.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

	/**
	 * 读取properties文件
	 * @param filePath
	 * @return
	 */
	public static Properties read(String filePath) {
		Properties properties = new Properties();
		InputStream is = PropertiesUtils.class.getClassLoader().getResourceAsStream(filePath);
		if (is == null) {
			LOGGER.error(String.format("[%s] file path not found.", filePath));
			return null;
		}

		try {
			properties.load(is);
			is.close();
		} catch (IOException e) {
			LOGGER.error(String.format("[%s] file load error.", filePath));
			return null;
		} finally {

		}

		return properties;
	}
	
	/**
	 * 获取整型值
	 * @param p
	 * @param name
	 * @return
	 */
	public static int getInt(Properties p, String name) {
		String str = p.getProperty(name).trim();
		if (str == null || str.isEmpty()) {
			return 0;
		}
		return Integer.valueOf(p.getProperty(name).trim());
	}
	
	
	/**
	 * @param p
	 * @param name
	 * @return
	 */
	public static long getLong(Properties p, String name) {
		String str = p.getProperty(name).trim();
		if (str == null || str.isEmpty()) {
			return 0;
		}
		return Long.valueOf(p.getProperty(name).trim());
	}
	
	/**
	 * 获取字符串值
	 * @param p
	 * @param name
	 * @return
	 */
	public static String getString(Properties p, String name) {
		return p.getProperty(name).trim();
	}
	
	/**
	 * 逗号分隔获取字符串List
	 * @param p
	 * @param name
	 * @return
	 */
	public static List<String> dotSplitStringList(Properties p, String name) {
		return getStringList(p, name, ",");
	}
	
	public static List<String> getStringList(Properties p, String name, String splitChar) {
		if (name == null || name.isEmpty()) {
			return new ArrayList<>();
		}
		String splitString = getString(p, name);
		if (splitString == null || splitString.isEmpty()) {
			return new ArrayList<>();
		}

		String[] splitArray = splitString.split(splitChar.trim());
		List<String> list = new ArrayList<>();
		for (String s : splitArray) {
			list.add(s);
		}
		return list;
	}
	
	/**
	 * 逗号分隔获取整型List
	 * @param p
	 * @param name
	 * @return
	 */
	public static List<Integer> dotSplitIntList(Properties p, String name) {
		return getIntList(p, name, ",");
	}
	
	public static List<Integer> getIntList(Properties p, String name, String splitChar) {
		if (name == null || name.isEmpty()) {
			return new ArrayList<>();
		}
		String splitString = getString(p, name);
		if (splitString == null || splitString.isEmpty()) {
			return new ArrayList<>();
		}

		String[] splitArray = splitString.split(splitChar.trim());
		List<Integer> list = new ArrayList<>();
		for (String s : splitArray) {
			list.add(Integer.valueOf(s));
		}
		return list;
	}
	
}
