package com.jtang.gameserver.module.adventures.bable.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class BableStarResponse extends IoBufferSerializer {

	/**
	 * 通天币数量
	 */
	public int star;
	
	/**
	 * 已使用通天币数量
	 */
	public int useStar;
	
	public BableStarResponse(int star,int useStar){
		this.star = star;
		this.useStar = useStar;
	}
	
	@Override
	public void write() {
		writeInt(star);
		writeInt(useStar);
	}
}
