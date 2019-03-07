package com.jtang.gameserver.admin.facade;

import com.jtang.core.result.Result;

public interface EquipMaintianFacade {

	/**
	 * 添加装备
	 * @param uid
	 * @param serverId
	 * @param platformId
	 * @param equipId
	 * @return
	 */
	public abstract Result addEquip(long actorId, int equipId);

	/**
	 * 删除装备
	 * @param actorId
	 * @param uuid
	 * @return
	 */
	public abstract Result deleteEquip(long actorId, long uuid);
	
	
	/**
	 * 修改装备等级
	 * @param actorId
	 * @param uuid
	 * @param level
	 * @return
	 */
	public Result modifyEquipLevel(long actorId, long uuid, int level);


	/**
	 * 添加所有装备
	 * @param actorId
	 * @return
	 */
	public abstract Result addAllEquip(long actorId);
}