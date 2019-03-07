package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.EquipComposeConfig;

/**
 * 装备合成配置服务类
 * @author 0x737263
 *
 */
@Component
public class EquipComposeService extends ServiceAdapter {

	/**
	 * key:equiptype value:{key:composeStar value:EquipComposeConfig}
	 */
	private static Map<Integer,Map<Integer,EquipComposeConfig>> EQUIP_COMPOSE_MAPS = new HashMap<>();
	
	@Override
	public void clear() {
		EQUIP_COMPOSE_MAPS.clear();
	}
	
	@Override
	public void initialize() {
		List<EquipComposeConfig> list = dataConfig.listAll(this, EquipComposeConfig.class);
		for (EquipComposeConfig config : list) {
			Map<Integer, EquipComposeConfig> itemMap = EQUIP_COMPOSE_MAPS.get(config.getType());
			if (itemMap == null) {
				itemMap = new HashMap<>();
			}
			itemMap.put(config.getComposeStar(), config);
			EQUIP_COMPOSE_MAPS.put(config.getType(), itemMap);
		}
	}
	
	/**
	 * 获取一条装备合成配置
	 * @param equipType		装备类型
	 * @param composeStar	合成星级
	 * @return
	 */
	public static EquipComposeConfig get(int equipType, int composeStar) {
		Map<Integer, EquipComposeConfig> maps = EQUIP_COMPOSE_MAPS.get(equipType);
		if (maps == null) {
			return null;
		}
		return maps.get(composeStar);
	}
	
	public static int getRequireNum(int equipType, int composeStar) {
		EquipComposeConfig config = get(equipType, composeStar);
		if (config == null) {
			return 1;
		}
		return config.getRequireEquipNum();
	}
	
	/**
	 * 随机装备id
	 * @param equipType		装备类型
	 * @param composeStar	合成的星级
	 * @param useTicket		是否使用点券
	 * @return
	 */
	public static int randEquip(int equipType, int composeStar, boolean useTicket) {
		EquipComposeConfig config = get(equipType, composeStar);
		if (config == null) {
			return 0;
		}

		int randMax = config.getBasePercent();
		boolean isHit = false;
		if (useTicket) {
			randMax = config.getUseTicketPercent();
			isHit =  true;
		} else {
			isHit = RandomUtils.is100Hit(randMax);
		}

		if (isHit == false) {
			return 0;// 未命中.
		}

		Map<Integer, Integer> equipMaps = config.getEquipMaps();
		Integer id = RandomUtils.randomHit(100, equipMaps);
		if(id == null){
			return 0;
		}
		return id;
	}
	
}
