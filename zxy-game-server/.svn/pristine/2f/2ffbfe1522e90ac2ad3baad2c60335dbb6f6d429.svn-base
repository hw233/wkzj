package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "treasureLevelConfig")
public class TreasureLevelConfig implements ModelAdapter {

	/**
	 * 开始等级
	 */
	public int beginLevel;
	
	/**
	 * 结束等级
	 */
	public int endLevel;
	
	/**
	 * 怪物列表
	 */
	public String monster;
	
	/**
	 * 战场id
	 */
	public int battleId;
	
	/**
	 * 几率
	 */
	public int proportion;
	
	@FieldIgnore
	private Map<Integer, Integer> monsterMap = new HashMap<Integer, Integer>();// 怪物列表map
	
	@Override
	public void initialize() {
		
		List<String> list = StringUtils.delimiterString2List(monster, Splitable.ELEMENT_SPLIT);
		for (String item : list) {
			List<String> attrs = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);
			// 怪物id
			int heroId = Integer.valueOf(attrs.get(0));
			// 怪物在阵型中的位置
			int gridIndex = Integer.valueOf(attrs.get(1));
			monsterMap.put(gridIndex,heroId);
		}
		this.monster = null;
		
	}
	
	public Map<Integer,Integer> getMonster(){
		return monsterMap;
	}

}
