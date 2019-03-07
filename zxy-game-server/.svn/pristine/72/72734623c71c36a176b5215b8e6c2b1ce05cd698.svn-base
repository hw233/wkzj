package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "ladderGlobalConfig")
public class LadderGlobalConfig implements ModelAdapter {

	/**
	 * 开启等级
	 */
	public int openLevel;
	
	/**
	 *基础积分 
	 */
	public int baseScore;
	
	/**
	 * 一个赛季天数
	 */
	public int sportDay;
	
	/**
	 * 战斗次数回复上限
	 */
	public int maxFightNum;
	
	/**
	 * 回复一次战斗次数需要的时间(秒)
	 */
	public int flushTime;
	
	/**
	 * 存储战斗信息的条数
	 */
	public int fightInfo;
	
	/**
	 * 补满战斗次数需要消耗的点券
	 */
	public String costTicket;
	
	/**
	 * 高排行敌人的等级区间
	 */
	private String targetLevel1;
	
	/**
	 * 相近排行敌人的等级区间
	 */
	private String targetLevel2;
	
	/**
	 * 刷新敌人的个数
	 */
	public int targetNum;
	
	/**
	 * 刷新敌人消耗的金币
	 */
	public String costGolds;
	
	/**
	 * 积分计算公式
	 */
	public String calcScore;
	
	/**
	 * 积分计算公式
	 */
	public int bottomNumber;
	
	/**
	 * 挑战成功积分计算公式(幂部分)
	 */
	public String winExponent;
	
	/**
	 * 挑战失败积分计算公式(幂部分)
	 */
	public String loseExponent;
	
	/**
	 * 地图id
	 */
	public int map;
	
	/**
	 * 赛季结束发奖时间
	 */
	public int endTime;
	
	/**
	 * 赛季发奖人数
	 */
	public int rewardRank;
	
	/**
	 * 排行榜显示人数
	 */
	public int rankView;
	
	/**
	 * 连胜发送世界公告
	 * 场次_公告类型(详见LadderChatType)|...
	 */
	public String sendChat;
	
	/**
	 * 高排行敌人开始等级
	 */
	@FieldIgnore
	private String strongStartLevel;
	
	/**
	 * 高排行敌人结束等级
	 */
	@FieldIgnore
	private String strongEndLevel;
	
	/**
	 * 相近排行敌人开始等级
	 */
	@FieldIgnore
	private String weakStartLevel;
	
	/**
	 * 相近排行敌人结束等级
	 */
	@FieldIgnore
	private String weakEndLevel;
	
	/**
	 * 发送公告类型map
	 */
	@FieldIgnore
	public Map<Integer,Integer> chatMap = new HashMap<>();
	
	@Override
	public void initialize() {
		List<String> list = StringUtils.delimiterString2List(targetLevel1, Splitable.ATTRIBUTE_SPLIT);
		strongStartLevel = list.get(0);
		strongEndLevel = list.get(1);
		targetLevel1 = null;
		List<String> list1 = StringUtils.delimiterString2List(targetLevel2, Splitable.ATTRIBUTE_SPLIT);
		weakStartLevel = list1.get(0);
		weakEndLevel = list1.get(1);
		targetLevel2 = null;
		
		chatMap = StringUtils.delimiterString2IntMap(sendChat);
		sendChat = null;
	}
	
	/**
	 * 获取下次刷新需要的点券
	 * @param flushNum 已经购买补满次数
	 * @return
	 */
	public int getCostTicket(int flushNum){
		return FormulaHelper.executeCeilInt(costTicket, flushNum);
	}
	
	/**
	 * 获取下次刷新对手需要的金币
	 * @param flushNum 已经刷新对手次数
	 * @return
	 */
	public int getCostGold(int flushNum){
		return FormulaHelper.executeCeilInt(costGolds, flushNum);
	}

	/**
	 * 刷新高排名对手最小排名
	 * @param level
	 * @return
	 */
	public int getStrongMinRank(int rank) {
		return Math.max(FormulaHelper.executeCeilInt(strongStartLevel,rank), 1);
		
	}
	
	/**
	 * 刷新高排名对手最大排名
	 * @param level
	 * @return
	 */
	public int getStrongMaxRank(int rank) {
		return Math.max(FormulaHelper.executeCeilInt(strongEndLevel,rank), 1);
	}
	
	/**
	 * 刷新相近排名对手最小排名
	 * @param level
	 * @return
	 */
	public int getWeakMinRank(int rank) {
		return Math.max(FormulaHelper.executeCeilInt(weakStartLevel,rank), 1);
	}
	
	/**
	 * 刷新相近排名对手最大排名
	 * @param level
	 * @return
	 */
	public int getWeakMaxRank(int rank, int maxRank) {
		return Math.min(FormulaHelper.executeCeilInt(weakEndLevel,rank), maxRank);
	}
	
}
