package com.jtang.core.mina.codec.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * socket服务端字节方式的编码类.
 * 用于内网服务器间通讯时建立服务端编码用
 * @author 0x737263
 *
 */
public abstract class InnerServerEncoder extends ProtocolEncoderAdapter {
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
		if (buf != null && !session.isClosing() && session.isConnected()) {
			encoderOutput.write(buf);
		} else {
			LOGGER.error("message buf is null.");
		}
	}
	
	protected abstract IoBuffer convert2Iobuffer(Object message);
		
//	/**
//	 * 转换为IoBuffer对象
//	 * @return
//	 * @throws IOException 
//	 */
//	protected IoBuffer convert2Iobuffer(Response response) throws IOException {
//
//		// packageLen(4) + module(1) + cmd(1) + statusCode(2)
//		IoBuffer buffer;
//		if (response.getValue() == null) {
//			buffer = IoBuffer.allocate(SocketConstant.SERVER_HEADER_LENGTH + SocketConstant.HEADER_FLAG_LENGTH);
//			buffer.setAutoExpand(true);
//			buffer.putInt(SocketConstant.HEADER_FLAG);
//			buffer.putInt(SocketConstant.SERVER_HEADER_LENGTH);
//			buffer.put(response.getModule());
//			buffer.put(response.getCmd());
//			buffer.putShort(response.getStatusCode());
//		} else {
//			byte[] bytes = (byte[]) response.getValue();
//			buffer = IoBuffer.allocate(SocketConstant.SERVER_HEADER_LENGTH + bytes.length + SocketConstant.HEADER_FLAG_LENGTH);
//			buffer.setAutoExpand(true);
//			buffer.putInt(SocketConstant.HEADER_FLAG);
//			buffer.putInt(SocketConstant.SERVER_HEADER_LENGTH + bytes.length);
//			buffer.put(response.getModule());
//			buffer.put(response.getCmd());
//			buffer.putShort(response.getStatusCode());
//			buffer.put(bytes);
//		}
//
//		buffer.flip();
//		buffer.free();
//		
//		return buffer;
//	}
	
}
