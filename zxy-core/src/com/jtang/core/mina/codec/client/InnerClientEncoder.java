package com.jtang.core.mina.codec.client;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * socket客户端字节方式的编码类.
 * 用于内网服务器间通讯时建立客户端编码用
 * @author 0x737263
 * 
 */
public abstract class InnerClientEncoder extends ProtocolEncoderAdapter {
	protected Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput encoderOutput) throws Exception {
		if (message == null) {
			LOGGER.error("message is null.");
			return;
		}
		
		if (session == null) {
			LOGGER.error("session is null.");
			return;
		}

		IoBuffer buf = convert2Iobuffer(message);
		if (buf != null && session.isClosing() == false && session.isConnected()) {
			encoderOutput.write(buf);
		} else {
			LOGGER.error("message buf is null.");
		}
	}
	
	protected abstract IoBuffer convert2Iobuffer(Object request);
		
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
