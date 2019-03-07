package com.jtang.core.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ByteArrayStreamUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ByteArrayStreamUtils.class);
			
	/**
	 * byte[]转换为InputStream
	 * @param buf
	 * @return
	 */
	public static InputStream byte2Input(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * inputStream转换为byte[]
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] input2byte(InputStream inputStream) {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		try {
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = inputStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			return swapStream.toByteArray();
		} catch (Exception ex) {

			LOGGER.warn("{}", ex);
			return null;
		} finally {
			try {
				swapStream.close();
				inputStream.close();
			} catch (IOException ex) {
				LOGGER.warn("{}", ex);
			}
		}
	}

}
