package com.jtang.gameserver.module.story.handler.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

public class StoryFightResponse extends IoBufferSerializer {
	/**
	 * 物品掉落
	 */
	public Map<Long, Integer> awardGoods = new HashMap<>();
	
	/**
	 * 仙人获得的经验
	 */
	public Map<Integer, Integer> awardHeroExp;
	
	/**
	 * 奖励的装备
	 */
	public List<Long> equips = new ArrayList<>();
	
	/**
	 * 奖励的角色属性值
	 */
	public Map<Byte, Number> awardAttribute = new HashMap<>();
	
	/**
	 * 奖励的英雄魂魄数,格式是Map<魂魄ID,数量>
	 */
	public Map<Integer, Integer> awardHeroSouls = new HashMap<>();
	
	/**
	 * 扫荡的额外奖励
	 */
	public List<RewardObject> storyFightReward = new ArrayList<>();
	
	@Override
	public void write() {
		writeLongIntMap(awardGoods);
		writeIntMap(awardHeroExp);
		writeLongList(equips);
		writeIntMap(awardHeroSouls);
		writeShort((short)awardAttribute.size());
		for (Map.Entry<Byte, Number> entry : awardAttribute.entrySet()) {
			this.writeByte(entry.getKey());
			this.writeObject(entry.getValue());
		}
		writeShort((short)storyFightReward.size());
		for(RewardObject rewardObject:storyFightReward){
			writeBytes(rewardObject.getBytes());
		}
	}
}
