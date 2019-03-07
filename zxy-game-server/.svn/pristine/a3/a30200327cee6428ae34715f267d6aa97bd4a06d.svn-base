package com.jtang.gameserver.dataconfig.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.dataconfig.model.InviteGlobalConfig;
import com.jtang.gameserver.dataconfig.model.InviteRewardConfig;

@Component
public class InviteService extends ServiceAdapter {
	
	private static Map<Integer,InviteRewardConfig> INVITE_REWARD_MAP = new HashMap<>();
	
	private static InviteGlobalConfig INVITE_GLOBAL = new InviteGlobalConfig();

	@Override
	public void clear() {
		INVITE_REWARD_MAP.clear();
		INVITE_GLOBAL = new InviteGlobalConfig();
	}

	@Override
	public void initialize() {
		List<InviteGlobalConfig> globalList = dataConfig.listAll(this, InviteGlobalConfig.class);
		for(InviteGlobalConfig config : globalList){
			INVITE_GLOBAL = config;
		}

		List<InviteRewardConfig> rewardList = dataConfig.listAll(this, InviteRewardConfig.class);
		for(InviteRewardConfig config : rewardList){
			INVITE_REWARD_MAP.put(config.inviteLevel, config);
		}
	}
	
	public static List<RewardObject> getRewardListByInviteLevel(int beInviteLevel){
		for(InviteRewardConfig config : INVITE_REWARD_MAP.values()){
			if(config.inviteLevel == beInviteLevel){
				return config.rewardList;
			}
		}
		return null;
	}
	
	public static boolean getNeedPushByInviteLevel(int beInviteLevel){
		for(InviteRewardConfig config : INVITE_REWARD_MAP.values()){
			if(config.inviteLevel <= beInviteLevel){
				return true;
			}
		}
		return false;
	}
	
	public static Collection<InviteRewardConfig> getAllRewardConfigs(){
		return INVITE_REWARD_MAP.values();
	}

	
	public static InviteGlobalConfig getGlobalConfig(){
		return INVITE_GLOBAL;
	}
	
}
