package com.jtang.gameserver.module.demon.handler;





/**
 * 集众降魔命令
 * @author ludd
 *
 */
public interface DemonCmd {
	/**
	 * 获取阵营列表数据
	 * 请求:{@code Request}
	 * 响应:{@code DemonCampDataResponse}
	 */
	byte DEMON_CAMP_DATA = 1;
	/**
	 * 加入阵营
	 * 请求:{@code Request}
	 * 响应:{@code DemonCampDataResponse}
	 */
	byte JOIN_CAMP = 2;
	/**
	 *  请求:{@code AttackBossRequest}
	 *  推送:{@code DemonBossAttackResponse}
	 */
	byte ATTACK_BOSS = 3;
	/**
	 * 请求:{@code AttackPlayerRequest}
	 * 推送:{@code DemonPlayerAttackResponse}
	 */
	byte ATTACK_PLAYER = 4;
	
	/**
	 * BOSS 属性变更
	 * 推送:{code BossAttChangeResponse}
	 */
	byte BOSS_ATT_CHANGE = 5;
	
	/**
	 * 玩家功勋值变更
	 * 推送:{@code FeatsChangeResponse}
	 */
	byte ACOTR_FEATS_CHANGE = 6;
	
	/**
	 * 加入阵营推送其他玩家
	 * 推送:{@code PlayerJoinResponse}
	 */
	byte PUSH_ACTOR_JOIN = 7;
	
	/**
	 * 获取降魔积分
	 * 响应:{@code DemonScoreResponse}
	 */
	byte GET_DEMON_SCORE = 8;
	
	/**
	 * 降魔积分兑换
	 * 请求:{@code DemonExchangeRequest}
	 * 响应:{@code DemonExchangeResponse}
	 */
	byte EXCHANGE_SCORE = 9;
	
	/**
	 * 结束奖励
	 * 推送:{@code DemonEndRewardResponse}
	 */
	byte PUSH_DENMON_END_REWARD = 10;
	
	/**
	 * 查看上次排名
	 * 请求:{@code Request}
	 * 响应:{@code DemonLastRankResponse}
	 */
	byte VIEW_LAST_RANK = 11;
	
	/**
	 * 查看上次奖励
	 * 请求:{@code Request}
	 * 响应:{@code DemonLastRewardResponse}
	 */
	byte VIEW_LAST_REWARD = 12;
	
	/**
	 * 设置奖励已读
	 * 请求:{@code Request}
	 * 响应:{@code Response}
	 */
	byte SET_REWARD_READ = 13;
	
	/**
	 * 请求:{@code Request}
	 * 
	 */
	byte GET_DEMON_TIME = 14;
	
	
	
}
