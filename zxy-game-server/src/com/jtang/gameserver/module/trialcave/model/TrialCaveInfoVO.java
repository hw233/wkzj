package com.jtang.gameserver.module.trialcave.model;

import com.jtang.core.protocol.IoBufferSerializer;

public class TrialCaveInfoVO extends IoBufferSerializer {

	/**
	 * 试炼洞关卡id
	 */
	public byte id;
	
	/**
	 * 已经试炼次数
	 */
	public int alreadyTrialTimes;
	
	/**
	 * 最后一次试炼时间
	 */
	public int lastTrialTime;
	
	
	@Override
	public void write() {
		this.writeByte(id);
		this.writeInt(alreadyTrialTimes);
		this.writeInt(lastTrialTime);
	}
	
	public static TrialCaveInfoVO valueOf(int id, int alreadyTrialTimes, int lastTrialTime){
		TrialCaveInfoVO vo = new TrialCaveInfoVO();
		vo.id = (byte)id;
		vo.alreadyTrialTimes = alreadyTrialTimes;
		vo.lastTrialTime = lastTrialTime;
		return vo;
	}
}
 