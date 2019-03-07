package test;

import java.math.BigInteger;

public class Test {
	// public static void main(String[] args) {
	// int time = TimeUtils.getNow();
	// short serverId = 10010;
	// System.out.println(1 << 48);
	// }

	/**
	 * baseString 递归调用
	 * 
	 * @param num
	 *            十进制数
	 * @param base
	 *            要转换成的进制数
	 */
	public static String baseString(int num, int base) {
		String str = "", digit = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (num == 0) {
			return "";
		} else {
			str = baseString(num / base, base);
			return str + digit.charAt(num % base);
		}
	}

	public static String baseString(BigInteger num, int base) {
		String str = "", digit = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (num.shortValue() == 0) {
			return "";
		} else {
			BigInteger valueOf = BigInteger.valueOf(base);
			str = baseString(num.divide(valueOf), base);
			return str + digit.charAt(num.mod(valueOf).shortValue());
		}
	}

	public static void main(String[] args) {
		long actorId = 86079702181L;
		System.out.println(baseString(1295, 36));
		BigInteger big = new BigInteger(String.valueOf(actorId));
		System.out.println(baseString(big, 36));
	}
}
