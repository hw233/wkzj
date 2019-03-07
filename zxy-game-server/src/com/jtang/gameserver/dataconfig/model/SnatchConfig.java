package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.battle.constant.WinLevel;

/**
 * 抢夺配置
 * @author liujian
 *
 */
@DataFile(fileName = "snatchConfig")
public class SnatchConfig implements ModelAdapter {
	
	/**
	 * 抢夺战斗的地图id
	 */
	private int mapId;
	
	/**
	 * 抢夺消耗精力的公式
	 */
	private String consumeEnergyExpr;
	
	/**
	 * 抢夺玩家成功获得物品 百分比(1-100)
	 */
	private int actorPercent;
	
	/**
	 * 抢夺机器人成功获得物品百分比(1-100)
	 */
	private int robotPercent;
	
	/**
	 * 每天开放时间(小时)  0-23
	 */
	private int openTime;
	
	/**
	 * 每天结束时间(小时)   0-23
	 */
	private int endTime;
	
	/**
	 * 抢夺成功获得对方金币百分比       小胜4_金币百分比|中胜5_金币百分比|大胜6_金币百分比
	 */
	private String snatchGoldPercent;
	
	/**
	 * 抢夺成功获得固定积分
	 * <pre>                              
	 * 大败1_积分计算表达式值|失败2_积分计算表达式值|惜败3_积分计算表达式值|小胜4_积分计算表达式值|中胜5_积分计算表达式值|大胜6_积分计算表达式值
	 * 参数: x1.自己等级  x2.对方等级
	 * </pre>
	 */
	private String awardScoreNumExpr;
	
	/**
	 * 抢夺次数上限
	 */
	public int snatchMaxNum;
	
	/**
	 * 购买次数消耗的点券
	 */
	public String costTicket;
	
	/**
	 * 回复一次战斗次数需要时间(秒)
	 */
	public int flushTime;
	
	/**
	 * 每天能获得奖励的抢夺次数
	 */
	public int rewardNum;

	/**
	 * 抢夺金币数百分比
	 * 格式为Map<WinLevel, 金币百分比>
	 */
	@FieldIgnore
	private Map<WinLevel,Integer> snatchGoldPercentMaps = new HashMap<>();
	
	/**
	 * 奖励积分数量
	 * 格式为Map<WinLevel, 积分值表达式>
	 */
	@FieldIgnore
	private Map<WinLevel, String> awardScoreNumMaps = new HashMap<>();
	
	@Override
	public void initialize() {
		
		Map<Integer, Integer> goldPercentMaps = StringUtils.delimiterString2IntMap(snatchGoldPercent);
		for (Entry<Integer, Integer> entry : goldPercentMaps.entrySet()) {
			snatchGoldPercentMaps.put(WinLevel.getByCode(entry.getKey()), entry.getValue());		
		}
		
		Map<Integer, String> awardScoreMaps = StringUtils.delimiterString2StringMap(awardScoreNumExpr);
		for (Entry<Integer, String> entry : awardScoreMaps.entrySet()) {
			awardScoreNumMaps.put(WinLevel.getByCode(entry.getKey()), entry.getValue());
		}
		
		this.snatchGoldPercent = null;
		this.awardScoreNumExpr = null;
	}

	public int getMapId() {
		return mapId;
	}

	/**
	 * 获取消耗的精力值
	 * @param actorLevel	目标等级
	 * @return
	 */
	public int getConsumeEnergy(int actorLevel) {
		return FormulaHelper.executeCeilInt(consumeEnergyExpr, actorLevel);
	}

	public int getActorPercent() {
		return actorPercent;
	}

	public int getRobotPercent() {
		return robotPercent;
	}
	
	public int getOpenTime() {
		return openTime;
	}

	public int getEndTime() {
		return endTime;
	}
	
	
	/**
	 * 获取抢夺敌人金币百分比
	 * @param level
	 * @return
	 */
	public int getSnatchGoldPercent(WinLevel level) {
		if (snatchGoldPercentMaps.containsKey(level)) {
			return snatchGoldPercentMaps.get(level);
		}
		return 0;
	}
	
	/**
	 * 获取奖励积分数
	 * @param level
	 * @param actorLevel
	 * @param targetActorLevel
	 * @return
	 */
	public int getAwardScoreNum(WinLevel level, int actorLevel, int targetActorLevel) {
		if (awardScoreNumMaps.containsKey(level)) {
			String expr = awardScoreNumMaps.get(level);
			return FormulaHelper.executeCeilInt(expr, actorLevel, targetActorLevel);
		}
		return 0;
	}
}
