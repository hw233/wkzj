package com.jtang.gameserver.module.extapp.onlinegifts.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class OnlineGiftsInfoResponse extends IoBufferSerializer {

	/**
	 * 当前要领取的礼包id
	 */
	public int curIndex;
	
	/**
	 *剩余时间
	 */
	public int leftTime;
	
	public OnlineGiftsInfoResponse(int index, int time){
		this.curIndex = index;
		this.leftTime = time;
	}
	
	@Override
	public void write() {
		writeInt(this.curIndex);
		writeInt(this.leftTime);
	}
	
}
