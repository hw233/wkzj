package com.jtang.core.mina.codec;

/**
 * 消息解码状态
 * 
 * @author 0x737263
 * 
 */
public enum DecoderState {
	/**
	 * 等待收数据中
	 */
	WAITING_DATA,

	/**
	 * 已经准备好
	 */
	READY;
}