package com.jtang.gameserver.module.adventures.vipactivity.facade;

import java.util.List;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.HeroComposeResultResponse;

/**
 * 仙人合成业务接口
 * @author 0x737263
 *
 */
public interface HeroComposeFacade {

	/**
	 * 仙人合成
	 * @param actorId		角色id
	 * @param useTicket		是否使用点券
	 * @param uuidList		装备uuid列表
	 * @return	返回合成结果
	 */
	TResult<HeroComposeResultResponse> compose(long actorId, boolean useTicket, List<Integer> heroIdList);
}
