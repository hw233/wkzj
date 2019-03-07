package com.jtang.core.mina.codec.client;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.mina.SocketConstant;
import com.jtang.core.mina.codec.CodecContext;
import com.jtang.core.mina.codec.DecoderState;
import com.jtang.core.utility.BufferFactory;

/**
 * socket客户端字节方式的解码类.
 * 用于内网服务器间通讯时建立客户端解码用
 * @author 0x737263
 * 
 */
public abstract class InnerClientDecoder extends CumulativeProtocolDecoder {
	protected Logger LOGGER = LoggerFactory.getLogger(getClass());
	
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

			Object response = parseResponse(session, buffer);
			if (response != null) {
				out.write(response);
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
			if (input.getInt() == SocketConstant.HEADER_FLAG) {
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
			CodecContext.setCodecContext(session,ctx);
			return false;
		}

		// 单个包接收完毕，进行解析
		byte[] buffer = new byte[len];
		input.get(buffer);
		
		Object response = parseResponse(session, buffer);
		if (response != null) {
			out.write(response);
		}
		
		return true;
	}
	
	protected abstract Object parseResponse(IoSession session, byte[] bytes);

//	/**
//	 * 解析byte[]为Response对象.
//	 * 
//	 * @param session
//	 * @param bytes 已脱包头的buffer
//	 *            
//	 * @return
//	 */
//	protected Response parseResponse(IoSession session, byte[] bytes) {
//		if (bytes == null) {
//			LOGGER.error("buffer is null.");
//			return null;
//		}
//
//		if (bytes.length < 4) {
//			LOGGER.error(String.format("bufferSize: [%d] too short.", bytes.length));
//			return null;
//		}
//
//		ByteBuffer byteBuffer = null;
//		try {		
//			byteBuffer = ByteBuffer.wrap(bytes);
//
//			// module(1) + cmd(1) + hashCode(4) + value(n)
//			byte module = byteBuffer.get();
//			byte cmd = byteBuffer.get();
//			short statusCode = byteBuffer.getShort();
////			int hashCode = byteBuffer.getInt();
//			
//			int len = bytes.length - SocketConstant.SERVER_HEADER_LENGTH;
//			byte[] valueData = new byte[len];
//			byteBuffer.get(valueData); 
//			byteBuffer.clear();
//			
////			if (stateCode != 0) {
////				//直接发送到
////				LOGGER.error(String.format("status:[%s]", stateCode));
////				return null;
////			}
//			
//			Response response = Response.valueOf(module, cmd, valueData);
//			response.setStatusCode(statusCode);
//			return response;
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
