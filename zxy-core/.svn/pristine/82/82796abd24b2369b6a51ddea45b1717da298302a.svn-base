package com.jiatang.common.crossbattle.response;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 世界服下发对阵双方数据
 * @author ludd
 *
 */
public class NoticeGameW2G extends IoBufferSerializer {
	
	/**
	 * 开始为1，结束为0
	 */
	public byte start;
	
	public NoticeGameW2G(byte[] bytes) {
		setReadBuffer(bytes);
	}
	
	
	
	public NoticeGameW2G(byte start) {
		super();
		this.start = start;
	}



	@Override
	public void read() {
		this.start = readByte();
	}
	
	@Override
	public void write() {
		this.writeByte(this.start);
	}

}
