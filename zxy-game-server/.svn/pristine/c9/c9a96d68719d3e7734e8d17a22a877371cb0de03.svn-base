package com.jtang.gameserver.module.extapp.ernie.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 摇奖信息
 * @author lig
 *
 */
public class ErnieInfoResponse extends IoBufferSerializer {
	
	/**
	 * 免费抽奖次数
	 */
	public int freeTimes;
	
	/*
	 * 使用次数
	 */
	public int useTimes;
	
	/**
	 * 当前需要消耗的元宝
	 */
	public int costTicket;
	/**
	 * 奖励列表
	 */
	private List<RewardObject> list;

	public ErnieInfoResponse(int freeTimes, int useTimes, int costTicket, List<RewardObject> list) {
		this.freeTimes = freeTimes;
		this.useTimes = useTimes;
		this.costTicket = costTicket;
		this.list = list;
	}
	
	@Override
	public void write() {
		this.writeInt(this.freeTimes);
		this.writeInt(this.useTimes);
		this.writeInt(this.costTicket);
		this.writeShort((short) this.list.size());
		for (RewardObject vo : list) {
			this.writeBytes(vo.getBytes());
		}
	}
}
