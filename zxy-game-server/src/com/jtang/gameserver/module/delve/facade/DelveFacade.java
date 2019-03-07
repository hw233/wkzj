package com.jtang.gameserver.module.delve.facade;

import java.util.List;
import java.util.Map;

import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.delve.handler.response.LastDelveResponse;
import com.jtang.gameserver.module.delve.model.DelveResult;
import com.jtang.gameserver.module.delve.model.DoDelveResult;

/**
 * 
 * @author 0x737263
 *
 */
public interface DelveFacade {
	
//	/**
//	 * 获取角色的潜修室数据
//	 * @param actorId
//	 * @return
//	 */
//	public DelveVO get(long actorId);
//	
//	/**
//	 * 升级潜修室
//	 * @param actorId
//	 * @return 
//	 */
//	public TResult<Integer> upgrade(long actorId);
	
	/**
	 * 立即潜修
	 * @param actorId 角色ID
	 * @param heroId  仙人ID
	 * @param delveType  是否使用潜修石
	 * @return 
	 */
	public TResult<Map<HeroVOAttributeKey, DoDelveResult>> doDelve(long actorId, int heroId, int delveType);

	/**
	 * 重修
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	public Result repeatDelve(long actorId, int heroId);

	/**
	 * 获取仙人上次潜修属性
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	public TResult<LastDelveResponse> lastDelve(long actorId, int heroId);

	/**
	 * 一键潜修
	 * @param actorId
	 * @param heroId
	 * @param type
	 * @param attribute 
	 * @return
	 */
	public TResult<List<DelveResult>> oneKeyDelve(long actorId, int heroId, int type, List<Integer> attribute);
	
}
