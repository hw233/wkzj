package com.jtang.gameserver.worldclient.codec;

import org.apache.mina.core.buffer.IoBuffer;

import com.jtang.core.mina.SocketConstant;
import com.jtang.core.mina.codec.client.InnerClientEncoder;
import com.jtang.core.protocol.ActorRequest;
import com.jtang.core.utility.BufferFactory;

/**
 * socket客户端字节方式的编码类.
 * 用于内网服务器间通讯时建立客户端编码用
 * @author 0x737263
 * 
 */
public class WorldClientEncoder extends InnerClientEncoder {

	//module(1) + cmd(1) + actorId(8) + value(n)
	private static int CLIENT_HEADER_LENGTH = 10;
	
	@Override
	protected IoBuffer convert2Iobuffer(Object message) {
		if (!(message instanceof ActorRequest)) {
			return null;
		}
		
		//module(1) + cmd(1) + actorId(8) + value(n)
		IoBuffer buffer;
		ActorRequest request = (ActorRequest) message;
		
		if (request.getValue() == null) {
			buffer = BufferFactory.getIoBuffer(CLIENT_HEADER_LENGTH + SocketConstant.HEADER_FLAG_LENGTH, true);
			//buffer = IoBuffer.allocate(CLIENT_HEADER_LENGTH + SocketConstant.HEADER_FLAG_LENGTH);
			//buffer.setAutoExpand(true);
			buffer.putInt(SocketConstant.HEADER_FLAG);
			buffer.putInt(CLIENT_HEADER_LENGTH);
			buffer.put(request.getModule());
			buffer.put(request.getCmd());
			buffer.putLong(request.getActorId());
		} else {
			byte[] bytes = (byte[]) request.getValue();
			buffer = BufferFactory.getIoBuffer(CLIENT_HEADER_LENGTH + bytes.length + SocketConstant.HEADER_FLAG_LENGTH, true);
			//buffer = IoBuffer.allocate(CLIENT_HEADER_LENGTH + bytes.length + SocketConstant.HEADER_FLAG_LENGTH);
			//buffer.setAutoExpand(true);
			buffer.putInt(SocketConstant.HEADER_FLAG);
			buffer.putInt(CLIENT_HEADER_LENGTH + bytes.length);
			buffer.put(request.getModule());
			buffer.put(request.getCmd());
			buffer.putLong(request.getActorId());
			buffer.put(bytes);
		}

		buffer.flip();
		buffer.free();
		
		return buffer;
		
	}
		
//	/**
//	 * 转换为IoBuffer对象
//	 * @return
//	 * @throws IOException 
//	 */
//	private IoBuffer convert2Iobuffer(Request request) throws IOException {
//
//		// packageLen(4) + module(1) + cmd(1)
//		IoBuffer buffer;
//		if (request.getValue() == null) {
//			buffer = IoBuffer.allocate(SocketConstant.CLIENT_HEADER_LENGTH + SocketConstant.HEADER_FLAG_LENGTH);
//			buffer.setAutoExpand(true);
//			buffer.putInt(SocketConstant.HEADER_FLAG);
//			buffer.putInt(SocketConstant.CLIENT_HEADER_LENGTH);
//			buffer.put(request.getModule());
//			buffer.put(request.getCmd());
//		} else {
//			byte[] bytes = (byte[]) request.getValue();
//			buffer = IoBuffer.allocate(SocketConstant.CLIENT_HEADER_LENGTH + bytes.length + SocketConstant.HEADER_FLAG_LENGTH);
//			buffer.setAutoExpand(true);
//			buffer.putInt(SocketConstant.HEADER_FLAG);
//			buffer.putInt(SocketConstant.CLIENT_HEADER_LENGTH + bytes.length);
//			buffer.put(request.getModule());
//			buffer.put(request.getCmd());
//			buffer.put(bytes);
//		}
//
//		buffer.flip();
//		buffer.free();
//		
//		return buffer;
//	}
	
}
