package com.jtang.gameserver.server.codec;

import java.io.IOException;

import org.apache.mina.core.buffer.IoBuffer;

import com.jtang.core.mina.SocketConstant;
import com.jtang.core.mina.codec.server.InnerServerEncoder;
import com.jtang.core.protocol.Response;
import com.jtang.core.utility.BufferFactory;

/**
 * 继承内网服务器的解码类。重写转义成IoBuffer对象方法.
 * 
 * @author 0x737263
 * 
 */
public class GatewayAdminServerEncoder extends InnerServerEncoder {

	private static int SERVER_HEADER_LENGTH = 4;
	
	/**
	 * 转换为IoBuffer对象
	 * @return
	 * @throws IOException 
	 */
	@Override
	protected IoBuffer convert2Iobuffer(Object message) {
		if(!(message instanceof Response)) {
			return null;
		}
		
		// packageLen(4) + module(1) + cmd(1) + statusCode(2)
		IoBuffer buffer;
		Response response = (Response) message;
		if (response.getValue() == null) {
//			buffer = IoBuffer.allocate(SERVER_HEADER_LENGTH + SocketConstant.HEADER_FLAG_LENGTH);
//			buffer.setAutoExpand(true);
			buffer = BufferFactory.getIoBuffer(SERVER_HEADER_LENGTH + SocketConstant.HEADER_FLAG_LENGTH, true);
			buffer.putInt(SocketConstant.HEADER_FLAG);
			buffer.putInt(SERVER_HEADER_LENGTH);
			buffer.put(response.getModule());
			buffer.put(response.getCmd());
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
			buffer.putShort(response.getStatusCode());
			buffer.put(bytes);
		}

		buffer.flip();
		buffer.free();
		
		return buffer;
	}
	
}
