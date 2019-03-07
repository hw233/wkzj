package com.jtang.gameserver.server.codec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.jtang.core.context.SpringContext;
import com.jtang.core.mina.codec.server.InnerServerDecoder;
import com.jtang.core.protocol.Request;
import com.jtang.core.protocol.Response;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.utility.BufferFactory;
import com.jtang.gameserver.server.firewall.Firewall;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 继承内网服务器的解码类。重写解 析Request对象方法.
 * 
 * @author 0x737263
 * 
 */
public class GatewayServerDecoder extends InnerServerDecoder {
	private static final Log LOGGER = LogFactory.getLog(GatewayServerDecoder.class);
	
	//module(1) + cmd(1) + hashCode(4)
	private static int SKIP_BYTES_LEN = 6;
	
	private Firewall firewall = (Firewall) SpringContext.getBean(Firewall.class);
	
	private PlayerSession playerSession = (PlayerSession) SpringContext.getBean(PlayerSession.class);

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

		if (bytes.length < 4) {
			LOGGER.error(String.format("bufferSize: [%d] too short.", bytes.length));
			return null;
		}

		IoBuffer byteBuffer = null;
		try {
//			byteBuffer = ByteBuffer.wrap(bytes);
			byteBuffer = BufferFactory.getIoBuffer(bytes);

			// module(1) + cmd(1) + hashCode(4) + value(n)
			byte module = byteBuffer.get();
			byte cmd = byteBuffer.get();
			int hashCode = byteBuffer.getInt();

			int len = bytes.length - SKIP_BYTES_LEN;
			byte[] valueData = new byte[len];
			byteBuffer.get(valueData);
			byteBuffer.clear();

			int cacleValue = cacleHashCode(valueData);

			if (hashCode != cacleValue) {
				session.write(Response.valueOf(module, cmd, StatusCode.DATA_VALIDATE_ERROR));

				LOGGER.warn(String.format("hashcode error: [module: %d, cmd: %d, hashCode: %d, calcHashCode: %d]", module, cmd, hashCode, cacleValue));
				if (firewall.isEnableFirewall() && firewall.blockedByAuthCodeErrors(session, 1)) {
					LOGGER.warn(String.format("In blacklist: [ip: %s]", playerSession.getRemoteIp(session)));
					playerSession.closeIoSession(session, true);
				}
				return null;
			}

			return Request.valueOf(module, cmd, valueData);

		} catch (Exception ex) {
			LOGGER.error("decode exception: ", ex);
		} finally {
			byteBuffer = null;
		}

		return null;
	}
	
	private static int seed = 0x1000193;

	private static int cacleHashCode(byte[] data) {

		int hash = 0x811c9dc5;
		int j = data.length;
		for (int i = 0; i < j; ++i) {
			byte b = data[i];
			hash = (hash ^ b) * seed;
		}

		hash += hash << 7;
		hash ^= hash >> 6;
		hash += hash << 3;
		hash ^= hash >> 8;
		hash += hash << 1;
		return hash;
	}
}
