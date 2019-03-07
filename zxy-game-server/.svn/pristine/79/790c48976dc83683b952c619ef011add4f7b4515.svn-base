package com.jtang.gameserver.module.love.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

public class BossStateVO extends IoBufferSerializer{

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 剩余血量
	 */
	public int HP;
	
	/**
	 * 最大血量
	 */
	public int maxHp;
	
	/**
	 * 状态
	 * 0.锁定 1.可攻击 2.可领奖 3.已领奖
	 */
	public int state;
	
	/**
	 * 已经攻击次数
	 */
	public int fightNum;
	
	/**
	 * 解锁需要的点券
	 */
	public int unLockCostTicket;
	
	/**
	 * 刷新次数需要的点券
	 */
	public int flushCostTicket;
	
	/**
	 * 锁定状态解锁扣除物品数量
	 */
	public int goodsNum;
	
	/**
	 * 奖励列表
	 */
	public List<RewardObject> rewardList = new ArrayList<>();
	
	public BossStateVO(BossVO bossVO,BossFightVO fightVO,int unLockCostTicket,int flushCostTicket,int goodsNum){
		this.id = bossVO.id;
		int hp = 0;
		for(Integer i : bossVO.monsterHPMap.values()){
			hp += i;
		}
		this.HP = hp;
		this.maxHp = bossVO.maxHp;
		this.state = fightVO.state;
		this.fightNum = fightVO.monsterFightNum;
		this.unLockCostTicket = unLockCostTicket;
		this.flushCostTicket = flushCostTicket;
		this.goodsNum = goodsNum;
		this.rewardList = fightVO.rewardList;
	}
	
	@Override
	public void write() {
		writeInt(id);
		writeInt(HP);
		writeInt(maxHp);
		writeInt(state);
		writeInt(fightNum);
		writeInt(unLockCostTicket);
		writeInt(flushCostTicket);
		writeInt(goodsNum);
		writeShort((short) rewardList.size());
		for(RewardObject rewardObject : rewardList){
			writeBytes(rewardObject.getBytes());
		}
	}
}
