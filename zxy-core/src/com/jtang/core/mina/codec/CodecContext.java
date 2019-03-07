package com.jtang.core.mina.codec;

import java.io.Serializable;

import org.apache.mina.core.session.IoSession;

/**
 * 解码上下文类
 * @author 0x737263
 *
 */
public class CodecContext implements Serializable {
	private static final long serialVersionUID = 6386883142830725337L;
	
	/**
	 * 解析包状态上下文
	 */
	public static final String CODEC_CONTEXT = "codec_context";
	
	/**
	 * 还需要的字节长度
	 */
	private int bytesNeeded = 0;

	private DecoderState state = DecoderState.WAITING_DATA;

	public int getBytesNeeded() {
		return this.bytesNeeded;
	}

	public void setBytesNeeded(int bytesNeeded) {
		this.bytesNeeded = bytesNeeded;
	}

	public DecoderState getState() {
		return this.state;
	}

	public void setState(DecoderState state) {
		this.state = state;
	}

	public boolean isSameState(DecoderState state) {
		return (this.state != null) && (state != null) && (this.state == state);
	}

	public static CodecContext valueOf(int byteNeeded, DecoderState state) {
		CodecContext codecContext = new CodecContext();
		codecContext.bytesNeeded = byteNeeded;
		codecContext.state = state;
		return codecContext;
	}
	
	
	/**
	 * 获取CodecConext状态
	 * @param session 当前用户session对象
	 * @return
	 */
	public static CodecContext getCodecContext(IoSession session) {
		return (CodecContext) session.getAttribute(CODEC_CONTEXT);
	}
	
	/**
	 * 设置CodecConext
	 * @param session
	 * @param context
	 */
	public static void setCodecContext(IoSession session,CodecContext context) {
		session.setAttribute(CODEC_CONTEXT, context);
	}
	
	/**
	 * 移除CodecContext属性
	 * @param session
	 */
	public static void removeCodecContext(IoSession session) {
		session.removeAttribute(CODEC_CONTEXT);
	}
}