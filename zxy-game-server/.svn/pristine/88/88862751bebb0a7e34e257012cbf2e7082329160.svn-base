package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.GiveEquipRewardConfig;

@Component
public class GiveEquipRewardService extends ServiceAdapter {
	
	private static List<GiveEquipRewardConfig> treasureList = new ArrayList<GiveEquipRewardConfig>();

	@Override
	public void clear() {
		treasureList.clear();
	}

	@Override
	public void initialize() {
		treasureList.addAll(dataConfig.listAll(this,GiveEquipRewardConfig.class));
	}
	
	public static GiveEquipRewardConfig random(){
		int random = RandomUtils.nextIntIndex(1000);
		int proportion = 0;
		for(GiveEquipRewardConfig reward:treasureList){
			proportion+=reward.proportion;
			if(random<=proportion){
				return reward;
			}
		}
		return null;
	}

}
