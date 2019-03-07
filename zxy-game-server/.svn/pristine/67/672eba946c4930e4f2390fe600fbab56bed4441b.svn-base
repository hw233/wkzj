package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.dataconfig.model.OnlineGiftsConfig;

@Component
public class OnlineGiftsService extends ServiceAdapter {

	
	private static final Map<Integer, OnlineGiftsConfig> ONLINE_GIFT_MAP = new HashMap<Integer, OnlineGiftsConfig>();
	@Override
	public void clear() {
		ONLINE_GIFT_MAP.clear();
	}

	@Override
	public void initialize() {
		List<OnlineGiftsConfig> list = this.dataConfig.listAll(this, OnlineGiftsConfig.class);
		for (OnlineGiftsConfig conf : list) {
			ONLINE_GIFT_MAP.put(conf.index, conf);
		}
	}
	
	public static int getCoolDownTime(int index) {
		if (ONLINE_GIFT_MAP.containsKey(index)) {
			return ONLINE_GIFT_MAP.get(index).time;
		}
		return -1;
	}
	
	public static List<RewardObject> getRewards(int index) {
		if (ONLINE_GIFT_MAP.containsKey(index)) {
			return ONLINE_GIFT_MAP.get(index).rewardsList;
		}
		return null;
	}
	
	public static int getMaxIndex() {
		return ONLINE_GIFT_MAP.size();
	}
	

}
