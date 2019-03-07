package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.LineupUnlockConfig;

@Component
public class LineupService extends ServiceAdapter{
	private static Map<Integer, LineupUnlockConfig> UNLOCK_MAP = new HashMap<Integer, LineupUnlockConfig>();
	
	@Override
	public void clear() {
		UNLOCK_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<LineupUnlockConfig> list = this.dataConfig.listAll(this, LineupUnlockConfig.class);
		for (LineupUnlockConfig item : list) {
			UNLOCK_MAP.put(item.getIndex(), item);
		}
	}
	
	public static LineupUnlockConfig get(int index) {
		return UNLOCK_MAP.get(index);
	}
	// TODO 补救丢数据的方法 需要删掉
	public static int gridNumByLevel(int level) {
		int num = 0;
		for (LineupUnlockConfig lineupUnlockConfig : UNLOCK_MAP.values()) {
			if (level >= lineupUnlockConfig.getNeedActorLevel() && lineupUnlockConfig.isAutoUnlock()) {
				num += 1;
			}
		}
		
		return num;
	}
	
}
