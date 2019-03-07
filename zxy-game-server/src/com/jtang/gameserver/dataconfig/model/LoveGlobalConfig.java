package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "loveGlobalConfig")
public class LoveGlobalConfig implements ModelAdapter {
	
	/**
	 * 地图
	 */
	public int map;
	
	/**
	 * 固定显示排行
	 */
	public int viewRank;
	
	/**
	 * 挑战次数
	 */
	public int fightNum;
	
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
	 * 发奖最小排行
	 */
	public int minRank;
	
	/**
	 * 发奖最大排行
	 */
	public int maxRank;
	
	/**
	 * 补满挑战次数扣除的点券
	 */
	private String costTicket;
	
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
	 * 战斗记录保存条数
	 */
	public int fightInfo;
	
	/**
	 * 发放世界公告的最低名次
	 */
	public int sendChat;
	
	/**
	 * 发奖时间
	 */
	public int sendTime;
	
	@FieldIgnore
	private List<String> list = new ArrayList<>();
	
	@FieldIgnore
	public Map<Integer,Integer> ticketMap = new HashMap<>();


	@Override
	public void initialize() {
		list = StringUtils.delimiterString2List(upList, Splitable.ATTRIBUTE_SPLIT);
		ticketMap = StringUtils.delimiterString2IntMap(costTicket);
	}
	
	public List<Integer> getUpList(int actorRank){
		List<Integer> rankList = new ArrayList<>();
		for(String str:list){
			int rank = FormulaHelper.executeCeilInt(str, actorRank);
			rankList.add(rank);
		}
		return rankList;
	}
	
	public int getCostTicket(int flushNum){
		return ticketMap.get(flushNum);
	}

}
