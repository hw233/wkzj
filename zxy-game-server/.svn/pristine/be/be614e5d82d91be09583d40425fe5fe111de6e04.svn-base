package com.jtang.gameserver.module.battle.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

public class BattleVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2906205368473440288L;
	/**
	 * 战斗类型
	 */
	public BattleType battleType;
	/**
	 * 胜利 次数
	 */
	public int winNum;
	
	/**
	 * 最后该类型战斗时间
	 */
	public int lastBattleTime;
	
	/**
	 * 失败次数
	 */
	public int failNum;
	
	public BattleVO(String[] strs) {
		strs = StringUtils.fillStringArray(strs, 4, "0");
		battleType = BattleType.getByCode(Integer.valueOf(strs[0]));
		winNum = Integer.valueOf(Integer.valueOf(strs[1]));
		lastBattleTime = Integer.valueOf(Integer.valueOf(strs[2]));
		failNum = Integer.valueOf(Integer.valueOf(strs[3]));
	}
	public BattleVO() {
	}
	
	public String parse2String() {
		List<Object> list = new ArrayList<>();
		list.add(this.battleType.getCode());
		list.add(this.winNum);
		list.add(this.lastBattleTime);
		list.add(failNum);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}
}
