package com.jtang.gameserver.module.notify.model.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

/**
 * 与盟友合作通关的信息结构
 * @author pengzy
 *
 */
public class StoryNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = -1440613755463492647L;

	/**
	 * 试炼洞战场Id
	 */
	public int battleId;
	
	/**
	 * 盟友获得声望
	 */
	public int allyReputation;
	
	/**
	 * 奖励
	 */
	public Map<Integer, Integer> rewards = new HashMap<>();
	
	/**
	 * 是否领取，0未领取，1为已领取
	 */
	public byte isGet;
	
	/**
	 * 是否有奖励
	 * 0.没有 1.有
	 */
	public byte isReward;
	
	public StoryNotifyVO() {
		
	}
	
	public StoryNotifyVO(int battleId, int allyReputation, Map<Integer, Integer> rewards, byte isGet,boolean isReward) {
		this.battleId = battleId;
		this.allyReputation = allyReputation;
		this.rewards = rewards;
		this.isGet = isGet;
		this.isReward = (byte) (isReward == true ? 1:0);
	}

	@Override
	protected void subClazzWrite() {
		writeInt(battleId);
		writeInt(allyReputation);
		writeShort((short) rewards.size());
		for (Entry<Integer, Integer> entry : rewards.entrySet()) {
			writeInt(entry.getKey());
			writeInt(entry.getValue());
		}
		writeByte(isGet);
		writeByte(isReward);
	}

	@Override
	protected void subClazzString2VO(String[] items) {
		battleId = Integer.valueOf(items[0]);
		allyReputation = Integer.valueOf(items[1]);
		rewards.putAll(StringUtils.keyValueString2IntMap(items[2]));
		isGet = Byte.valueOf(items[3]);
		if(items.length < 5){
			isReward = 1;
		}else{
			isReward = Byte.valueOf(items[4]);
		}
	}

	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(this.battleId));
		attributes.add(String.valueOf(this.allyReputation));
		String goodsString = StringUtils.map2DelimiterString(this.rewards, Splitable.DELIMITER_ARGS, Splitable.BETWEEN_ITEMS);
		attributes.add(goodsString);
		attributes.add(String.valueOf(isGet));
		attributes.add(String.valueOf(isReward));
	}
}
