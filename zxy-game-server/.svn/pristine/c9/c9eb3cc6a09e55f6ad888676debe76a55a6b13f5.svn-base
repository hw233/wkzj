package com.jtang.gameserver.module.adventures.bable.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 自动登塔回复
 * @author ludd
 *
 */
public class BableAutoResponse extends IoBufferSerializer {
	/**
	 * 到达目标层数
	 */
	private short targetFloor;
	
	/**
	 * 消耗点券数量个数
	 */
	private int costTicket;
	
	/**
	 * 倒计时时间
	 */
	private int autoTime;

	public BableAutoResponse(short targetFloor, int costTicket, int autoTime) {
		super();
		this.targetFloor = targetFloor;
		this.costTicket = costTicket;
		this.autoTime = autoTime;
	}
	
	@Override
	public void write() {
		this.writeShort(this.targetFloor);
		this.writeInt(this.costTicket);
		this.writeInt(this.autoTime);
	}
	
	
}
