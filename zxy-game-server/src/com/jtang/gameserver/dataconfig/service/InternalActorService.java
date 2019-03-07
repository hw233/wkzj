package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.InternalActorConfig;

@Component
public class InternalActorService extends ServiceAdapter {
	
	private static List<Long> INTERNAL_ACTOR_LIST = new ArrayList<Long>();

	@Override
	public void clear() {
		INTERNAL_ACTOR_LIST.clear();
	}

	@Override
	public void initialize() {
		List<InternalActorConfig> internalActorConfigs = dataConfig.listAll(this, InternalActorConfig.class);
		for(InternalActorConfig config : internalActorConfigs){
			INTERNAL_ACTOR_LIST.add(config.actorId);
		}
	}
	
	public static boolean isInternalActor(long actorId) {
		return INTERNAL_ACTOR_LIST.contains(actorId);
	}
	
	public static boolean tryGet(long actorId) {
		int index = RandomUtils.nextIntIndex(INTERNAL_ACTOR_LIST.size());
		if (index < 0 || index >= INTERNAL_ACTOR_LIST.size()) {
			return false;
		}
		long who = INTERNAL_ACTOR_LIST.get(index);
		if (who == actorId) {
			return true;
		}
		return false;
	}

}
