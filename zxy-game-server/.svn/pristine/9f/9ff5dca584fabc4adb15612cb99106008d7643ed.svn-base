package com.jtang.gameserver.module.trialcave.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.TrialCave;
import com.jtang.gameserver.module.trialcave.model.TrialCaveInfoVO;

/**
 * 获取试炼洞信息
 * @author lig
 *
 */
public class TrialCaveInfoResponse extends IoBufferSerializer {	
		
	/**
	 * 今天已经重置试炼的次数
	 */
	private byte trialedResetCount;
	
	/**
	 * 试炼次数
	 * 格式 ：<试炼洞 关卡id， 已经试炼次数>
	 */
	private List<TrialCaveInfoVO> trailCaveList;
	
	@Override
	public void write() {
		this.writeByte(trialedResetCount);
		this.writeShort((short)this.trailCaveList.size());
		for (TrialCaveInfoVO entity : trailCaveList) {
			this.writeBytes(entity.getBytes());
		}
	}
	
	public TrialCaveInfoResponse(TrialCave cave) {
		trailCaveList = new ArrayList<TrialCaveInfoVO>();
		this.trialedResetCount = (byte)cave.trialResetCount;
		TrialCaveInfoVO vo1 = TrialCaveInfoVO.valueOf(1, cave.ent1trialed, cave.ent1LastAckTime);
		trailCaveList.add(vo1);
		TrialCaveInfoVO vo2 = TrialCaveInfoVO.valueOf(2, cave.ent2trialed, cave.ent2LastAckTime);
		trailCaveList.add(vo2);
	}
}
