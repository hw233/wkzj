package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;


/**
 * 充值事件
 * @author pengzy
 *
 */
public class RechargeTicketsEvent extends GameEvent{

	/**
	 * 自游戏以来，充值的点券总数
	 */
	public int totalSum;
	
	/**
	 * 充值金额（人民币）
	 */
	public int money;
	
	/**
	 * 当前充值点券数
	 */
	public int currentTicket;
	
	/**
	 * 充值id
	 */
	public int rechargeId;
	
	public RechargeTicketsEvent(long actorId, int totalSum, int money, int currentTicket, int rechargeId) {
		super(EventKey.TICKETS_RECHARGE, actorId);
		this.totalSum = totalSum;
		this.money = money;
		this.currentTicket = currentTicket;
		this.rechargeId = rechargeId;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(totalSum);
		list.add(money);
		list.add(currentTicket);
		list.add(rechargeId);
		return list;
	}
}
