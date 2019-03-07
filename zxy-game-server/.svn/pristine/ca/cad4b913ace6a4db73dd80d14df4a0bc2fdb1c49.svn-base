package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.DemonMonsterConfig;

/**
 * 集众降魔boss配置服务
 * @author ludd
 *
 */
@Component
public class DemonMonsterService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemonMonsterService.class);
	
	private static Map<Integer, DemonMonsterConfig> MAP = new HashMap<Integer, DemonMonsterConfig>(); 
	@Override
	public void clear() {
		MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<DemonMonsterConfig> list = dataConfig.listAll(this, DemonMonsterConfig.class);
		for (DemonMonsterConfig demonMonsterConfig : list) {
			MAP.put(demonMonsterConfig.getId(), demonMonsterConfig);
		}
	}
	
	public static DemonMonsterConfig get(int id) {
		if (MAP.containsKey(id)) {
			return MAP.get(id);
		}
		LOGGER.error(String.format("不存在DemonMonsterConfig， id:[%s]", id));
		return null;
	}
	
//	public static Map<Integer, MonsterVO> getMonster(int configId, int totalLevel) {
//		DemonMonsterConfig cfg = get(configId);
//		if (cfg == null) {
//			return null;
//		}
//		Map<Integer, Integer> monsterPossition = cfg.getMonsterList();
//		Map<Integer,MonsterVO> monsters = new ConcurrentHashMap<Integer, MonsterVO>();
//		for (Map.Entry<Integer, Integer> posistion : monsterPossition.entrySet()) {
//			int monsterId = posistion.getKey();
//			MonsterConfig monsterCfg = MonsterService.get(monsterId);
//			if (monsterCfg == null) {
//				continue;
//			}
//			
//			MonsterVO mVO = MonsterService.getMonsterVO(monsterId);
//			mVO = mVO.clone();
//			mVO.setAtk(cfg.getMonsterAttack(totalLevel));
//			mVO.setDefense(cfg.getMonsterDeffends(totalLevel));
//			mVO.setHp(cfg.getMonsterHp(totalLevel));
//			mVO.setMaxHp(mVO.getHp());
//			monsters.put(posistion.getValue(), mVO);
//		}
//		return monsters;
//	}
	


}
