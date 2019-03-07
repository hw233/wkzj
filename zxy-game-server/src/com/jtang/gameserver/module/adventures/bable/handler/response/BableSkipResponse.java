package com.jtang.gameserver.module.adventures.bable.handler.response;

import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

public class BableSkipResponse extends IoBufferSerializer {

	/**
	 * 登塔状态
	 */
	public byte bableState;
	
	/**
	 * 获得的星星
	 */
	public int star;
	
	/**
	 * 额外获得的星星
	 */
	public int extStar;
	
	/**
	 * 获得的金币
	 */
	public int goodsId;
	
	/**
	 * 数量
	 */
	public int goodsNum;
	
	/**
	 * 目前所在楼层
	 */
	public int floor;
	
//	/**
//	 * 下一层怪物
//	 */
//	public Map<Integer,Integer> monster;
	
	/**
	 * 是否登顶
	 */
	public byte isTopFloor;
	
	public BableSkipResponse(byte bableState,int star,int extStar,int goodsId,int goodsNum,int floor,Map<Integer,Integer> monster,boolean isTopFloor){
		this.bableState = bableState;
		this.star = star;
		this.extStar = extStar;
		this.goodsId = goodsId;
		this.goodsNum = goodsNum;
		this.floor = floor;
		this.isTopFloor = isTopFloor ? (byte)1 : (byte)0;
		//this.monster = monster;
	}
	
	@Override
	public void write() {
		writeByte(bableState);
		writeInt(star);
		writeInt(extStar);
		writeInt(goodsId);
		writeInt(goodsNum);
		writeInt(floor);
//		writeIntMap(monster);
		writeByte(isTopFloor);
	}
}
