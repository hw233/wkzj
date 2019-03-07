package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.PlantConfig;
import com.jtang.gameserver.dataconfig.service.PlantService;
import com.jtang.gameserver.module.extapp.plant.type.PlantType;

@TableName(name = "plant", type = DBQueueType.IMPORTANT)
public class Plant extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3625052696257134501L;

	/**
	 * 玩家id
	 */
	@Column(pk = true)
	private long actorId;

	/**
	 * 种植物品
	 */
	@Column
	public int plantId;

	/**
	 * 种植状态
	 * (0.未种植 1.成长中 2.可收获)
	 */
	@Column
	public int plantState;

	/**
	 * 开始种植时间
	 */
	@Column
	public int plantStartTime;

	/**
	 * 今天种植次数
	 */
	@Column
	public int todayPlant;

	/**
	 * 总种植次数
	 */
	@Column
	public int plantCount;

	/**
	 * 当天收获次数
	 */
	@Column
	public int todayHarvest;

	/**
	 * 总收获次数
	 */
	@Column
	public int harvestCount;
	
	/**
	 * 多少次领取保底
	 */
	@Column
	public int getRewardCount;

	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		Plant plant = new Plant();
		plant.actorId = rs.getLong("actorId");
		plant.plantId = rs.getInt("plantId");
		plant.plantState = rs.getInt("plantState");
		plant.plantStartTime = rs.getInt("plantStartTime");
		plant.todayPlant = rs.getInt("todayPlant");
		plant.plantCount = rs.getInt("plantCount");
		plant.todayHarvest = rs.getInt("todayHarvest");
		plant.harvestCount = rs.getInt("harvestCount");
		plant.getRewardCount = rs.getInt("getRewardCount");
		return plant;
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<Object>();
		if(containsPK){
			value.add(actorId);
		}
		value.add(plantId);
		value.add(plantState);
		value.add(plantStartTime);
		value.add(todayPlant);
		value.add(plantCount);
		value.add(todayHarvest);
		value.add(harvestCount);
		value.add(getRewardCount);
		return value;
	}

	@Override
	protected void hasReadEvent() {
		
	}
	
	@Override
	protected void beforeWritingEvent() {

	}

	public static Plant valueOf(long actorId) {
		Plant plant = new Plant();
		plant.actorId = actorId;
		plant.plantId = 0;
		plant.plantState = 0;
		plant.plantStartTime = 0;
		plant.todayHarvest = 0;
		plant.plantCount = 0;
		plant.todayHarvest = 0;
		plant.harvestCount = 0;
		int startCount = PlantService.getPlantGlobalConfig().startCount;
		int endCount = PlantService.getPlantGlobalConfig().endCount;
		plant.getRewardCount = RandomUtils.nextInt(startCount, endCount);
		return plant;
	}

	/**
	 * 加速
	 */
	public void quicken(PlantConfig plantConfig) {
		this.plantState = PlantType.HARVECT.getType();
		//this.plantStartTime = TimeUtils.getNow() - plantConfig.growTime;
	}

	/**
	 * 收获
	 */
	public void harvest() {
		this.plantId = 0;
		//this.plantStartTime = 0;
		this.plantState = PlantType.NOT_PLANT.getType();
		this.todayHarvest += 1;
		this.harvestCount += 1;
	}

	/**
	 * 种植
	 */
	public void plant(PlantConfig config) {
		this.plantId = config.plantId;
		this.plantStartTime = TimeUtils.getNow();
		this.plantState = PlantType.GROW.getType();
		this.todayPlant += 1;
		this.plantCount += 1;
	}

	public void reSet() {
		this.todayHarvest = 0;
		this.todayPlant = 0;
		int startCount = PlantService.getPlantGlobalConfig().startCount;
		int endCount = PlantService.getPlantGlobalConfig().endCount;
		this.getRewardCount = RandomUtils.nextInt(startCount, endCount);
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}

}
