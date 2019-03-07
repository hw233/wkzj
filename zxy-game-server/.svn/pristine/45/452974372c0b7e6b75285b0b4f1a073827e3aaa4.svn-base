package com.jtang.gameserver.module.user.facade;

import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;

public interface VipFacade {

	/**
	 * 充值添加点券(仅适用web接口)
	 * @param actorId 角色Id
	 * @param rechargeNum  基本点券
	 * @param giveNum 额外赠送点券
	 * @param money 金额
	 * @param rechargeId 
	 * @return
	 */
	boolean rechargeTicket(long actorId, int rechargeNum, int giveNum, int money, int rechargeId);
	
	/**
	 * 赠送添加点券
	 * @param actorId
	 * @param addType
	 * @param giveNum
	 * @return
	 */
	boolean addTicket(long actorId, TicketAddType addType, int giveNum);

	/**
	 * 扣除点券
	 * @param actorId
	 * @param decreaseType
	 * @param ticketNum
	 * @param id 产出物品id
	 * @param num 产出物品num
	 * @return
	 */
	boolean decreaseTicket(long actorId, TicketDecreaseType decreaseType, int ticketNum, int id, int num);
	
	/**
	 * 获取vip等级
	 * @param actorId
	 * @return
	 */
	int getVipLevel(long actorId);
	
	/**
	 * 获取vip等级特权数据
	 * @param vipLevel
	 * @return
	 */
	VipPrivilege getVipPrivilege(int vipLevel);

	/**
	 * 获取玩家点券
	 * @return
	 */
	int getTicket(long actorId);
	
	/**
	 * 获取总的充值点券
	 * @param actorId
	 * @return
	 */
	int getTotalRechargeTicket(long actorId);
	
	/**
	 * 发放vip等级奖励
	 * @param actorId
	 * @param currentLevel
	 * @param targetLevel
	 */
	void sendRewardByVipLevel(long actorId, int currentLevel, int targetLevel);
	/**
	 * 更新vip等级
	 * @param actorId
	 * @param level
	 */
	void updateVipLevel(long actorId, int level);
	
	/**
	 *  是否有足够的点券
	 * @param actorId
	 * @param tickitNum
	 * @return
	 */
	boolean hasEnoughTicket(long actorId, int tickitNum);
	
	/**
	 * 获取充值次数
	 * @param actorId
	 * @return
	 */
	int getRechargeNum(long actorId);
	
	/**
	 * 获取首次充值时间
	 * @param actorId
	 * @return
	 */
	int getFirstRechargeTime(long actorId);
	
}
