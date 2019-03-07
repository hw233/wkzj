package com.jtang.core.utility;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 枚举工具类
 * @author 0x737263
 *
 */
public class EnumUtils {
	
	private static Logger LOGGER = LoggerFactory.getLogger(EnumUtils.class);

	/**
	 * 获取枚举值
	 * @param enumClass		枚举类
	 * @param fieldName		字段名
	 * @return
	 */
	public static <T extends Enum<T>> T valueOf(Class<T> enumClass, String fieldName) {
		return Enum.valueOf(enumClass, fieldName);
	}

	/**
	 * 获取枚举对象
	 * @param enumClass	枚举类
	 * @param value		枚举值
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends Enum<T>> T getEnum(Class<T> enumClass, int value) {
		try {
			if (value < 0) {
				return null;
			}
			Method method = enumClass.getMethod("values", new Class[0]);
			Enum[] values = (Enum[]) method.invoke(enumClass, new Object[0]);
			if ((values != null) && (values.length > value)) {
				return (T)values[value];
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("building enum object  [Class: {} - Value: {} ] exception.", new Object[] { enumClass, Integer.valueOf(value), e });
		}
		return null;
	}
}