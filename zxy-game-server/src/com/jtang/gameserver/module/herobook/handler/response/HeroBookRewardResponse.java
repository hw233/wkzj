package com.jtang.gameserver.module.herobook.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 奖励回复
 * @author ludd
 *
 */
public class HeroBookRewardResponse extends IoBufferSerializer {
	/**
	 * 当前奖励序号
	 */
	private int getOrderId;
	/**
	 * 奖励列表
	 */
	private List<RewardObject> list = new ArrayList<>();
	
	
	
	public HeroBookRewardResponse(int getOrderId, List<RewardObject> list) {
		super();
		this.getOrderId = getOrderId;
		this.list = list;
	}



	@Override
	public void write() {
		writeInt(getOrderId);
		this.writeShort((short) this.list.size());
		for (RewardObject obj : list) {
			this.writeBytes(obj.getBytes());
		}
	}

}
