package com.jtang.core.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 序列化对象转换类
 * @author 0x737263
 *
 */
public class ObjectConverter {
	private static final Log LOGGER = LogFactory.getLog(ObjectConverter.class);

	/**
	 * 对象转byte[]
	 * @param obj	对象
	 * @return
	 */
	public static byte[] object2ByteArray(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			new ObjectOutputStream(bos).writeObject(obj);

			return bos.toByteArray();
		} catch (IOException ex) {
			LOGGER.error("failed to serialize obj", ex);
		}
		return null;
	}

	/**
	 * byte[]转 对象
	 * @param buffer
	 * @return
	 */
	public static Object byteArray2Object(byte[] buffer) {
		if ((buffer == null) || (buffer.length == 0)) {
			return null;
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception ex) {
			LOGGER.error("failed to deserialize obj", ex);
			return null;
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (Exception ex) {
			}
			try {
				bais.close();
			} catch (Exception ex) {
			}
		}
	}
}