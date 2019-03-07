package com.jtang.gameserver.module.extapp.plant.dao;

import com.jtang.gameserver.dbproxy.entity.Plant;

public interface PlantDao {

	/**
	 * 获取玩家种植信息
	 * @param actorId
	 * @return
	 */
	Plant get(long actorId);

	/**
	 * 更新种植
	 * @param plant
	 */
	void update(Plant plant);

}
