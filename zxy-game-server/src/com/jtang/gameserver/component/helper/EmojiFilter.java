package com.jtang.gameserver.component.helper;

import com.jtang.core.utility.StringUtils;

public class EmojiFilter {
	
//	public static void main(String[] args) {
//		String s = "<body>😄213这是一个有各种内容的消息,  Hia Hia Hia !!!! xxxx@@@...*)!" + "(@*$&@(&#!)@*)!&$!)@^%@(!&#. 😄👩👨], ";
//		String c = filterEmoji(s);
//
//		if (s.equals(c) == false) {
//			System.out.print(c);
//		}
//	}
	
	/**
	 * 检测是否有emoji字符
	 * @param source
	 * @return
	 */
	public static boolean containsEmoji(String source) {
		if (StringUtils.isBlank(source)) {
			return false;
		}

		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isEmojiCharacter(codePoint)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 如果不存在emoji
	 * @param codePoint
	 * @return
	 */
	private static boolean isEmojiCharacter(char codePoint) {
		return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
	}
	
	
	/**
	 * 过滤掉emoji字符
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {
		StringBuilder buf = new StringBuilder();
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isEmojiCharacter(codePoint) == false) {
				buf.append(codePoint);
			}
		}
		return buf.toString();
	}

}
