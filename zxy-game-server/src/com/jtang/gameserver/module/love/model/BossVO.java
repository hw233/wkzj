package com.jtang.gameserver.module.love.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;


public class BossVO {

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 总计造成的伤害值
	 */
	public int lastHurtNum;
	
	/**
	 * 总计挑战次数
	 */
	public int fightNum;
	
	/**
	 * 最大血量
	 */
	public int maxHp;
	
	/**
	 * 怪物血量
	 * 怪物id,血量:...
	 */
	public Map<Integer,Integer> monsterHPMap;
	
	public Map<Integer, Integer> monsterLastHpMax;
	
	public BossVO(){
		
	}
	
	public BossVO(int id,int lastHurtNum,int fightNum,int maxHp,Map<Integer,Integer> monsterHPMap){
		this.id = id;
		this.lastHurtNum = lastHurtNum;
		this.fightNum = fightNum;
		this.maxHp = maxHp;
		this.monsterHPMap = monsterHPMap;
	}
	
	public String parser2String(){
		StringBuffer sb = new StringBuffer();
		sb.append(id).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(lastHurtNum).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(fightNum).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(maxHp).append(Splitable.ATTRIBUTE_SPLIT);
		String monsterHpStr = StringUtils.map2DelimiterString(monsterHPMap, Splitable.BETWEEN_ITEMS, Splitable.DELIMITER_ARGS);
		sb.append(monsterHpStr).append(Splitable.ATTRIBUTE_SPLIT);
		String monsterHpMaxStr = StringUtils.map2DelimiterString(monsterLastHpMax, Splitable.BETWEEN_ITEMS, Splitable.DELIMITER_ARGS);
		sb.append(monsterHpMaxStr);
		return sb.toString();
	}
	
	public static BossVO valueOf(String str[]){
		str = StringUtils.fillStringArray(str, 6, "0");
		BossVO bossStateVO = new BossVO();
		bossStateVO.id = Integer.valueOf(str[0]);
		bossStateVO.lastHurtNum = Integer.valueOf(str[1]);
		bossStateVO.fightNum = Integer.valueOf(str[2]);
		bossStateVO.maxHp = Integer.valueOf(str[3]);
		List<String[]> list = StringUtils.delimiterString2Array(str[4], Splitable.DELIMITER_ARGS, Splitable.BETWEEN_ITEMS);
		bossStateVO.monsterHPMap = new HashMap<>();
		for(String[] string : list){
			string = StringUtils.fillStringArray(string, 2, "0");
			bossStateVO.monsterHPMap.put(Integer.valueOf(string[0]), Integer.valueOf(string[1]));
		}
		bossStateVO.monsterLastHpMax = new HashMap<>();
		list = StringUtils.delimiterString2Array(str[5], Splitable.DELIMITER_ARGS, Splitable.BETWEEN_ITEMS);
		for(String[] string : list){
			string = StringUtils.fillStringArray(string, 2, "0");
			bossStateVO.monsterLastHpMax.put(Integer.valueOf(string[0]), Integer.valueOf(string[1]));
		}
		return bossStateVO;
	}
}
