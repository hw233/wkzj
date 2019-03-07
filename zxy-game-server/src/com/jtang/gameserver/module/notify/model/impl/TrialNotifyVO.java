package com.jtang.gameserver.module.notify.model.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

/**
 * 与盟友试炼通知盟友的信息结构
 * @author pengzy
 *
 */
public class TrialNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = 1519752020389158723L;

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
	
	public TrialNotifyVO() {
		
	}
	
	public TrialNotifyVO(int battleId, int allyReputation, Map<Integer, Integer> rewards, byte isGet) {
		this.battleId = battleId;
		this.allyReputation = allyReputation;
		this.rewards = rewards;
		this.isGet = isGet;
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
	}

	@Override
	protected void subClazzString2VO(String[] items) {
		battleId = Integer.valueOf(items[0]);
		allyReputation = Integer.valueOf(items[1]);
		rewards.putAll(StringUtils.keyValueString2IntMap(items[2]));
		isGet = Byte.valueOf(items[3]);
	}

	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(battleId));
		attributes.add(String.valueOf(allyReputation));
		String rewardString = StringUtils.map2DelimiterString(this.rewards, Splitable.DELIMITER_ARGS, Splitable.BETWEEN_ITEMS);
		attributes.add(rewardString);
		attributes.add(String.valueOf(isGet));		
	}	
}
