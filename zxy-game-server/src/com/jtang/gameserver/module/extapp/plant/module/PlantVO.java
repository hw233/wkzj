package com.jtang.gameserver.module.extapp.plant.module;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.PlantConfig;
import com.jtang.gameserver.dataconfig.service.PlantService;
import com.jtang.gameserver.dbproxy.entity.Plant;
import com.jtang.gameserver.module.extapp.plant.type.PlantType;

public class PlantVO extends IoBufferSerializer {
	
	/**
	 * 玩家id
	 */
	public long actorId;
	
	/**
	 * 今日种植次数
	 */
	public int todayPlant;
	
	/**
	 * 可种植次数
	 */
	public int plantCount;
	
	/**
	 * 今日收获次数
	 */
	public int todayHarvest;
	
	/**
	 * 总收获次数
	 */
	public int harvestCount;
	
	/**
	 * 种植状态
	 * (0.未种植 1.成长中 2.可收获)
	 */
	public int plantState;
	
	/**
	 * 种植的物品
	 */
	public int plantId;
	
	/**
	 * 剩余收获时间
	 */
	public int surplusTime;
	
	
	@Override
	public void write() {
		writeLong(actorId);
		writeInt(todayPlant);
		writeInt(plantCount);
		writeInt(todayHarvest);
		writeInt(harvestCount);
		writeInt(plantState);
		writeInt(plantId);
		writeInt(surplusTime);
	}


	public static PlantVO valueOf(Plant plant,PlantConfig plantConfig) {
		PlantVO plantVO = new PlantVO();
		plantVO.actorId = plant.getPkId();
		plantVO.todayPlant = plant.todayPlant;
		plantVO.plantCount = PlantService.getPlantGlobalConfig().dayPlant;
		plantVO.todayHarvest = plant.todayHarvest;
		plantVO.plantState = plant.plantState;
		plantVO.plantId = plant.plantId;
		if(plantConfig == null || plant.plantState != PlantType.GROW.getType()){
			plantVO.surplusTime = 0;
		}else{
			int time = plantConfig.growTime + plant.plantStartTime;
			plantVO.surplusTime = time - TimeUtils.getNow();
		}
		return plantVO;
	}
	
}
