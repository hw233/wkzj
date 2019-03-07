package com.jtang.core.utility;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZipUtil {
	
	public static final boolean NOWRAP = true;
	/**
	 * 压缩
	 * @param in
	 * @return
	 */
	final static public byte[] compress(byte[] in) {
		byte[] out = new byte[in.length];
		Deflater de = new Deflater(Deflater.BEST_SPEED, NOWRAP);
		de.setInput(in);
		de.finish();
		int length = de.deflate(out);
		byte[] filter = new byte[length];
		System.arraycopy(out, 0, filter, 0, length);
		return filter;
	}
	
	
	/**
	 * 解压
	 * @param in
	 * @param len
	 * @return
	 */
	final static public byte[] unCompress(byte[] in, int len) {
		Inflater decompresser = new Inflater(NOWRAP);
		decompresser.setInput(in, 0, in.length);
		byte[] result = new byte[len];
		int length = 0;
		byte[] filter = null;
		try {
			length = decompresser.inflate(result);
			filter = new byte[length];
			System.arraycopy(result, 0, filter, 0, length);
		} catch (DataFormatException e) {
			e.printStackTrace();
		}
		decompresser.end();
		return filter;
	}
}
