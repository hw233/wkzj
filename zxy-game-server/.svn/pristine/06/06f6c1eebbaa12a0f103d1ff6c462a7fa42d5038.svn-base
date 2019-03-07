package com.jtang.gameserver.module.monster.facade.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.LoveMonsterConfig;
import com.jtang.gameserver.dataconfig.model.MonsterConfig;
import com.jtang.gameserver.dataconfig.service.LoveService;
import com.jtang.gameserver.dataconfig.service.MonsterService;
import com.jtang.gameserver.module.monster.facade.LoveDemonMonsterFacade;

@Component
public class LoveDemonMonsterFacadeImpl implements LoveDemonMonsterFacade {

	@Override
	public Map<Integer, MonsterVO> getMonster(int configId, int totalLevel) {
		LoveMonsterConfig cfg = LoveService.getLoveMonsterConfig(configId);
		if (cfg == null) {
			return null;
		}
		Map<Integer, Integer> monsterPossition = cfg.getMonsterList();
		Map<Integer,MonsterVO> monsters = new ConcurrentHashMap<Integer, MonsterVO>();
		for (Map.Entry<Integer, Integer> posistion : monsterPossition.entrySet()) {
			int monsterId = posistion.getValue();
			MonsterConfig monsterCfg = MonsterService.get(monsterId);
			if (monsterCfg == null) {
				continue;
			}
			
			MonsterVO mVO = new MonsterVO(monsterCfg);
			mVO = mVO.clone();
			mVO.setAtk(cfg.getMonsterAttack(totalLevel,monsterId));
			mVO.setDefense(cfg.getMonsterDeffends(totalLevel,monsterId));
			mVO.setHp(cfg.getMonsterHp(totalLevel,monsterId));
			mVO.setMaxHp(mVO.getHp());
			monsters.put(posistion.getKey(), mVO);
		}
		return monsters;
	}

}
