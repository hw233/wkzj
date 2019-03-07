package com.jtang.core.utility;

import java.io.ByteArrayOutputStream;

/**
 * 16进制工具类
 * @author 0x737263
 *
 */
public class HexUtils {
	
	private static String HEX_CHAR = "0123456789ABCDEF";

	/**
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 * @param str	字符串
	 * @return		16进制字符串
	 */
	public static String string2Hex(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(HEX_CHAR.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(HEX_CHAR.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/**
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 * @param HEX_CHAR	16进制字符串
	 * @return			字符串
	 */
	public static String hex2String(String hexString) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(hexString.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < hexString.length(); i += 2)
			baos.write((HEX_CHAR.indexOf(hexString.charAt(i)) << 4 | HEX_CHAR.indexOf(hexString.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	/**
	 * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * @param src
	 * @return
	 */
	public static String bytes2Hex(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	
	/**
	* Convert hex string to byte[]
	* @param hexString the hex string
	* @return byte[]
	*/
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	
	/**
	* Convert char to byte
	* @param c char
	* @return byte
	*/
	private static byte charToByte(char c) {
		return (byte) HEX_CHAR.indexOf(c);
	}
}