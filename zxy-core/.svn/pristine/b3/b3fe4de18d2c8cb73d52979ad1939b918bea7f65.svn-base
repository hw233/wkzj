package com.jiatang.common.crossbattle.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

public class ExchangePointW2G extends IoBufferSerializer {
	/**
	 * 奖励列表
	 */
	public List<RewardObject> list;
	
	/**
	 * 贡献点
	 * @param bytes
	 */
	public int point;

	public ExchangePointW2G(byte[] bytes) {
		super(bytes);
	}

	public ExchangePointW2G(List<RewardObject> list,int point) {
		super();
		this.list = list;
		this.point = point;
	}
	
	@Override
	protected void read() {
		this.list = new ArrayList<>();
		short len = readShort();
		for (int i = 0; i < len; i++) {
			RewardObject rewardObject = new RewardObject();
			rewardObject.readBuffer(this);
			this.list.add(rewardObject);
		}
		this.point = readInt();
	}
	
	@Override
	public void write() {
		this.writeShort((short) this.list.size());
		for (RewardObject rewardObject : list) {
			this.writeBytes(rewardObject.getBytes());
		}
		this.writeInt(point);
	}
	
	
}
