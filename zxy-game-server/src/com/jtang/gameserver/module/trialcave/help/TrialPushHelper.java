package com.jtang.gameserver.module.trialcave.help;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.trialcave.handler.TrialCaveCmd;
import com.jtang.gameserver.module.trialcave.handler.response.TrialBattleResultResponse;
import com.jtang.gameserver.module.trialcave.handler.response.TrialCaveResetResponse;
import com.jtang.gameserver.module.trialcave.model.TrialCaveInfoVO;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 试炼洞模块推送类
 * @author lig
 *
 */
@Component
public class TrialPushHelper {
	protected static final Logger LOGGER = LoggerFactory.getLogger(TrialPushHelper.class);
	
	@Autowired
	PlayerSession playerSession;
	
	private static ObjectReference<TrialPushHelper> REF = new ObjectReference<TrialPushHelper>();
	
	@PostConstruct
	protected void init() {
		REF.set(this);
	}
	
	private static TrialPushHelper getInstance() {
		return REF.get();
	}	
	
	/**
	 * 推送试炼战斗结果
	 * @param actorId
	 * @param fightData
	 * @param battleStar
	 * @param actorAttribute
	 * @param uuidGoods
	 */
	public static void pushBattleResult(long actorId, FightData fightData, List<RewardObject> rewards, TrialCaveInfoVO entranceVO) {
		TrialBattleResultResponse res = new TrialBattleResultResponse(fightData, rewards, entranceVO);
		Response rsp = Response.valueOf(ModuleName.TRIAL_CAVE, TrialCaveCmd.TRIAL_BATTLE, res.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	/**
	 * 推送整点重置结果
	 * @param actorId
	 */
	public static void pushTrialedCount(long actorId, int resetCount) {
		TrialCaveResetResponse response = new TrialCaveResetResponse(resetCount);
		Response rsp = Response.valueOf(ModuleName.TRIAL_CAVE, TrialCaveCmd.TRIAL_CAVE_RESET, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
}
