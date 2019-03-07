package com.jtang.gameserver.module.extapp.plant.handler;

public interface PlantCmd {

	
	/**
	 * 获取种植信息
	 * 请求:{@code Request}
	 * 返回:{@code PlantResponse}
	 */
	byte GET_PLANT = 1;
	
	/**
	 * 种植活动状态推送
	 * 推送:{@code PlantStateResponse}
	 */
	byte PUSH_PLANT_STATE = 2;
	
	/**
	 * 种植成长状态推送
	 * 推送:{@code PlantResponse}
	 */
	byte PUSH_PLANT_GROW = 3;
	
	/**
	 * 加速
	 * 请求:{@code Request}
	 * 返回:{@code PlantResponse}
	 */
	byte PLANT_QUICKEN = 4;
	
	/**
	 * 收获
	 * 请求:{@code Request}
	 * 返回:{@code PlantHarvestResponse}
	 */
	byte PLANT_HARVEST = 5;
	
	/**
	 * 种植
	 * 请求:{@code PlantRequest}
	 * 返回:{@code PlantResponse}
	 */
	byte PLANT = 6;
}
