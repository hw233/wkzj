package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.battle.constant.WinLevel;

/**
 * 通天塔配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "bableConfig")
public class BableConfig implements ModelAdapter {

	/**
	 * 登天塔id
	 */
	public int bableId;
	
	/**
	 * 等级限制
	 */
	public int greaterLevel;
	
	/**
	 * 是否开放奖励表达式(x1表示当前层数)
	 */
	public String openAwardExpr;
	
	/**
	 * 掉落物品集合(物品id_物品数表达式(x1:层数))
	 */
	public String dropGoods;
	
	/**
	 * 随机掉落百分比(1-100)
	 */
	public int dropGoodsRand;
	
	/**
	 * 地图
	 */
	public int mapId;
	
	/**
	 * 掉落的通天币(大胜_数量|中胜_数量|小胜_数量)
	 */
	public String star;
	
	/**
	 * 登顶额外奖励星数
	 */
	public int maxFloorExtraStarNum;
	
	/**
	 * 最大层数
	 */
	public int maxFloor;
	
	/**
	 * 每天重置次数
	 */
	public int resetNum;
	
	/**
	 * 历史登塔最高奖励百分比
	 */
	public String historyFloorPercent;
	
	@FieldIgnore
	Map<WinLevel,Integer> starMap;
	
	@FieldIgnore
	private Map<Integer, String> dropGoodsMaps = new HashMap<>();
	
	@FieldIgnore
	private List<HistoryFloorPercent> historyFloorPercentList = new ArrayList<>();
	
	@Override
	public void initialize() {
		starMap = new HashMap<>();
		List<String[]> list = StringUtils.delimiterString2Array(star);
		for(String[] str:list){
			int winLevel = Integer.valueOf(str[0]);
			starMap.put(WinLevel.getByCode(winLevel), Integer.valueOf(str[1]));
		}
		
		dropGoodsMaps = StringUtils.delimiterString2StringMap(dropGoods);
		
		historyFloorPercentList = HistoryFloorPercent.valueListOf(historyFloorPercent);
		
		this.star = null;
		this.dropGoods = null;
		this.historyFloorPercent = null;
	}

	public int getStar(WinLevel winLevel) {
		if(starMap.get(winLevel) == null){
			return 0;
		}
		return starMap.get(winLevel);
	}
	
	public RewardObject getReward(int floor){
		if(isOpen(floor) == false){
			return null;
		}
		int random = RandomUtils.nextInt(0, 100);
		RewardObject rewardObject = null;
		if(dropGoodsRand < random){
			rewardObject = new RewardObject();
			Set<Integer> keys = dropGoodsMaps.keySet();
			Integer[] keysArray = new Integer[keys.size()];
			keys.toArray(keysArray);
			int index = RandomUtils.nextIntIndex(keysArray.length);
			int goodsId = keysArray[index];
			int goodsNum = FormulaHelper.executeCeilInt(dropGoodsMaps.get(goodsId), floor);
			rewardObject.id = goodsId;
			rewardObject.num = goodsNum;
		}
		return rewardObject;
	}
	
	private boolean isOpen(int floor){
		return FormulaHelper.executeCeilInt(openAwardExpr, floor) > 0;
	}

	public int getHistoryFloorPercent(int floor) {
		for (HistoryFloorPercent FloorPercent : historyFloorPercentList) {
			if (floor >= FloorPercent.minFloor && floor <= FloorPercent.maxFloor){
				return FloorPercent.percent;
			}
		}
		return 0;
	}
	
	/**
	 * 昨日登塔最高奖励百分比
	 * @author 0x737263
	 *
	 */
	public static class HistoryFloorPercent {
		public int minFloor;
		public int maxFloor;
		public int percent;

		public static List<HistoryFloorPercent> valueListOf(String lastFloorPercent) {
			List<HistoryFloorPercent> list = new ArrayList<>();
			List<String[]> stringList = StringUtils.delimiterString2Array(lastFloorPercent);
			for(String[] item : stringList) {
				HistoryFloorPercent vo = new HistoryFloorPercent();
				vo.minFloor = Integer.valueOf(item[0]);
				vo.maxFloor = Integer.valueOf(item[1]);
				vo.percent = Integer.valueOf(item[2]);
				list.add(vo);
			}
			return list;
		}
	}
	
}
