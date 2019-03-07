package com.jtang.gameserver.module.user.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class ActorBuyResponse extends IoBufferSerializer {
	
	/**
	 * 活力购买次数
	 */
	public int vitNum;
	
	/**
	 * 消耗点券
	 */
	public int vitCostTicket;
	
	/**
	 * 购买上限
	 */
	public int vitMaxNum;
	
	/**
	 *  购买精力次数
	 */
	public int energyNum;
	
	/**
	 * 购买需要消耗的点券数
	 */
	public int energyCostTicket;
	
	/**
	 * 购买上限
	 */
	public int energyMaxNum;
	
	/**
	 *  购买金币次数
	 */
	public int goldNum;
	
	/**
	 * 购买金币需要消耗的点券数
	 */
	public int goldCostTicket;
	
	/**
	 * 购买上限
	 */
	public int goldMaxNum;
	
	/**
	 * 改名需要的点券
	 */
	public int renameTicket;
	
	/**
	 * 改性别需要的点券
	 */
	public int resexTicket;
	
	public ActorBuyResponse(){
	}
	
	@Override
	public void write() {
		writeInt(vitNum);
		writeInt(vitCostTicket);
		writeInt(vitMaxNum);
		writeInt(energyNum);
		writeInt(energyCostTicket);
		writeInt(energyMaxNum);
		writeInt(goldNum);
		writeInt(goldCostTicket);
		writeInt(goldMaxNum);
		writeInt(renameTicket);
		writeInt(resexTicket);
	}
}
