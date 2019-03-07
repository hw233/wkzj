package com.jtang.gameserver.module.extapp.vipbox.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class VipBoxResponse extends IoBufferSerializer {

	/**
	 * 奖励是否已领取
	 * 0.未领取
	 * 1.已领取
	 */
	public int isGet;
	
	/**
	 * 今天可以领取多少个箱子
	 */
	public int boxNum;
	
	/**
	 * 当前开到第几个箱子
	 */
	public int num;
	
	public VipBoxResponse(int isGet,int boxNum,int num){
		this.isGet = isGet;
		this.boxNum = boxNum;
		this.num = num;
	}
	
	@Override
	public void write() {
		writeByte((byte)isGet);
		writeInt(boxNum);
		writeInt(num);
	}
}
