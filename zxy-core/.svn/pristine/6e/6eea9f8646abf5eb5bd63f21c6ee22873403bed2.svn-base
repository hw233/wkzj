package com.jtang.core.mina.codec.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.jtang.core.mina.SocketConstant;
import com.jtang.core.mina.codec.CodecContext;
import com.jtang.core.mina.codec.DecoderState;
import com.jtang.core.utility.BufferFactory;

/**
 * socket服务端字节方式的解码类.
 * 用于内网服务器间通讯时建立服务端解码用
 * @author 0x737263
 *
 */
public abstract class InnerServerDecoder extends CumulativeProtocolDecoder {
	protected Log LOGGER = LogFactory.getLog(getClass());
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer input, ProtocolDecoderOutput out) throws Exception {
		input.order(BufferFactory.BYTE_ORDER);
		CodecContext ctx = CodecContext.getCodecContext(session);
		
		if ((ctx != null) && (ctx.isSameState(DecoderState.WAITING_DATA))) {
			if (input.remaining() < ctx.getBytesNeeded()) {
				return false;
			}
			
			byte[] buffer = new byte[ctx.getBytesNeeded()];
			input.get(buffer);

			Object request = parseRequest(session, buffer);
			if (request != null) {
				out.write(request);
			}
			ctx.setState(DecoderState.READY);
			CodecContext.removeCodecContext(session);
			return true;
		}

		while (true) {
			// 包长度 = 4
			if (input.remaining() < 8) {
				return false;
			}
			
			input.mark();
			int i= input.getInt() ;

			if (i == SocketConstant.HEADER_FLAG) {
				
				break;
			}
			input.reset();
			input.get();
		}

		// 获取value 的消息码包长度
		int len = input.getInt();
		if (len < 1) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("message body length: %d.too large.", len));
			}
			return true;
		}
		
		//len = len - 4; //减去当前包的4个字节(长度)
		if (input.remaining() < len) {
			// 根据packageLen包长度。决定后续还有多少字节没有接收到.
			ctx = CodecContext.valueOf(len, DecoderState.WAITING_DATA);
			CodecContext.setCodecContext(session, ctx);
			return false;
		}

		// 单个包接收完毕，进行解析
		byte[] buffer = new byte[len];
		input.get(buffer);
		Object request = parseRequest(session, buffer);
		if (request != null) {
			out.write(request);
		}
		
		return true;
	}
	
	protected abstract Object parseRequest(IoSession session, byte[] bytes);

	/**
	 * 解析byte[]为Request对象.
	 * 
	 * @param session
	 * @param bytes 已脱包头的buffer
	 *            
	 * @return
	 */
//	protected abstract Request parseRequest(IoSession session, byte[] bytes) {
//		if (bytes == null) {
//			LOGGER.error("buffer is null.");
//			return null;
//		}
//
//		if (bytes.length < 2) {
//			LOGGER.warn(String.format("bufferSize: [%d] too short.", bytes.length));
//			return null;
//		}
//
//		ByteBuffer byteBuffer = null;
//		try {		
//			byteBuffer = ByteBuffer.wrap(bytes);
//
//			// module(1) + cmd(1) + value(n)
//			byte module = byteBuffer.get();
//			byte cmd = byteBuffer.get();
//			
//			int len = bytes.length - 2;
//			byte[] valueData = new byte[len];
//			byteBuffer.get(valueData); 
//			byteBuffer.clear();
//			
//			return Request.valueOf(module, cmd, valueData);
//			
//		} catch (Exception ex) {
//			LOGGER.error("decode exception: ", ex);
//		} finally {
//			byteBuffer = null;
//		}
//
//		return null;
//	}
}
