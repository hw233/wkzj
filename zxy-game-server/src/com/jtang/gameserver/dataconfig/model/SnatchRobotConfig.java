package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;

/**
 * 抢夺机器人配置
 * @author liujian
 *
 */
@DataFile(fileName="snatchRobotConfig")
public class SnatchRobotConfig implements ModelAdapter {

	/**
	 * 掌教等级
	 */
	public int actorLevel;
	/**
	 * 当前等级机器人生成个数
	 */
	public int robotNumber;
	/**
	 * 对应仙人等级下限
	 */
	public int heroMinLevel;
	/**
	 * 对应仙人等级上限
	 */
	public int heroMaxLevel;
	/**
	 * 机器人阵型(根据配置从heroConfig抽取仙人基础数值) 格式:位置_仙人Id|位置_仙人Id
	 */
	public String heroLineUp1;
	/**
	 * 机器人阵型(根据配置从heroConfig抽取仙人基础数值) 格式:位置_仙人Id|位置_仙人Id
	 */
	public String heroLineUp2;
	/**
	 * 机器人阵型(根据配置从heroConfig抽取仙人基础数值) 格式:位置_仙人Id|位置_仙人Id
	 */
	public String heroLineUp3;
	/**
	 * 机器人阵型(根据配置从heroConfig抽取仙人基础数值) 格式:位置_仙人Id|位置_仙人Id
	 */
	public String heroLineUp4;
	/**
	 * 机器人阵型(根据配置从heroConfig抽取仙人基础数值) 格式:位置_仙人Id|位置_仙人Id
	 */
	public String heroLineUp5;
	
	/**
	 * 机器人金币下限
	 */
	public int robotMinGold;
	
	/**
	 * :机器人金币上限
	 */
	public int robotMaxGold;
	
	/**
	 * 气势下限
	 */
	public int robotMinMorale;
	
	/**
	 * 气势上限
	 */
	public int robotMaxMorale;
	
	
	@FieldIgnore
	private Map<Integer,Integer> lineUpList1 = new HashMap<Integer,Integer>();
	@FieldIgnore
	private Map<Integer,Integer> lineUpList2 = new HashMap<Integer,Integer>();
	@FieldIgnore
	private Map<Integer,Integer> lineUpList3 = new HashMap<Integer,Integer>();
	@FieldIgnore
	private Map<Integer,Integer> lineUpList4 = new HashMap<Integer,Integer>();
	@FieldIgnore
	private Map<Integer,Integer> lineUpList5 = new HashMap<Integer,Integer>();
	
	@FieldIgnore
	private List<Map<Integer,Integer>> totalLineUpList = new ArrayList<>();
	
	@Override
	public void initialize() {
		lineUpList1 = StringUtils.delimiterString2IntMap(heroLineUp1);
		lineUpList2 = StringUtils.delimiterString2IntMap(heroLineUp2);
		lineUpList3 = StringUtils.delimiterString2IntMap(heroLineUp3);
		lineUpList4 = StringUtils.delimiterString2IntMap(heroLineUp4);
		lineUpList5 = StringUtils.delimiterString2IntMap(heroLineUp5);
		totalLineUpList.add(lineUpList1);
		totalLineUpList.add(lineUpList2);
		totalLineUpList.add(lineUpList3);
		totalLineUpList.add(lineUpList4);
		totalLineUpList.add(lineUpList5);
		
		this.heroLineUp1 = null;
		this.heroLineUp2 = null;
		this.heroLineUp3 = null;
		this.heroLineUp4 = null;
		this.heroLineUp5 = null;
	}

	public Map<Integer,Integer> getRandomLineUp(){
		int random = RandomUtils.nextIntIndex(totalLineUpList.size());
		return totalLineUpList.get(random);
	}

	public int getFistHeroId(Map<Integer,Integer> lineUpList){
		int result = 0;
		for (Map.Entry<Integer, Integer> entry : lineUpList.entrySet()) {
			if(result == 0 || entry.getKey() < result){
				result = entry.getKey();
			}
		}
		return lineUpList.get(result);
	}
	
	/**
	 * 随机机器人金币
	 * @return
	 */
	public int getRobotGold(){
		return RandomUtils.nextInt(robotMinGold, robotMaxGold);
	}
	
	/**
	 * 随机机器人等级
	 * @return
	 */
	public int randomLevel() {
		return RandomUtils.nextInt(this.heroMinLevel, this.heroMaxLevel);
	}

}
