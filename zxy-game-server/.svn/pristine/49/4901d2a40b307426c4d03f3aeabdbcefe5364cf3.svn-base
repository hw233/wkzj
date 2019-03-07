package com.jtang.gameserver.module.extapp.basin.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.extapp.basin.model.BasinVO;

public class BasinResponse extends IoBufferSerializer {
	
	/**
	 * 活动期间累计充值的元宝数
	 */
	public int recharge;
	
	/**
	 * 活动结束时间
	 */
	public int time;

	/**
	 * 活动记录
	 * key:充值元宝数
	 */
	public List<BasinVO> reward = new ArrayList<>();
	
	
	public BasinResponse(int recharge,int time,List<BasinVO> reward){
		this.recharge = recharge;
		this.time = time;
		this.reward = reward;
	}
	
	@Override
	public void write() {
		writeInt(recharge);
		writeInt(time);
		writeShort((short)reward.size());
		for(BasinVO basinVO:reward){
			writeBytes(basinVO.getBytes());
		}
	}
}
