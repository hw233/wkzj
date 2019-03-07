package com.jiatang.common.crossbattle.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 世界服请求报名
 * @author ludd
 *
 */
public class SignupW2G extends IoBufferSerializer {
	/**
	 * 报名最强势力榜名次
	 */
	public int powerRank;
	
	public SignupW2G(byte[] bytes) {
		super(bytes);
	}
	
	
	public SignupW2G(int powerRank) {
		super();
		this.powerRank = powerRank;
	}


	@Override
	public void read() {
		this.powerRank = readInt();
	}
	
	@Override
	public void write() {
		this.writeInt(this.powerRank);
	}
	
	

}
