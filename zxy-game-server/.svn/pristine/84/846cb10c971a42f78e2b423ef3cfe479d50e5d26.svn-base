package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.goods.constant.GoodsRule;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;

/**
 * 抢夺结果事件（用于触发任务）
 * @author ludd
 *
 */
public class SnatchResultEvent extends GameEvent {

	/**
	 * 抢夺敌人类型
	 */
	public SnatchEnemyType snatchEnemyType;
	
	/**
	 * 目标角色id
	 */
	public long targetActorId;
	
	/**
	 * 胜利等级
	 */
	public WinLevel winLevel;
	
	/**
	 * 获得积分
	 */
	public int ownScore;
	
	/**
	 * 抢夺的物品id.
	 * 如果是金币，则是金币的id
	 */
	public int goodsId;
	
	/**
	 * 抢夺的物品数量
	 */
	public int goodsNum;
	
	
	public SnatchResultEvent(long actorId, SnatchEnemyType snatchEnemyType, long targetActorId, WinLevel winLevel, int score, int goodsId,
			int goodsNum) {
		super(EventKey.SNATCH_RESULT, actorId);
		this.snatchEnemyType = snatchEnemyType;
		this.targetActorId = targetActorId;
		this.winLevel = winLevel;
		this.ownScore = score;
		this.goodsId = goodsId;
		this.goodsNum = goodsNum;
	}
	
	/**
	 * 是否为金币
	 * @return
	 */
	public boolean isGold() {
		return this.goodsId == GoodsRule.GOODS_ID_GOLD;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(snatchEnemyType.getType());
		list.add(targetActorId);
		list.add(winLevel.getCode());
		list.add(ownScore);
		list.add(goodsId);
		list.add(goodsNum);
		return list;
	}

}
