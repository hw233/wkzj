package com.jtang.gameserver.module.adventures.favor.effect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.FavorTriggerConfig;
import com.jtang.gameserver.dataconfig.service.FavorTriggerService;
import com.jtang.gameserver.module.adventures.favor.type.FavorTriggerType;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.type.BattleType;
@Component
public class FavorTrigger1 extends FavorTrigger {

	@Autowired
	private BattleFacade battleFacade;
	@Override
	public int getTriggerType() {
		return FavorTriggerType.TYPE1.getType();
	}

	@Override
	public boolean isTrigger(long actorId) {
		FavorTriggerConfig config = FavorTriggerService.get(getTriggerType());
		int times = battleFacade.getBatteTotalNum(actorId, BattleType.SNATCH);
		int needTimes = Integer.valueOf(config.value);
		if (times == needTimes){
			return true;
		}
		return false;
	}

}
