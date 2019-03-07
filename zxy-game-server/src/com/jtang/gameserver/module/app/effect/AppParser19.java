package com.jtang.gameserver.module.app.effect;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.type.EffectId;

@Component
public class AppParser19 extends AppParser{

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_19;
	}

	@Override
	public void onApplicationEvent() {
		
	}

	@Override
	public void onGameEvent(GameEvent paramEvent, long appId) {
		
	}

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId, long appId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppRecordVO getAppRecord(long actorId, long appId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId,
			Map<String, String> paramsMaps, long appId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onActorLogin(long actorId, long appId) {
		
	}

}
