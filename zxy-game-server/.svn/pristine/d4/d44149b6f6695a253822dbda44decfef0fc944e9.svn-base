package com.jtang.gameserver.module.adventures.vipactivity.model;

import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.StringUtils;

/**
 * 合成失败奖励物品
 * @author ludd
 *
 */
public class FailReward extends IoBufferSerializer{
	/**
	 * 类型
	 */
	public RewardType type;
	/**
	 * id
	 */
	public int id;
	/**
	 * 数量
	 */
	public int num;
	
	public static FailReward valueOf(String[] record) {
		record = StringUtils.fillStringArray(record, 3, "0");
		FailReward failReward = new FailReward();
		failReward.type = RewardType.getType(Integer.valueOf(record[0]));
		failReward.id = Integer.valueOf(record[1]);
		failReward.num = Integer.valueOf(record[2]);
		return failReward;
	}

	@Override
	public void write() {
		this.writeInt(type.getCode());
		this.writeInt(id);
		this.writeInt(num);
	}
}
