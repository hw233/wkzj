package des;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 自己做的JAVA和C通讯的AES加密和解密，供参考
 * @author yaodaqing
 *
 */
public class DesUtil {

	/**
	 * 密钥
	 */
	private static final String aesKey = "0123456789abcdef";
	
	/**
	 * 加密
	 * @param b
	 * @return
	 */
	public static byte[] encryptAES(byte[] b){
		if (aesKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        //判断Key是否为16位
        if (aesKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        try {
			byte[] raw = aesKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(b);
			return encrypted;
		} catch (Exception e) {
			System.out.println("数据加密时发生异常...");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解密
	 * @param b
	 * @return
	 * 由于C语言在加密时采用了模式，所以JAVA在解析时需要采用模式来解密
	 */
	public static byte[] decryptAES(byte[] b){
		//判断Key是否正确
        if (aesKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        //判断Key是否为16位
        if (aesKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        try {
			byte[] raw = aesKey.getBytes("ASCII");
			SecretKeySpec skp = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, skp);
			byte[] original = cipher.doFinal(b);
			return original;
		} catch (Exception e) {
			System.out.println("数据解密时发生异常...");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		byte[] test = new byte[1000];
		byte[] result = encryptAES(test);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			encryptAES(test);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			decryptAES(result);
		}
		end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}

