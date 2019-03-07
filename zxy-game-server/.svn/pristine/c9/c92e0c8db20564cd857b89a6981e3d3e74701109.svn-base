package com.jtang.gameserver.module.adventures.achievement.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
/**
 * 获取已完成、已领取奖励、正在做的成就的数据包
 * @author pengzy
 *
 */
public class AchieveListResponse extends IoBufferSerializer{

	private List<AchieveVO> achievementList;
	
	public AchieveListResponse(List<AchieveVO> achievementList){
		this.achievementList = achievementList;
	}
	
	@Override
	public void write() {
		writeShort((short)achievementList.size());
		for(AchieveVO achivementVO : achievementList){
			achivementVO.writeIn(this);
		}
	}

}
