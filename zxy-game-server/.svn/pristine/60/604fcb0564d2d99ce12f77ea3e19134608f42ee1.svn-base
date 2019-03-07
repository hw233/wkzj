package com.jtang.gameserver.dataconfig.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.rhino.FormulaHelper;

/**
 * 登天塔跳层配置
 * @author ludd
 *
 */
@DataFile(fileName = "bableSkipConfig")
public class BableSkipConfig implements ModelAdapter {
	@FieldIgnore
	private static final Logger LOGGER = LoggerFactory.getLogger(BableSkipConfig.class);
	
	/**
	 * 登天塔id  1.1-50角色等级    2.51-99角色等级    3.99级以上
	 */
	private int bableId;
	
	/**
	 * 跳层角色等级
	 */
	private int useActorLevel;
	/*
	 * 跳层昨日需要达到的层数
	 */
	private int skipYesterdayFloor;
	/**
	 * 可到达楼层(x1:昨日最高楼层)
	 */
	private String skipFloorNum;
	/**
	 * 跳层获得的星星数( x1：昨日最高楼层；x2 ：每层额外获得的星星数）
	 */
	private String rewardStarNum;
	/**
	 * 跳层获得的金币数（x1:昨日最高楼层; x2:金币加成百分比）
	 */
	private String rewardGoldNum;
	/**
	 * 消耗点券
	 */
	private float useTicket;
	
	private int consumeGoodsId;
	private float eachFloorConsumeGoodsNum;
	private int eachFloorConsumeTime;
	public int getBableId() {
		return bableId;
	}
	public int getUseActorLevel() {
		return useActorLevel;
	}
	public int getSkipYesterdayFloor() {
		return skipYesterdayFloor;
	}
	public int getSkipFloorNum(int floor) {
		return FormulaHelper.executeCeilInt(skipFloorNum, floor);
	}
	public int getRewardStarNum(int nowFloor,int historyFloor, int extStar) {
		return FormulaHelper.executeCeilInt(rewardStarNum,nowFloor, historyFloor, extStar);
	}
	public int getRewardGoldNum(int nowFloor,int historyFloor, int percent) {
		double p = percent / 100.0d;
		return FormulaHelper.executeCeilInt(rewardGoldNum,nowFloor, historyFloor, p);
	}
	public float getUseTicket() {
		return useTicket;
	}
	@Override
	public void initialize() {
	}
	
	public int getConsumeGoodsId() {
		return consumeGoodsId;
	}
	
	public float getEachFloorConsumeGoodsNum() {
		return eachFloorConsumeGoodsNum;
	}
	
	public int getEachFloorConsumeTime() {
		return eachFloorConsumeTime;
	}
	

}
