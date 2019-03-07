package com.jtang.gameserver.module.ally.dao;

import com.jtang.gameserver.module.ally.model.CoordinateVO;

public interface AllyCoordinateDao {

	/**
	 * 获取角色的CoordinateVO
	 * @param actorId
	 * @return
	 */
	public CoordinateVO get(long actorId);
	
	/**
	 * 角色上线时添加
	 * @param coordianteVO
	 */
	public void add(long actorId, CoordinateVO coordianteVO);

	/**
	 * 更新角色的CoordinateVO
	 * @param actorId
	 * @return
	 */
	public void update(long actorId, double longitude, double latitude, double height);
}
