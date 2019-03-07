package com.jtang.worldserver.server.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.jtang.core.mina.codec.server.InnerServerDecoder;
import com.jtang.core.protocol.ActorRequest;
import com.jtang.core.utility.BufferFactory;

/**
 * 
 * @author 0x737263
 *
 */
public class WorldServerDecoder extends InnerServerDecoder {

	//module(1) + cmd(1) + actorId(8) + value(n)
	private static int SKIP_BYTES_LEN = 10;
	
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
			
			// module(1) + cmd(1) + actorid(8) + value(n)
			byte module = byteBuffer.get();
			byte cmd = byteBuffer.get();
			
			long actorId = byteBuffer.getLong();
			
			int len = bytes.length - SKIP_BYTES_LEN;
			byte[] valueData = new byte[len];
			byteBuffer.get(valueData);
			byteBuffer.clear();

			return ActorRequest.valueOf(module, cmd, actorId, valueData);

		} catch (Exception ex) {
			LOGGER.error("decode exception: ", ex);
		} finally {
			byteBuffer = null;
		}

		return null;
	}

}
