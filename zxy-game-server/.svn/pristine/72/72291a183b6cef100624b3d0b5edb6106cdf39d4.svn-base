package com.jtang.gameserver.module.monster.facade.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.jtang.core.context.SpringContext;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.DemonMonsterConfig;
import com.jtang.gameserver.dataconfig.model.MonsterConfig;
import com.jtang.gameserver.dataconfig.service.DemonMonsterService;
import com.jtang.gameserver.dataconfig.service.MonsterService;
import com.jtang.gameserver.module.monster.facade.DemonMonsterFacade;
@Component
public class DemonMonsterFacadeImpl implements DemonMonsterFacade {
	
	@Override
	public Map<Integer, MonsterVO> getMonster(int configId, int totalLevel) {
		DemonMonsterConfig cfg = DemonMonsterService.get(configId);
		if (cfg == null) {
			return null;
		}
		Map<Integer, Integer> monsterPossition = cfg.getMonsterList();
		Map<Integer,MonsterVO> monsters = new ConcurrentHashMap<Integer, MonsterVO>();
		for (Map.Entry<Integer, Integer> posistion : monsterPossition.entrySet()) {
			int monsterId = posistion.getKey();
			MonsterConfig monsterCfg = MonsterService.get(monsterId);
			if (monsterCfg == null) {
				continue;
			}
			
			MonsterVO mVO = new MonsterVO(monsterCfg);
			mVO = mVO.clone();
			mVO.setAtk(cfg.getMonsterAttack(totalLevel));
			mVO.setDefense(cfg.getMonsterDeffends(totalLevel));
			mVO.setHp(cfg.getMonsterHp(totalLevel));
			mVO.setMaxHp(mVO.getHp());
			monsters.put(posistion.getValue(), mVO);
		}
		return monsters;
	}
	
	public static void main(String[] args) {
		DemonMonsterFacadeImpl facade = (DemonMonsterFacadeImpl) SpringContext.getBean(DemonMonsterFacadeImpl.class);
		Map<Integer, MonsterVO> m = facade.getMonster(1, 100);
		for (Map.Entry<Integer, MonsterVO> v : m.entrySet()) {
			System.out.println(v.getKey());
			System.out.println(v.getValue().getHeroId());
		}
	}

}
