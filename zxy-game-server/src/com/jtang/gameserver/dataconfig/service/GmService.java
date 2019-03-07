package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.GmConfig;

@Component
public class GmService extends ServiceAdapter {

	private static List<GmConfig> GM_ACTORID_LIST = new ArrayList<>();

	@Override
	public void clear() {
		GM_ACTORID_LIST.clear();
	}

	@Override
	public void initialize() {
		GM_ACTORID_LIST.addAll(dataConfig.listAll(this, GmConfig.class));
	}

	public static boolean isGm(long actorId) {
		for (GmConfig gmConfig : GM_ACTORID_LIST) {
			if (gmConfig.fromActorId <= actorId && actorId <= gmConfig.endActorId) {
				return true;
			}
		}
		return false;
	}

}
