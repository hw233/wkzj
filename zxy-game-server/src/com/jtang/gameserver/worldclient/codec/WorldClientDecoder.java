package com.jtang.gameserver.worldclient.codec;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.jtang.core.mina.codec.client.InnerClientDecoder;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.core.utility.BufferFactory;

/**
 * socket客户端字节方式的解码类. 用于内网服务器间通讯时建立客户端解码用
 * 
 * @author 0x737263
 * 
 */
public class WorldClientDecoder extends InnerClientDecoder {

	// module(1) + cmd(1) + actorId(8) + statusCode(2) + value(n)
	private static int SERVER_HEADER_LENGTH = 12;

	@Override
	protected Object parseResponse(IoSession session, byte[] bytes) {
		if (bytes == null) {
			LOGGER.error("buffer is null.");
			return null;
		}

		if (bytes.length < 4) {
			LOGGER.error(String.format("bufferSize: [%d] too short.",
					bytes.length));
			return null;
		}

		//ByteBuffer byteBuffer = null;
		IoBuffer byteBuffer = null;
		
			
		try {
			byteBuffer = BufferFactory.getIoBuffer(bytes);
			//byteBuffer = ByteBuffer.wrap(bytes);
			//byteBuffer.order(BufferFactory.BYTE_ORDER);

			byte module = byteBuffer.get();
			byte cmd = byteBuffer.get();
			long actorId = byteBuffer.getLong();
			
			short statusCode = byteBuffer.getShort();

			int len = bytes.length - SERVER_HEADER_LENGTH;
			byte[] valueData = new byte[len];
			byteBuffer.get(valueData);
			byteBuffer.clear();

			ActorResponse response = ActorResponse.valueOf(module, cmd);
			response.setActorId(actorId);
			response.setStatusCode(statusCode);
			response.setValue(valueData);
			return response;

		} catch (Exception ex) {
			LOGGER.error("decode exception: ", ex);
		} finally {
			byteBuffer = null;
		}

		return null;

	}

}
