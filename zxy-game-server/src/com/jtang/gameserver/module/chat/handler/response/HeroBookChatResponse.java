package com.jtang.gameserver.module.chat.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.module.icon.model.IconVO;

public class HeroBookChatResponse extends ChatResponse {

	
	/**
	 * 数量
	 */
	public int num;
	
	/**
	 * 仙人星级
	 */
	public int heroStar;
	
	/**
	 * 奖励
	 */
	public List<RewardObject> rewardObject;
	
	public HeroBookChatResponse(int msgType, String actorName, long actorId, int level, int vipLevel,int num,int heroStar,List<RewardObject> rewardObject,IconVO iconVO) {
		super(msgType, actorName, actorId, level, vipLevel,iconVO);
		this.num = num;
		this.heroStar = heroStar;
		this.rewardObject = rewardObject;
	}
	
	@Override
	public void write() {
		super.write();
		writeInt(num);
		writeInt(heroStar);
		writeShort((short) rewardObject.size());
		for(RewardObject reward:rewardObject){
			writeBytes(reward.getBytes());
		}
	}

}
