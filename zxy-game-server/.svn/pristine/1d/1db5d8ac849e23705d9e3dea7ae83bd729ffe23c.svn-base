package com.jtang.gameserver.module.extapp.basin.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

public class BasinVO extends IoBufferSerializer {

	/**
	 * 领取状态
	 * 0.没完成1.可领取2.已领取
	 */
	public int state;
	
	/**
	 * 充值金额
	 */
	public int recharge;
	
	/**
	 * 奖励列表
	 */
	public List<RewardObject> list = new ArrayList<>();
	
	/**
	 * 图标
	 */
	public int rewardIcon;
	
	public BasinVO(int state,int recharge,List<RewardObject> list,int rewardIcon){
		this.state = state;
		this.recharge = recharge;
		this.list = list;
		this.rewardIcon = rewardIcon;
	}
	
	@Override
	public void write() {
		writeInt(state);
		writeInt(recharge);
		writeShort((short)list.size());
		for (RewardObject reawrdObject : list) {
			writeBytes(reawrdObject.getBytes());
		}
		writeInt(rewardIcon);
	}
}
