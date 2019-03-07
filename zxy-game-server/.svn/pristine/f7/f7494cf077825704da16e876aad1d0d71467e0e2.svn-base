package com.jtang.gameserver.module.adventures.favor.effect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.FavorTriggerConfig;
import com.jtang.gameserver.dataconfig.service.FavorTriggerService;
import com.jtang.gameserver.module.adventures.bable.facade.BableFacade;
import com.jtang.gameserver.module.adventures.favor.type.FavorTriggerType;
@Component
public class FavorTrigger2 extends FavorTrigger {

	@Autowired
	private BableFacade bableFacade;
	@Override
	public int getTriggerType() {
		return FavorTriggerType.TYPE2.getType();
	}

	@Override
	public boolean isTrigger(long actorId) {
		FavorTriggerConfig config = FavorTriggerService.get(getTriggerType());
		int hasTimes = bableFacade.getHasEnterTimes(actorId);
		int needTimes = Integer.valueOf(config.value);
		if (hasTimes == needTimes){
			return true;
		}
		return false;
	}

}
