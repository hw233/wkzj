package com.jtang.gameserver.module.love.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.love.handler.response.FightVideoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveFightInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveRankInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveRankListResponse;

public interface LoveFightFacade {

	/**
	 * 获取排行信息
	 * @param actorId
	 * @return
	 */
	public TResult<LoveRankInfoResponse> getInfo(long actorId);

	/**
	 * 请求战斗
	 * @param actorId
	 * @param targetActorId 
	 * @return
	 */
	public Result loveFight(long actorId, long targetActorId);
	
	/**
	 * 购买战斗次数
	 * @param actorId
	 * @return
	 */
	public Result buyFightNum(long actorId);

	/**
	 * 获取固定排行榜
	 * @return
	 */
	public TResult<LoveRankListResponse> getTopRank();

	/**
	 * 获取战斗信息
	 * @param actorId
	 * @return
	 */
	public TResult<LoveFightInfoResponse> getFightInfo(long actorId);

	/**
	 * 获取战斗录像
	 * @param actorId
	 * @param fightVideoId
	 * @return
	 */
	public TResult<FightVideoResponse> getFightVideo(long actorId,long fightVideoId);

	/**
	 * 获取挑战对手
	 * @param actorId
	 * @return
	 */
	public TResult<LoveRankListResponse> getFightRank(long actorId);
	
	/**
	 * 离婚删除排行数据
	 * @param actorId
	 */
	public Result removeRank(long actorId);
	
	/**
	 * 离婚调用接口
	 */
	public Result unMarry(long actorId);

}
