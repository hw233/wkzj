package com.jtang.gameserver.module.notice.facade.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.SnatchResultEvent;
import com.jtang.gameserver.dataconfig.model.NoticeConfig;
import com.jtang.gameserver.dataconfig.service.NoticeService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Power;
import com.jtang.gameserver.module.battle.model.AttackPlayerRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.notice.dao.NoticeDao;
import com.jtang.gameserver.module.notice.facade.NoticeFacade;
import com.jtang.gameserver.module.notice.helper.NoticePushHelper;
import com.jtang.gameserver.module.notice.model.NoticeVO;
import com.jtang.gameserver.module.notice.type.NoticeType;
import com.jtang.gameserver.module.power.facade.PowerFacade;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;

/**
 * 
 * @author pengzy
 * 
 */
@Component
public class NoticeFacadeImpl implements NoticeFacade, Receiver {

	@Autowired
	private NoticeDao noticeDao;

	@Autowired
	private ActorFacade actorFacade;

	@Autowired
	private EventBus eventBus;

	@Autowired
	private VipFacade vipFacade;

	@Autowired
	private ChatFacade chatFacade;

	@Autowired
	private PowerFacade powerFacade;

	@PostConstruct
	public void init() {
		eventBus.register(EventKey.SNATCH_RESULT, this);
	}

	@Override
	public void onEvent(Event paramEvent) {
		if (paramEvent.name == EventKey.SNATCH_RESULT) {
			SnatchResultEvent event = paramEvent.convert();
			broadcastSnatchResult(event);
		}

	}

	private void broadcastSnatchResult(SnatchResultEvent event) {
		// 是机器人则忽略
		if (event.snatchEnemyType == SnatchEnemyType.ROBOT) {
			return;
		}

		Power power = powerFacade.getPower(event.actorId);
		Power targetPower = powerFacade.getPower(event.targetActorId);
		NoticeConfig noticeConfig = NoticeService.get(NoticeType.POWER_RANK.getCode());
		boolean powerNotNull = (power != null && power.rank <= noticeConfig.getCondition());
		boolean targetPowerNotNull = (targetPower != null && targetPower.rank <= noticeConfig.getCondition());

		Actor actor = actorFacade.getActor(event.actorId);
		Actor targetActor = actorFacade.getActor(event.targetActorId);
		if (powerNotNull || targetPowerNotNull) {// 发起方或目标排行超过前三十
			String actorName = actor.actorName;
			long actorId = actor.getPkId();
			int level = actor.level;
			int vipLevel = vipFacade.getVipLevel(actorId);
			String otherActorName = targetActor.actorName;
			int otherLevel = targetActor.level;
			int otherVipLevel = vipFacade.getVipLevel(targetActor.getPkId());
			int num = event.goodsNum;
			int isWin = event.winLevel.isWin() ? 1 : 0;
			if (num > 0) {
				chatFacade.sendNoticeChat(actorName, actorId, level, vipLevel, otherActorName, otherLevel, otherVipLevel, Math.abs(num),
						isWin);
			}
		}
	}

	@Override
	public void broadcastSnatchPowerRank(BattleResult battleResult) {
		AttackPlayerRequest attackReq = (AttackPlayerRequest) battleResult.battleReq;
		Power power = powerFacade.getPower(attackReq.actorId);
		Power targetPower = powerFacade.getPower(attackReq.targetActorId);
		NoticeConfig noticeConfig = NoticeService.get(NoticeType.POWER_RANK.getCode());
		boolean powerNotNull = (power != null && power.rank <= noticeConfig.getCondition());
		boolean targetPowerNotNull = (targetPower != null && targetPower.rank <= noticeConfig.getCondition());
		Actor actor = actorFacade.getActor(attackReq.actorId);
		Actor targetActor = actorFacade.getActor(attackReq.targetActorId);
		if (powerNotNull || targetPowerNotNull) {
			int vipLevel = vipFacade.getVipLevel(actor.getPkId());
			int targetVipLevel = vipFacade.getVipLevel(targetActor.getPkId());
			int isWin = battleResult.fightData.result.isWin() ? 1 : 2;
			int isFirst = power.rank == 1 && isWin == 1 ? 1:0;
			chatFacade.sendPowerChat(actor.actorName, actor.getPkId(),
					actor.level, vipLevel, isWin, targetActor.level,
					targetVipLevel, targetActor.actorName, isFirst);
		}
	}

	@Override
	public void broadcastDemonSnatch(long receiverActorId, long snatchActorId) {
		Actor receiverActor = actorFacade.getActor(receiverActorId);
		Actor snatchActor = actorFacade.getActor(snatchActorId);
		if (receiverActor != null && snatchActor != null) {
			NoticeVO noticeVO = NoticeVO.valueOf(NoticeType.DEMON_NOTICE, snatchActor.getPkId(), snatchActor.actorName);
			NoticePushHelper.broadcastDemonNotice(receiverActorId, noticeVO);
		}
	}

	@Override
	public void broadcastCrossBattleEnd(int rank, List<RewardObject> rewardObjct) {
//		SysmailConfig config = SysmailService.get(4);
//		String text = FormulaHelper.excuteString(config.text, rank);
//		StringBuffer sb = new StringBuffer();
//		for(RewardObject reward:rewardObjct){
//			sb.append(reward.parse2String());
//			sb.append(Splitable.ELEMENT_DELIMITER);
//		}
//		sb.deleteCharAt(sb.length() - 1);
//		if(StringUtils.isNotBlank(text) && StringUtils.isNotBlank(sb.toString())){
//			NoticeVO noticeVO = NoticeVO.valueOf(NoticeType.CROSS_BATTLE, text,sb.toString());
//			NoticePushHelper.broadcastNotice(new ArrayList<Integer>(), noticeVO);
//		}
	}

}
