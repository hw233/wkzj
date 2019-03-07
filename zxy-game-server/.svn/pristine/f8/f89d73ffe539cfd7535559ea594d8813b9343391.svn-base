/**
 * 
 */
package com.jtang.gameserver.module.extapp.deitydesc.facade;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.deitydesc.model.DeityDescendVO;

/**
 * 天神下凡活动
 * @author ligang
 *
 */
public interface DeityDescendFacade {

	/**
	 * 加载试炼洞信息
	 * @param actorId
	 * @return
	 */
	public TResult<DeityDescendVO> getDeityDescendInfo(long actorId);

	/**
	 * 砸蛋蛋
	 * @param actorId
	 * @param hitCount 砸蛋次数
	 * @return
	 */
	public TResult<List<RewardObject>> hitDeityDescend(long actorId, byte hitCount);
	
	/**
	 * 领取仙人
	 * @param actorId
	 * @param heroId 仙人id
	 * @return
	 */
	public Result receiveHero(long actorId);
	
	/**
	 * 状态
	 * @param actorId
	 * @param heroId 仙人id
	 * @return
	 */
	public Result getStatus(long actorId);
	
	/**
	 * 领取仙人
	 * @param actorId
	 * @param heroId 仙人id
	 * @return
	 */
	public DeityDescendVO getCurDeityDescendVO(long actorId);
}
