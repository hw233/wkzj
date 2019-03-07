package com.jtang.gameserver.admin.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 在线人数返回
 * @author ludd
 *
 */
public class OnlinePlayerNumResponse extends IoBufferSerializer {

	/**
	 * 历史最小在线
	 */
	private int historyOnlineMinNum;
	/**
	 * 历史最大在线
	 */
	private int historyOnlineMaxNum;
	/**
	 * 当前在线人数
	 */
	private int currentOnlineNum;
	
	
	public OnlinePlayerNumResponse(int historyOnlineMinNum, int historyOnlineMaxNum, int currentOnlineNum) {
		super();
		this.historyOnlineMinNum = historyOnlineMinNum;
		this.historyOnlineMaxNum = historyOnlineMaxNum;
		this.currentOnlineNum = currentOnlineNum;
	}


	@Override
	public void write() {
		this.writeInt(this.historyOnlineMinNum);
		this.writeInt(this.historyOnlineMaxNum);
		this.writeInt(this.currentOnlineNum);
	}

}
