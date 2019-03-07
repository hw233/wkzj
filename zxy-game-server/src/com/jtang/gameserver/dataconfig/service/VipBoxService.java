package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.VipBoxConfig;
import com.jtang.gameserver.dataconfig.model.VipBoxControlConfig;
import com.jtang.gameserver.dataconfig.model.VipBoxOpenConfig;
import com.jtang.gameserver.dataconfig.model.VipBoxRewardConfig;
import com.jtang.gameserver.dataconfig.model.VipBoxTimeConfig;

@Component
public class VipBoxService extends ServiceAdapter {

	private static Map<Integer,VipBoxConfig> VIP_BOX_CONFIG = new HashMap<>();
	
	private static Map<Integer,VipBoxOpenConfig> VIP_BOX_OPEN_CONFIG = new HashMap<>();
	
	private static Map<Integer,Map<Integer,VipBoxRewardConfig>> VIP_BOX_REWARD_CONFIG = new HashMap<>();
	
	private static VipBoxTimeConfig VIP_BOX_TIME_CONFIG;
	
	private static Map<Integer,VipBoxControlConfig> VIP_BOX_CONTROL_CONFIG = new HashMap<>();
	
	@Override
	public void clear() {
		VIP_BOX_CONFIG.clear();
		VIP_BOX_OPEN_CONFIG.clear();
		VIP_BOX_REWARD_CONFIG.clear();
		VIP_BOX_CONTROL_CONFIG.clear();
	}

	@Override
	public void initialize() {
		List<VipBoxConfig> boxConfigList = dataConfig.listAll(this, VipBoxConfig.class);
		for(VipBoxConfig vipBoxConfig:boxConfigList){
			VIP_BOX_CONFIG.put(vipBoxConfig.vipLevel, vipBoxConfig);
		}
		
		List<VipBoxOpenConfig> boxOpenList= dataConfig.listAll(this, VipBoxOpenConfig.class);
		for(VipBoxOpenConfig vipBoxOpenConfig:boxOpenList){
			VIP_BOX_OPEN_CONFIG.put(vipBoxOpenConfig.count, vipBoxOpenConfig);
		}
		
		List<VipBoxRewardConfig> boxRewardList = dataConfig.listAll(this, VipBoxRewardConfig.class);
		for(VipBoxRewardConfig vipBoxRewardConfig : boxRewardList) {
			if(VIP_BOX_REWARD_CONFIG.containsKey(vipBoxRewardConfig.openCount)){
				VIP_BOX_REWARD_CONFIG.get(vipBoxRewardConfig.openCount).put(vipBoxRewardConfig.index,vipBoxRewardConfig);
			}else{
				Map<Integer,VipBoxRewardConfig> map = new HashMap<>();
				map.put(vipBoxRewardConfig.index,vipBoxRewardConfig);
				VIP_BOX_REWARD_CONFIG.put(vipBoxRewardConfig.openCount, map);
			}
		}
		
		List<VipBoxTimeConfig> timeList = dataConfig.listAll(this, VipBoxTimeConfig.class);
		for(VipBoxTimeConfig config : timeList){
			VIP_BOX_TIME_CONFIG = config;
		}
		
		List<VipBoxControlConfig> controlList = dataConfig.listAll(this, VipBoxControlConfig.class);
		for(VipBoxControlConfig config : controlList){
			VIP_BOX_CONTROL_CONFIG.put(config.index, config);
		}
	}
	
	/**
	 * 获取当前vip等级可以领取的箱子个数
	 */
	public static int getBoxNum(int vipLevel){
		return VIP_BOX_CONFIG.get(vipLevel).boxNum;
	}
	
	/**
	 * 获取当前vip等级的箱子id
	 */
	public static int getBoxId(int vipLevel){
		return VIP_BOX_CONFIG.get(vipLevel).goodsId;
	}
	
	/**
	 * 获取当前开箱子消耗的点券数
	 */
	public static int getCostTicket(int openNum){
		VipBoxOpenConfig vipOpenConfig = VIP_BOX_OPEN_CONFIG.get(openNum);
		return vipOpenConfig.costTicket;
	}
	
	/**
	 * 获取配置开启关闭时间
	 */
	public static VipBoxTimeConfig getOpenTime(){
		return VIP_BOX_TIME_CONFIG;
	}
	
	/**
	 * 获取奖励
	 * @param openMap 
	 */
	public static List<VipBoxRewardConfig> getReward(int openNum, Map<Integer, Integer> openMap){
		VipBoxOpenConfig vipOpenConfig = VIP_BOX_OPEN_CONFIG.get(openNum);
		int rewardNum = vipOpenConfig.getRewardNum();
		Map<Integer,VipBoxRewardConfig> rewardMap = new HashMap<>(VIP_BOX_REWARD_CONFIG.get(openNum));
		for(Entry<Integer,Integer> entry : openMap.entrySet()){
			VipBoxControlConfig controlConfig = VIP_BOX_CONTROL_CONFIG.get(entry.getKey());
			for(VipBoxRewardConfig rewardConfig : VIP_BOX_REWARD_CONFIG.get(openNum).values()){
				if(rewardConfig.rewardType == controlConfig.type){
					if(controlConfig.type == 3){//装备
						EquipConfig config = EquipService.get(rewardConfig.rewardId);
						if(config != null && config.getStar() >= controlConfig.context && entry.getValue() >= controlConfig.num){
							rewardMap.remove(rewardConfig.index);
						}
					}else if(controlConfig.type == 2 || controlConfig.type == 4 || controlConfig.type == 5){//物品、魂魄、碎片
						if(controlConfig.context == rewardConfig.rewardId && entry.getValue() >= controlConfig.num){
							rewardMap.remove(rewardConfig.index);
						}
					}
				}
			}
		}
		List<VipBoxRewardConfig> rewardList = new ArrayList<>();
		for(VipBoxRewardConfig rewardConfig : rewardMap.values()){
			if(rewardConfig.isMustReward()){
				rewardList.add(rewardConfig);
			}
		}
		if(rewardList.size() >= rewardNum){//保证只出配置随机出的个数
			rewardList = rewardList.subList(0, rewardNum);
			return rewardList;
		}else{//扣除必出物品已经占用的次数,将必出物品从奖励池中移除
			rewardNum -= rewardList.size();
			for(VipBoxRewardConfig rewardConfig : rewardList){
				rewardMap.remove(rewardConfig.index);
			}
		}
		
		for (int i = 0; i < rewardNum; i++) {
			int rate = 0;
			Map<Integer,Integer> map = new HashMap<>();
			for(Entry<Integer,VipBoxRewardConfig> entry : rewardMap.entrySet()){
				map.put(entry.getKey(), entry.getValue().rewardRate);
				rate += entry.getValue().rewardRate;
			}
			Integer index = RandomUtils.randomHit(rate, map);
			VipBoxRewardConfig config = rewardMap.get(index);
			rate -= config.rewardRate;
			rewardList.add(config);
			rewardMap.remove(index);
		}
		return rewardList;
	}

	public static int getOpenMax() {
		return VIP_BOX_OPEN_CONFIG.size();
	}

	public static Collection<VipBoxControlConfig> getOpenControl() {
		return VIP_BOX_CONTROL_CONFIG.values();
	}

	public static VipBoxControlConfig getControl(Integer key) {
		return VIP_BOX_CONTROL_CONFIG.get(key);
	}

}
