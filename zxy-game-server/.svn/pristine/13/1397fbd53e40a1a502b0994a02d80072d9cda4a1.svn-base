package com.jtang.gameserver.module.monster.facade.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.BattleConfig;
import com.jtang.gameserver.dataconfig.model.MonsterConfig;
import com.jtang.gameserver.dataconfig.service.MonsterService;
import com.jtang.gameserver.dataconfig.service.StoryService;
import com.jtang.gameserver.module.monster.facade.StoryMonsterFacade;

@Component
public class StoryMonsterFacadeImpl implements StoryMonsterFacade {
	protected final Log LOGGER = LogFactory.getLog(StoryMonsterFacadeImpl.class);
	
	/**
	 * 副本怪物列表
	 * <pre>
	 * 格式是:
	 * Map<BattleId, Map<GridIndex, MonsterVO>>
	 * BattleId是战场/副本的id
	 * GridIndex是怪物在阵型中的位置
	 * </pre>
	 */
	private static Map<Integer,Map<Integer, MonsterVO>> REPLICA_MONSTER_MAPS = new HashMap<Integer,Map<Integer,MonsterVO>>();
		
	
	@Override
	public Map<Integer, MonsterVO> getMonsters(int battleId) {
		return REPLICA_MONSTER_MAPS.get(battleId);
	}

	@PostConstruct
	public void init() {
		Collection<BattleConfig> battleList = StoryService.getAllBattle();
		for (BattleConfig battle : battleList) {
			int battleId = battle.getBattleId();
			Map<Integer, MonsterVO> monsterMap = new HashMap<>();
			
			Map<Integer, Integer> list = battle.getMonsterList();
			for (Entry<Integer, Integer> entry : list.entrySet()) {
				int gridIndex = entry.getKey();
				int monsterCfgId = entry.getValue();
				MonsterConfig monsterConf = MonsterService.get(monsterCfgId);
				Assert.isTrue(monsterConf != null, "怪物配置无法找到,id:" + monsterCfgId);
				MonsterVO monsterVO = new MonsterVO(monsterConf);
				monsterMap.put(gridIndex, monsterVO);
			}
			
			REPLICA_MONSTER_MAPS.put(battleId, monsterMap);
		}		
	}

}
