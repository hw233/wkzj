package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 试炼洞怪堆配置
 * @author lig
 *
 */
@DataFile(fileName = "trialCaveMonsterConfig")
public class TrialCaveMonsterConfig implements ModelAdapter {
	
	/**
	 * 配置id
	 */
	public int id;
	
    /**
	 * 怪物阵容
	 */
	public String monster;
	
	/**
	 * 怪物士气
	 */
	public int morale;
	
	/**
	 *  怪物列表map
	 */
	@FieldIgnore
	private Map<Integer, Integer> monsterMap = new HashMap<Integer, Integer>();
	
	@Override
	public void initialize() {
		List<String> list = StringUtils.delimiterString2List(monster, Splitable.ELEMENT_SPLIT);
		for (String item : list) {
			List<String> attrs = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);
			// 怪物id
			int monsterId = Integer.valueOf(attrs.get(0));
			// 怪物在阵型中的位置
			int gridIndex = Integer.valueOf(attrs.get(1));
			monsterMap.put(gridIndex, monsterId);
		}
		this.monster = null;
	}
	
	/**
	 * Map<怪物在阵型中的位置,怪物id> 
	 * @return
	 */
	public Map<Integer,Integer> getMonster(){
		return monsterMap;
	}
	
}
