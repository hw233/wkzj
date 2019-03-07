package com.jtang.gameserver.module.monster.facade;

import java.util.Map;

import com.jtang.gameserver.component.model.MonsterVO;


public interface DemonMonsterFacade {
	/**
	 * 获取集众降魔怪物列表(返回的Map的key代表的是怪物在阵型中的位置)
	 * @param difficult
	 * @return
	 */
	Map<Integer, MonsterVO> getMonster(int configId, int totalLevel);
}
