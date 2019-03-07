package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.HoleBattleConfig;
import com.jtang.gameserver.dataconfig.model.HoleConfig;
import com.jtang.gameserver.dataconfig.model.HoleProportionConfig;

@Component
public class HoleService extends ServiceAdapter {

	private static Map<Integer, HoleConfig> HOLE_MAP = new HashMap<Integer, HoleConfig>();
	
	private static Map<Integer, HoleBattleConfig> HOLE_BATTLE_MAP = new HashMap<Integer, HoleBattleConfig>();
	
	private static Map<Integer, HoleProportionConfig> HOLE_PROPORTION_MAP = new HashMap<Integer, HoleProportionConfig>();

	@Override
	public void clear() {
		HOLE_MAP.clear();
		HOLE_BATTLE_MAP.clear();
		HOLE_PROPORTION_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<HoleConfig> holes = this.dataConfig.listAll(this, HoleConfig.class);
		for(HoleConfig holeConfig:holes){
			HOLE_MAP.put(holeConfig.holeId, holeConfig);
		}
		
		List<HoleBattleConfig> holeBattles = this.dataConfig.listAll(this,HoleBattleConfig.class);
		for (HoleBattleConfig holeBattleConfig : holeBattles) {
			HOLE_BATTLE_MAP.put(holeBattleConfig.holeBattleId, holeBattleConfig);
		}
		
		List<HoleProportionConfig> holeProportion = this.dataConfig.listAll(this,HoleProportionConfig.class);
		for(HoleProportionConfig config:holeProportion){
			HOLE_PROPORTION_MAP.put(config.storyBattleId, config);
		}
	}
	
	public static HoleConfig get(int holeId){
		return HOLE_MAP.get(holeId);
	}
	
	/**
	 * 获得洞府配置
	 * @param holeId
	 * @return
	 */
	public static HoleConfig getHole(int holeId){
		return HOLE_MAP.get(holeId);
	}

	public static HoleBattleConfig getHoleBattle(int battleId) {
		return HOLE_BATTLE_MAP.get(battleId);
	}
	
	public static HoleProportionConfig getHoleByBattleId(int battleId) {
		return HOLE_PROPORTION_MAP.get(battleId);
	}
	
	public static int getHoleButtleNum(int holeId){
		HoleConfig holeConfig = getHole(holeId);
		if(holeConfig == null){
			return 0;
		}
		return holeConfig.getHoleBattleNum();
	}
	
}
