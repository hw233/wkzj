package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
/**
 * 集众降魔配置
 * @author ludd
 *
 */
@DataFile(fileName = "demonGlobalConfig")
public class DemonGlobalConfig implements ModelAdapter {

	/**
	 * 开放日期
	 */
	private String openDate;
	/**
	 * 开放时间
	 */
	private String openTimes;
	
	/**
	 * 默认查看排行榜难度
	 */
	private int defaultRankDifficult;
	
	/**
	 * 查看排名数量
	 */
	private int rankNum;
	
	/**
	 * 兑换星期
	 */
	private String exchangeWeek;
	/**
	 * 兑换时间
	 */
	private String exchangeTime;
	
	/**
	 * 需要角色等级
	 */
	private int actorLevel;
	@Override
	public void initialize() {
		
	}
	
	
	public int getDefaultRankDifficult() {
		return defaultRankDifficult;
	}
	
	public String getOpenTimes() {
		return openTimes;
	}
	
	public int getRankNum() {
		return rankNum;
	}
	
	public String getExchangeTime() {
		return exchangeTime;
	}
	
	public String getExchangeWeek() {
		return exchangeWeek;
	}
	
	public String getOpenDate() {
		return openDate;
	}
	
	public int getActorLevel() {
		return actorLevel;
	}
	
	
	
	

}
