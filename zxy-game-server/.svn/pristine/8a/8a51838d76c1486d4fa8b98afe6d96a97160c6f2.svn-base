package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.MonsterConfig;

/**
 * 怪物配置服务读取类
 * @author 0x737263
 *
 */
@Component
public class MonsterService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonsterService.class);

	private static Map<Integer, MonsterConfig> MONSTER_CONFIG_MAPS = new HashMap<Integer, MonsterConfig>();
	
//	private static Map<Integer, MonsterVO> monsters = new HashMap<>();

	@Override
	public void clear() {
		MONSTER_CONFIG_MAPS.clear();
//		monsters.clear();
	}
	
	@Override
	public void initialize() {
		List<MonsterConfig> list = dataConfig.listAll(this, MonsterConfig.class);	
		for (MonsterConfig monster : list) {
			MONSTER_CONFIG_MAPS.put(monster.getMonsterId(), monster);
//			MonsterVO monsterVO = new MonsterVO(monster);
//			monsters.put(monsterVO.getHeroId(), monsterVO);
		}
	}
	
	/**
	 * 获取Hero
	 * @param heroId 仙人Id
	 * @return
	 */
	public static MonsterConfig get(int monsterId) {
		if (MONSTER_CONFIG_MAPS.containsKey(monsterId)) {
			return MONSTER_CONFIG_MAPS.get(monsterId);
		} else {
			LOGGER.error("怪物配置无法找到:" + monsterId);
		}
		return null;
	}
//	public static MonsterVO getMonsterVO(int monsterId) {
//		if (monsters.containsKey(monsterId)) {
//			return monsters.get(monsterId);
//		} else {
//			LOGGER.error("怪物无法找到:" + monsterId);
//		}
//		return null;
//	}
}
