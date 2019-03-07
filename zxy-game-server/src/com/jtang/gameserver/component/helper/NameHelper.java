package com.jtang.gameserver.component.helper;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 名称效验工具类
 * @author 0x737263
 *
 */
public class NameHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(NameHelper.class);
	/**
	 * 角色名字的最小长度 (单字节)
	 */
	public static final int MIN_ACTOR_NAME_LENTH = 3;
	
	/**
	 * 角色的名字最大长度 (单字节)
	 */
	public static final int MAX_ACTOR_NAME_LENTH = 14;
	
	/** 限制的参数 */
	private static final String[] LIMIT_CHAR = { "%", ",", "*", "^", "#", "$", "&", ":", "_", "[", "]", "|" };

	/**
	 * 验证角色名字
	 * 
	 * @param actorName 角色名            
	 * @return {@code Boolean} true-可以使用, false-不可以使用
	 */
	public static boolean validateActorName(String actorName) {
		return validateActorName(actorName, MIN_ACTOR_NAME_LENTH, MAX_ACTOR_NAME_LENTH);
	}
	
	public static boolean validateActorName(String actorName, int minLen, int maxLen) {
		if (validStrLength(actorName, minLen, maxLen)) {
			return validNamePattern(actorName);
		}
		return false;
	}

	/**
	 * 验证名字的格式
	 * @param name	需要验证的名字
	 * @return {@code Boolean} true-可以使用, false-不可以使用
	 */
	private static boolean validNamePattern(String name) {
		if(name == null || name.isEmpty()) {
			return false;
		}
		
		for (String element : LIMIT_CHAR) {
			if (name.contains(element)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 校验字符串的长度
	 * 
	 * @param element		字符串元素
	 * @param minLength		最小长度
	 * @param maxLength		最大长度
	 * @return {@code Boolean} true-合法的, false-不合法的
	 */
	private static boolean validStrLength(String element, int minLength, int maxLength) {		
		if(element == null || element.isEmpty()) {
			element = "";	
		}

		try {
			return validLenth(toEncode(element, "GB18030"), minLength, maxLength);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(element + "{}", e);
		}
		
		try {
			return validLenth(toEncode(element, "GB2312"), minLength, maxLength);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(element + "{}", e);
		}
		
		try {
			return validLenth(toEncode(element, "GBK"), minLength, maxLength);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(element + "{}", e);
		}
		
		return validLenth(toDefaultEncoding(element), minLength, maxLength);
	}

	/**
	 * 验证字符串长度是否在区域范围内
	 * 
	 * @param length
	 * @param min
	 * @param max
	 * @return
	 */
	private static boolean validLenth(int length, int min, int max) {
		return length >= min && length <= max;
	}

	/**
	 * 构建指定编码格式
	 * @param element		字符串
	 * @param encoding		编码格式
	 * @return {@code Integer} 字符串长度
	 * @throws UnsupportedEncodingException
	 */
	private static int toEncode(String element, String encoding) throws UnsupportedEncodingException {
		return element.getBytes(encoding).length;
	}

	/**
	 * 构建指定编码格式
	 * 
	 * @param element	字符串
	 * @return {@code Integer} 字符串长度
	 * @throws UnsupportedEncodingException
	 */
	private static int toDefaultEncoding(String element) {
		return element.getBytes().length;
	}
}