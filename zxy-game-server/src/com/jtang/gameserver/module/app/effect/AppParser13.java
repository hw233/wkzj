package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_CLOSED;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;

/**
 * <pre>
 * 登天塔额外奖励
 * </pre>
 */
@Component
public class AppParser13 extends AppParser{

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> paramsMaps,long appId) {
		return ListResult.statusCode(APP_CLOSED);
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_13;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {

	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		return null;
	}

	@Override
	public void onApplicationEvent() {
		
	}

	@Override
	public void onActorLogin(long actorId,long appId) {
		
	}

}
