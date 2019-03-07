package com.jtang.gameserver.module.adventures.achievement.handler.response;


import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
/**
 * 推送一个成就的数据包
 * @author pengzy
 *
 */
public class AchieveResponse extends IoBufferSerializer{
	
	private List<AchieveVO> achieveVOList;
	
	public AchieveResponse(List<AchieveVO> achievementVO){
		this.achieveVOList = achievementVO;
	}
	
	@Override
	public void write() {
		writeShort((short) achieveVOList.size());
		for(AchieveVO achieveVO : achieveVOList){
			achieveVO.writeIn(this);
		}
	}

}
