package com.jtang.worldserver.module.crossbattle.facade;

import com.jiatang.common.crossbattle.request.AttackActorResultG2W;
import com.jiatang.common.crossbattle.request.SignupG2W;
import com.jiatang.common.crossbattle.response.ActorCrossDataW2G;
import com.jiatang.common.crossbattle.response.ActorPointW2G;
import com.jiatang.common.crossbattle.response.AttackPlayerW2G;
import com.jiatang.common.crossbattle.response.CrossBattleConfigW2G;
import com.jiatang.common.crossbattle.response.EndRewardW2G;
import com.jiatang.common.crossbattle.response.ExchangePointW2G;
import com.jiatang.common.crossbattle.response.HomeServerRankW2G;
import com.jiatang.common.crossbattle.response.LastBattleResultW2G;
import com.jiatang.common.crossbattle.response.LineupW2G;
import com.jiatang.common.crossbattle.response.ServerRankW2G;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.worldserver.module.crossbattle.model.ServerCrossData;

public interface CrossBattleFacade {
	/**
	 * 报名
	 * @param serverId
	 * @param signupRequest
	 * @return
	 */
	void signup(int serverId, SignupG2W signupRequest);
	
	/**
	 * 获取对阵数据
	 * @param serverId
	 * @return
	 */
	ServerCrossData getCrossData(int serverId);
	
	/**
	 * 攻击玩家
	 * @param actorId
	 * @param targetActorId
	 * @return
	 */
	TResult<AttackPlayerW2G> attackPlayer(int serverId, long actorId, long targetActorId);
	
	/**
	 * 处理攻击结果
	 * @param serverId
	 * @param attackPlayerResultRequest
	 * @return
	 */
	void attackPlayerResult(int serverId, long actorId, AttackActorResultG2W attackPlayerResultRequest);

	/**
	 * 获取对战数据
	 * @param serverId
	 * @param actorId
	 * @return
	 */
	TResult<ActorCrossDataW2G> getCrossData(int serverId, long actorId);

	/**
	 * 获取阵形
	 * @param serverId
	 * @param actorId
	 * @param targetActorId
	 * @return
	 */
	TResult<LineupW2G> getLineup(int serverId, long actorId, long targetActorId);
	
	/**
	 * 获取积分排行榜
	 * @param serverId
	 * @param actorId
	 * @return
	 */
	TResult<ServerRankW2G> getServerRank(int serverId, long actorId);

	/**
	 * 获取本服务器排行榜
	 * @param serverId
	 * @param actorId
	 * @return
	 */
	TResult<HomeServerRankW2G> getHomeServerRank(int serverId, long actorId);

	/**
	 * 今日战斗结果信息
	 * @param serverId
	 * @param actorId
	 * @return
	 */
	TResult<EndRewardW2G> dayBattleEndRewardResult(int serverId, long actorId);

	/**
	 * 兑换贡献点
	 * @param serverId
	 * @param actorId
	 * @param configId
	 * @return
	 */
	TResult<ExchangePointW2G> exchangePoint(int serverId, long actorId, int configId);

	/**
	 * 获取配置
	 * @return
	 */
	TResult<CrossBattleConfigW2G> getConfig(long actorId);

	/**
	 * 获取玩家贡献点
	 * 
	 * @param actorId
	 * @return
	 */
	TResult<ActorPointW2G> getActorPoint(long actorId, int serverId);
	
	/**
	 * 上次对阵结果
	 */
	TResult<LastBattleResultW2G> getLastBattleResult(long actorId);
	
	/**
	 * 设置已读
	 * @param actorId
	 * @return
	 */
	Result setReadFlag(long actorId);
}
