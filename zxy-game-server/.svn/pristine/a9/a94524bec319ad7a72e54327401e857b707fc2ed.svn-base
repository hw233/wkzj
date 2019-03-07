package com.jtang.gameserver.dataconfig.service;

import static com.jiatang.common.GameStatusCodeConstant.EQUIP_COMPOSE_STAR_DIFFERENT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.EquipUpgradeConfig;

/**
 * 装备配置服务类
 * @author 0x737263
 *
 */
@Component
public class EquipService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipService.class);
	
	private static Map<Integer, EquipConfig> EQUIP_CFG_LIST = new HashMap<>(); 
	private static Map<String, EquipUpgradeConfig> EQUIP_UP_CONFIG = new HashMap<String, EquipUpgradeConfig>();
	
	@Override
	public void clear() {
		EQUIP_CFG_LIST.clear();
		EQUIP_UP_CONFIG.clear();
	}
	
	@Override
	public void initialize() {
		List<EquipConfig> list = dataConfig.listAll(this, EquipConfig.class);
		for (EquipConfig equipConfig : list) {
			EQUIP_CFG_LIST.put(equipConfig.getEquipId(), equipConfig);
		}
		List<EquipUpgradeConfig> upList = dataConfig.listAll(this, EquipUpgradeConfig.class);
		for(EquipUpgradeConfig upConfig : upList){
			EQUIP_UP_CONFIG.put(getKey(upConfig.getStar(), upConfig.getType()), upConfig);
		}
	}

	/**
	 * 获取装备配置
	 * @param equipId
	 * @return
	 */
	public static EquipConfig get(int equipId) {
		if (EQUIP_CFG_LIST.containsKey(equipId)) {
			return EQUIP_CFG_LIST.get(equipId);
		}
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(String.format("EquipConfig缺少配置，equipId: [%s]", equipId));
		}
		return null;
	}
	/**
	 * 根据装备品质获取升级配置
	 * @param star 装备品质
	 * @param type 装备类型
	 * @return 如果配置中不存在该装备的配置，返回null
	 */
	public static EquipUpgradeConfig getUpgradeConfig(int star, int type){
		EquipUpgradeConfig upgradeConfig = EQUIP_UP_CONFIG.get(getKey(star, type));
		if(upgradeConfig == null)
			LOGGER.error(String.format("EquipUpgradeConfig缺少配置，star: [%s] type: [%s]", star, type));
		return upgradeConfig;
	}
	
	private static String getKey(int star, int type){
		return star + "_" + type;
	}
	/**
	 * 获取相同星级相同类型的装备
	 * @param star
	 * @param type
	 * @return
	 */
	public static List<EquipConfig> getEquipList(int star, int type){
		List<EquipConfig> list = new ArrayList<>();
		for (Map.Entry<Integer, EquipConfig> entry : EQUIP_CFG_LIST.entrySet()) {
			if(entry.getValue().getStar() == star && entry.getValue().getType() == type){
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	/**
	 * 获取装备相同的星级
	 * @param equipIdList
	 * @return
	 */
	public static TResult<Integer> getStar(List<Integer> equipIdList) {
		int lastStar = 0;
		for(Integer equipId : equipIdList) {
			EquipConfig config = get(equipId);
			if(config == null) {
				return TResult.valueOf(EQUIP_COMPOSE_STAR_DIFFERENT);
			}
			if(lastStar == 0) {
				lastStar = config.getStar();
				continue;
			}
			if(lastStar != config.getStar()) {
				return TResult.valueOf(EQUIP_COMPOSE_STAR_DIFFERENT);
			}
		}
		return TResult.sucess(lastStar);
	}
	
	/**
	 * 获取装备对应的碎片id
	 * @param equipId
	 * @return
	 */
	public static int getFragmentIdByEquipId(int equipId) {
		if (EQUIP_CFG_LIST.containsKey(equipId)) {
			return EQUIP_CFG_LIST.get(equipId).fragmentId;
		}
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(String.format("EquipConfig缺少碎片Id配置，equipId: [%s]", equipId));
		}
		return -1;
	}
	/**
	 * 获取所有装备配置
	 * @return
	 */
	public static Collection<EquipConfig> getAllEquipConfigs() {
		return EQUIP_CFG_LIST.values();
	}
	
	/**
	 * 获取所有X星以上的装备碎片id
	 */
	public static List<Integer> getAllFragmentByStar(int star){
		List<Integer> list = new ArrayList<>();
		for(EquipConfig equipConfig : EQUIP_CFG_LIST.values()){
			if(equipConfig.getStar() >= star){
				list.add(equipConfig.fragmentId);
			}
		}
		return list;
	}
}
