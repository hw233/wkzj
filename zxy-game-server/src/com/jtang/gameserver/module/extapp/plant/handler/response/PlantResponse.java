package com.jtang.gameserver.module.extapp.plant.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dataconfig.service.PlantService;
import com.jtang.gameserver.module.extapp.plant.module.PlantVO;

public class PlantResponse extends IoBufferSerializer {
	
	/**
	 * 种植vo
	 */
	public PlantVO plantVO;
	
	/**
	 * 种植固定奖励
	 * @param plantVO
	 */
	public List<RewardObject> reward = new ArrayList<RewardObject>();
	
	/**
	 * 种植所有额外奖励
	 */
	public List<RewardObject> extReward = new ArrayList<RewardObject>();
	
	/**
	 * 活动开始时间
	 */
	public int startTime;
	
	/**
	 * 活动结束时间
	 */
	public int endTime;
	
	/**
	 * 每分钟消耗多少点券
	 */
	public String costTicket;
	
	public PlantResponse(PlantVO plantVO,List<RewardObject> reward,List<RewardObject> extReward,String costTicket){
		this.plantVO = plantVO;
		this.reward = reward;
		this.extReward = extReward;
		this.startTime = PlantService.getPlantGlobalConfig().start;
		this.endTime = PlantService.getPlantGlobalConfig().end;
		this.costTicket = costTicket;
	}
	
	@Override
	public void write() {
		writeBytes(plantVO.getBytes());
		writeShort((short) reward.size());
		for(RewardObject rewardObject:reward){
			writeBytes(rewardObject.getBytes());
		}
		writeShort((short) extReward.size());
		for(RewardObject rewardObject:extReward){
			writeBytes(rewardObject.getBytes());
		}
		writeInt(startTime);
		writeInt(endTime);
		writeString(costTicket);
	}
}
