package com.jtang.gameserver.module.extapp.plant.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.plant.handler.response.PlantHarvestResponse;
import com.jtang.gameserver.module.extapp.plant.handler.response.PlantResponse;

public interface PlantFacade {

	/**
	 * 获取玩家种植信息
	 * @param actorId
	 * @return
	 */
	TResult<PlantResponse> getPlant(long actorId);

	/**
	 * 加速
	 * @param actorId
	 * @return
	 */
	TResult<PlantResponse> plantQuicken(long actorId);

	/**
	 * 收获
	 * @param actorId
	 * @return
	 */
	TResult<PlantHarvestResponse> plantHarvest(long actorId);

	/**
	 * 种植
	 * @param actorId
	 * @param plantId 
	 * @return
	 */
	TResult<PlantResponse> plant(long actorId, int plantId);

}
