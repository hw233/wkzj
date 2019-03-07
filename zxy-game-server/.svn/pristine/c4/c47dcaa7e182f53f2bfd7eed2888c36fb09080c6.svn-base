package com.jtang.gameserver.module.vampiir.facade;

import java.util.List;
import java.util.Map;

import com.jiatang.common.model.HeroVO;
import com.jtang.core.result.ListResult;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.vampiir.model.ExchangeVO;

/**
 * 
 * @author ludd
 * 
 */
public interface VampiirFacade {

	/**
	 * 仙人魂魄兑换
	 * @param actorId
	 * @param heroSouls
	 * @param heroIds 
	 * @return
	 */
	ListResult<ExchangeVO> exchange(long actorId,Map<Integer, Integer> heroSouls);

//	/**
//	 * 获取角色的吸灵室数据
//	 * 
//	 * @param actorId
//	 * @return
//	 */
//	public Vampiir get(long actorId);
//
//	/**
//	 * 升级吸灵室
//	 * 
//	 * @param actorId
//	 * @return
//	 */
//	public TResult<Integer> upgrade(long actorId);
//
	/**
	 * 吸灵
	 * @param actorId
	 * @param heroId
	 * @param heroIds 被吸收掉的仙人
	 * @param heroSouls key:魂魄id value：魂魄数量
	 * @param goods key:物品id value：物品数量
	 * @return
	 */
	public TResult<HeroVO> doVampiir(Long actorId, int heroId, List<Integer> heroIds, Map<Integer, Integer> heroSouls, Map<Integer, Integer> goods);
	
	
	
}
