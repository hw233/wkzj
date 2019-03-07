package com.jtang.core.utility;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * xml工具类
 * @author 0x737263
 *
 */
public class XmlUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtils.class);

	/**
	 * 流对象写入到list
	 * @param list
	 * @param fos
	 * @throws IOException
	 */
	public static void write(Collection<?> list, OutputStream fos) throws IOException {
		XMLEncoder encoder = new XMLEncoder(fos);

		for (Object o : list) {
			encoder.writeObject(o);
		}

		encoder.flush();
		encoder.close();
		fos.close();
	}

	/**
	 * 读取留,返回一个列表
	 * @param fis
	 * @return
	 * @throws IOException
	 */
	public static List<?> read(InputStream fis) throws IOException {
		List<Object> objList = new ArrayList<Object>();
		XMLDecoder decoder = new XMLDecoder(fis);
		Object obj = decoder.readObject();
		while (obj != null) {
			objList.add(obj);
			try {
				obj = decoder.readObject();
			} catch (ArrayIndexOutOfBoundsException e) {
				LOGGER.error("{}", e);
				decoder.close();
			}
		}
		fis.close();
		return objList;
	}
}