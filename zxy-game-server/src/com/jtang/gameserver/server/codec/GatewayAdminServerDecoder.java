package com.jtang.gameserver.server.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.jtang.core.mina.codec.server.InnerServerDecoder;
import com.jtang.core.protocol.Request;
import com.jtang.core.utility.BufferFactory;

/**
 * 继承内网服务器的解码类。重写解 析Request对象方法.
 * 
 * @author 0x737263
 * 
 */
public class GatewayAdminServerDecoder extends InnerServerDecoder {

	//module(1) + cmd(1)
	private static int SKIP_BYTES_LEN = 2;
	
	/**
	 * 解析byte[]为Request对象.
	 * 
	 * @param session
	 * @param bytes 已脱包头的buffer
	 *         
	 * @return
	 */
	@Override
	protected Object parseRequest(IoSession session, byte[] bytes) {
		if (bytes == null) {
			LOGGER.error("buffer is null.");
			return null;
		}

		if (bytes.length < 2) {
			LOGGER.warn(String.format("bufferSize: [%d] too short.", bytes.length));
			return null;
		}

		IoBuffer byteBuffer = null;
		try {
//			byteBuffer = ByteBuffer.wrap(bytes);
			byteBuffer = BufferFactory.getIoBuffer(bytes);

			// module(1) + cmd(1) + value(n)
			byte module = byteBuffer.get();
			byte cmd = byteBuffer.get();

			int len = bytes.length - SKIP_BYTES_LEN;
			byte[] valueData = new byte[len];
			byteBuffer.get(valueData);
			byteBuffer.clear();

			return Request.valueOf(module, cmd, valueData);

		} catch (Exception ex) {
			LOGGER.error("decode exception: ", ex);
		} finally {
			byteBuffer = null;
		}

		return null;
	}
}
