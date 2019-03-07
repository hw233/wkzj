package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.VipConfig;
@Component
public class VipService extends ServiceAdapter {

	private static List<VipConfig> vipConfigList = new ArrayList<>();
	private static int maxLevel;
	
	@Override
	public void clear() {
		vipConfigList.clear();
	}
	
	@Override
	public void initialize() {
		List<VipConfig> list = dataConfig.listAll(this, VipConfig.class);
		VipConfig[] configArr = new VipConfig[list.size()];
		list.toArray(configArr);
		Arrays.sort(configArr);
		for (VipConfig vipConfig : configArr) {
			vipConfigList.add(vipConfig);
			if (vipConfig.vipLevel >= maxLevel){
				maxLevel = vipConfig.vipLevel;
			}
		}
	}
	
	public static VipConfig get(int ticketNum){
		for (int i = vipConfigList.size() - 1; i >= 0; i--) {
			VipConfig vipConfig = vipConfigList.get(i);
			if (vipConfig.rechargeTicket <= ticketNum){
				return vipConfig;
			}
		}
		
		return null;
	}
	
	public static VipConfig getByLevel(int level){
		for (VipConfig vipConfig : vipConfigList) {
			if (vipConfig.vipLevel == level){
				return vipConfig;
			}
		}
		return null;
	}

	public static int maxLevel() {
		return maxLevel;
	}
	/**
	 * 赠送vip装备列表
	 * @param vipLevel
	 * @return
	 */
	public static Set<Integer> getVipEquip(int vipLevel){
		Set<Integer> list = new HashSet<>();
		for (int i = 0; i < vipLevel; i++) {
			VipConfig cfg = vipConfigList.get(i);
			if (!cfg.getGiveEquipList().isEmpty()) {
				list.addAll(cfg.getGiveEquipList());
			}
		}
		
		return list;
	}
	/**
	 * 赠送vip英雄列表
	 * @param vipLevel
	 * @return
	 */
	public static Set<Integer> getVipHeros(int vipLevel){
		Set<Integer> list = new HashSet<>();
		for (int i = 0; i < vipLevel; i++) {
			VipConfig cfg = vipConfigList.get(i);
			if (!cfg.getGiveHerosList().isEmpty()) {
				list.addAll(cfg.getGiveHerosList());
			}
		}
		return list;
	}
	
	public static List<Map<Integer, Integer>> getVipGoods(int vipLevel) {
		List<Map<Integer, Integer>> list = new ArrayList<>();
		for (int i = 0; i < vipLevel; i++) {
			VipConfig cfg = vipConfigList.get(i);
			if (!cfg.getGiveGoodsMap().isEmpty()) {
				list.add(cfg.getGiveGoodsMap());
			}
		}
		return list;
	}

}
