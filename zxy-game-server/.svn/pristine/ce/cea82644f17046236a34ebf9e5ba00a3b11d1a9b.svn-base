package com.jtang.gameserver.module.adventures.bable.model;

import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;



public class BableBattleResult extends IoBufferSerializer{

	/**
	 * 战斗数据 {@code FightData}
	 */
	private FightData fightData;
	/**
	 * 层数
	 */
	private int floorNum;
	
	/**
	 * 获得的星星数
	 */
	private int starNum;
	
	/**
	 * 获得物品ID
	 */
	private int goodsId;
	
	/**
	 * 获得物品数量
	 */
	private int goodsNum;
	
	/**
	 * 是否到达顶层(1:到达，0:未到达)
	 */
	private byte isTopFloor = 0;
	
	/**
	 * 额外获得星星数（vip奖励）
	 */
	private int extStar;
	
//	/**
//	 * 下一关阵容
//	 */
//	private Map<Integer,Integer> monster;
	
	/**
	 * 已使用重试次数
	 */
	public int useRefight;

	@Override
	public void write() {
		this.writeBytes(fightData.getBytes());
		this.writeInt(this.floorNum);
		this.writeInt(this.starNum);
		this.writeInt(this.goodsId);
		this.writeInt(this.goodsNum);
		this.writeByte(this.isTopFloor);
		this.writeInt(this.extStar);
		//this.writeIntMap(monster);
		this.writeInt(this.useRefight);
	}

	public BableBattleResult(FightData fightData, int floorNum, int starNum, int goodsId, int goodsNum, boolean isTopFloor, int extStar,Map<Integer,Integer> monster,int useRefight) {
		super();
		this.fightData = fightData;
		this.floorNum = floorNum;
		this.starNum = starNum;
		this.goodsId = goodsId;
		this.goodsNum = goodsNum;
		this.isTopFloor = isTopFloor ? (byte)1 : (byte)0;
		this.extStar = extStar;
		//this.monster = monster;
		this.useRefight = useRefight;
	}
	public BableBattleResult(FightData fightData, int floorNum) {
		super();
		this.fightData = fightData;
		this.floorNum = floorNum;
	}
	
	
}
