package com.jtang.gameserver.module.demon.facade;


import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.demon.handler.response.DemonCampDataResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonExchangeResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonLastRankResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonLastRewardResponse;
/**
 * 集众降魔业务接口
 * @author ludd
 *
 */
public interface DemonFacade {

	/**
	 * 获取角色难度的所有阵营数据
	 * @param actorId
	 * @return
	 */
	TResult<DemonCampDataResponse> getDemonData(long actorId);

	/**
	 * 加入阵营
	 * @param actorId
	 * @return
	 */
	TResult<DemonCampDataResponse> joinCamp(long actorId);

	/**
	 * 攻击玩家
	 * @param actorId
	 * @param targetId
	 * @return
	 */
	Result attackPlayer(long actorId, long targetId, boolean useTicket);
	
	/**
	 * 攻击boss
	 * @param actorId
	 * @return
	 */
	Result attackBoss(long actorId, boolean useTicket);
	
	/**
	 * 兑换
	 * @param actorId
	 * @param id
	 * @param exchangeNum
	 * @return
	 */
	TResult<DemonExchangeResponse> exchangeReward(long actorId, int id, int exchangeNum);
	
	/**
	 * 是否在参与集众降魔
	 * @param actorId
	 * @return
	 */
	boolean isDemon(long actorId);
	
	/**
	 * 获取降魔积分
	 * @param actorId
	 * @return
	 */
	long getDemonScore(long actorId);
	
	/**
	 * 获取上次排名数据
	 * @param actorId
	 * @return
	 */
	TResult<DemonLastRankResponse> getLastRankData(long actorId);
	

	/**
	 * 获取上次奖励数据
	 * @param actorId
	 * @return
	 */
	TResult<DemonLastRewardResponse> getLastRewardData(long actorId);
	
	/**
	 * 设置奖励已读
	 * @param actorId
	 * @return
	 */
	public Result setRewardRead(long actorId);
	
	/**
	 * 添加降魔积分
	 * @param actorId
	 * @param score
	 * @return
	 */
	public boolean addDemonScore(long actorId, int score);
	
	
}
