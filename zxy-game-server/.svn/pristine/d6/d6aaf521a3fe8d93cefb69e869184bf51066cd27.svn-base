package com.jtang.gameserver.module.gift.facade;

import com.jtang.core.result.MapResult;
import com.jtang.core.result.Result;
import com.jtang.gameserver.dbproxy.entity.Gift;

/**
 * 礼物的服务类
 * @author vinceruan
 *
 */
public interface GiftFacade {
	
	/**
	 * 获取礼物信息
	 * @param actorId
	 * @return
	 */
	Gift get(long actorId);
	
	/**
	 * 送礼
	 * @param actorId
	 * @param allyActorId
	 * @return
	 */
	Result giveGift(long actorId, long allyActorId);
	
	/**
	 * 收礼 
	 * @param actorId
	 * @param allyActorId
	 * @return MapResult的item是Map<物品的全局id,数量>
	 */
	MapResult<Long, Integer> acceptGift(long actorId, long allyActorId); 
	
	/**
	 * 打开礼包
	 * @param actorId
	 * @return MapResult的item是Map<物品的全局id,数量>
	 */
	MapResult<Long, Integer> openGiftPackage(long actorId);

	/**
	 * 一键送礼
	 * @param actorId
	 * @return
	 */
	Result oneKeyGiveGift(long actorId);

	/**
	 * 一键收礼
	 * @param actorId
	 * @return
	 */
	MapResult<Long, Integer> oneKeyAcceptGift(long actorId);

}
