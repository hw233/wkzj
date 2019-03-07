package com.jtang.gameserver.module.adventures.vipactivity.facade;

import java.util.List;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.EquipComposeResultResponse;

/**
 * 炼器宗师业务接口
 * @author 0x737263
 *
 */
public interface EquipComposeFacade {

	/**
	 * 合成装备
	 * @param actorId		角色id
	 * @param equipType		装备类型
	 * @param useTicket		是否使用点券
	 * @param uuidList		装备uuid列表
	 * @return	返回合成后的装备uuid
	 */
	TResult<EquipComposeResultResponse> compose(long actorId, int equipType, boolean useTicket, List<Long> uuidList);
	
}
