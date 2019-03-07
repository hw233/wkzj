package com.jtang.gameserver.module.extapp.welkin.module;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dataconfig.model.WelkinConfig;
import com.jtang.gameserver.dataconfig.service.WelkinService;
import com.jtang.gameserver.dbproxy.entity.Welkin;

public class WelkinVO extends IoBufferSerializer {

	/**
	 * 已探物次数
	 */
	public int count;
	
	/**
	 * 得到的奖励
	 */
	public List<RewardObject> reward = new ArrayList<>();
	
	/**
	 * 所有奖励
	 */
	public List<RewardObject> allReward = new ArrayList<>();
	
	/**
	 * 获得奖励在所有奖励的下标
	 */
	public int index;
	
	/**
	 * 目前位置(南天门、前殿、宝殿)
	 */
	public int place;
	
	/**
	 * 每次消耗点券
	 */
	public int costTicket;
	
	
	public static WelkinVO valueOf(Welkin welkin,int level) {
		WelkinVO welkinVO = new WelkinVO();
		welkinVO.count = welkin.useCount + welkin.ticketUseCount;
		WelkinConfig config = WelkinService.getWelkinConfig(welkinVO.count+1);
		welkinVO.place = config.id;
		welkinVO.costTicket = config.costTicket;
		welkinVO.allReward = WelkinService.getAllReward(welkinVO.place, level);
		return welkinVO;
	}
	
	@Override
	public void write() {
		writeInt(count);
		writeShort((short) reward.size());
		for(RewardObject rewardObject:reward){
			writeBytes(rewardObject.getBytes());
		}
		writeShort((short) allReward.size());
		for(RewardObject rewardObject:allReward){
			writeBytes(rewardObject.getBytes());
		}
		writeInt(index);
		writeInt(place);
		writeInt(costTicket);
	}

}
