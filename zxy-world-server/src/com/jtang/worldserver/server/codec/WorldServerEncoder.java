package com.jtang.worldserver.server.codec;

import org.apache.mina.core.buffer.IoBuffer;

import com.jtang.core.mina.SocketConstant;
import com.jtang.core.mina.codec.server.InnerServerEncoder;
import com.jtang.core.protocol.ActorResponse;
import com.jtang.core.utility.BufferFactory;

/**
 * socket服务端字节方式的编码类.
 * 用于内网服务器间通讯时建立服务端编码用
 * @author 0x737263
 *
 */
public class WorldServerEncoder extends InnerServerEncoder {

	//module(1) + cmd(1) + actorId(8) + statusCode(2) + value(n)
	private static int SERVER_HEADER_LENGTH = 12;
	
	@Override
	protected IoBuffer convert2Iobuffer(Object message) {
		if (!(message instanceof ActorResponse)) {
			return null;
		}
		
		IoBuffer buffer;
		ActorResponse response = (ActorResponse) message;
		
		if (response.getValue() == null) {
//			buffer = IoBuffer.allocate(SERVER_HEADER_LENGTH + SocketConstant.HEADER_FLAG_LENGTH);
//			buffer.setAutoExpand(true);
			buffer = BufferFactory.getIoBuffer(SERVER_HEADER_LENGTH + SocketConstant.HEADER_FLAG_LENGTH, true);
			buffer.putInt(SocketConstant.HEADER_FLAG);
			buffer.putInt(SERVER_HEADER_LENGTH);
			buffer.put(response.getModule());
			buffer.put(response.getCmd());
			buffer.putLong(response.getActorId());
			buffer.putShort(response.getStatusCode());
		} else {
			byte[] bytes = (byte[]) response.getValue();
//			buffer = IoBuffer.allocate(SERVER_HEADER_LENGTH + bytes.length + SocketConstant.HEADER_FLAG_LENGTH);
//			buffer.setAutoExpand(true);
			buffer = BufferFactory.getIoBuffer(SERVER_HEADER_LENGTH + bytes.length + SocketConstant.HEADER_FLAG_LENGTH, true);
			buffer.putInt(SocketConstant.HEADER_FLAG);
			buffer.putInt(SERVER_HEADER_LENGTH + bytes.length);
			buffer.put(response.getModule());
			buffer.put(response.getCmd());
			
			buffer.putLong(response.getActorId());
			buffer.putShort(response.getStatusCode());
			buffer.put(bytes);
		}

		buffer.flip();
		buffer.free();
		
		return buffer;
	}
	
}
