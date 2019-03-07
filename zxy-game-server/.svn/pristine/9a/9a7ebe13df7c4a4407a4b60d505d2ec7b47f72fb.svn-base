package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "powerGlobalConfig")
public class PowerGlobalConfig implements ModelAdapter {
	
	/**
	 * 排行榜上榜等级
	 */
	public int level;
	
	/**
	 * 地图
	 */
	public int map;
	
	/**
	 * 固定显示排行
	 */
	public int viewRank;
	
	/**
	 * 挑战间隔时间(秒)
	 */
	public int fightTime;
	
	/**
	 * 向上可挑战名次
	 */
	public int viewUp;
	
	/**
	 * 向下可挑战名次
	 */
	public int viewDown;
	
	/**
	 * 向上显示排行节点
	 */
	private String upList;
	
	/**
	 * 显示世界公告的排行
	 */
	public int sendChat;
	
	/**
	 * 发奖最小排行
	 */
	public int minRank;
	
	/**
	 * 发奖最大排行
	 */
	public int maxRank;
	
	/**
	 * 向上挑战节点排行显示个数
	 */
	public int upListNum;
	
	/**
	 * 向上周围排行显示个数
	 */
	public int upRankNum;
	
	/**
	 * 向下周围排行显示个数
	 */
	public int downRankNum;
	
	/**
	 * 势力币id
	 */
	public int goodsId;
	
	/**
	 * 每日获取的气势上限
	 */
	public int dayMorale;
	
	@FieldIgnore
	private List<String> list = new ArrayList<>();

	@Override
	public void initialize() {
		list = StringUtils.delimiterString2List(upList, Splitable.ATTRIBUTE_SPLIT);
	}
	
	public List<Integer> getUpList(int actorRank){
		List<Integer> rankList = new ArrayList<>();
		for(String str:list){
			int rank = FormulaHelper.executeCeilInt(str, actorRank);
			rankList.add(rank);
		}
		return rankList;
	}

}
