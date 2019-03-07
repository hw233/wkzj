package com.jtang.gameserver.module.extapp.invite.facade.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.dataconfig.model.InviteGlobalConfig;
import com.jtang.gameserver.dataconfig.model.InviteRewardConfig;
import com.jtang.gameserver.dataconfig.service.InviteService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Invite;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.invite.dao.InviteDao;
import com.jtang.gameserver.module.extapp.invite.facade.InviteFacade;
import com.jtang.gameserver.module.extapp.invite.handler.response.InviteResponse;
import com.jtang.gameserver.module.extapp.invite.helper.InvitePushHelper;
import com.jtang.gameserver.module.extapp.invite.type.ReceiveStatusType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;

@Component
public class InviteFacadeImpl implements InviteFacade, Receiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(InviteFacadeImpl.class);
	
	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	VipFacade vipFacade;
	
	@Autowired
	InviteDao inviteDao;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	@Autowired
	HeroFacade heroFacade;

	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	EventBus eventBus;
	
	@PostConstruct
	private void init() {
		eventBus.register(EventKey.ACTOR_LEVEL_UP, this);
	}
	
	@Override 
	public TResult<InviteResponse> getInfo(long actorId) {
		Result result = baseConditionCheck(actorId);
		if (result.isFail()) {
			return TResult.valueOf(result.statusCode);
		}
		
		Invite invite = inviteDao.get(actorId);
		InviteResponse response = new InviteResponse();
		response.inviteCode = invite.inviteCode;
		response.inviteName = ActorHelper.getActorName(invite.inviteActor);
		response.isInvite = invite.targetInvite == 0L? ReceiveStatusType.DID_NOT_RECEIVE.getType() : ReceiveStatusType.CAN_RECEIVE.getType();
		if (invite.rewardMap.isEmpty()) {
			Collection<InviteRewardConfig> rewardCollection = InviteService.getAllRewardConfigs();
			for (InviteRewardConfig rewardConfig : rewardCollection) {
				invite.rewardMap.put(rewardConfig.inviteLevel, ReceiveStatusType.DID_NOT_RECEIVE.getType());
			}
		} else {
			invite.arrangementRewardMap(ActorHelper.getActorLevel(invite.inviteActor));
		}
		response.rewardMap = invite.rewardMap;
		return TResult.sucess(response);
	}

	@Override
	public Result acceptInvitation(long beInviteId, String inviteCode) {
		Result result = baseConditionCheck(beInviteId);
		if (result.isFail()) {
			return result;
		}
		Invite beInvite = inviteDao.get(beInviteId);
		if (beInvite.targetInvite == 0L) {
			long inviteActorId = inviteDao.getInviteByCode(inviteCode);
			if (inviteActorId == 0L) {
				return Result.valueOf(GameStatusCodeConstant.INVITE_CODE_NOT_EXIST);
			}
			if (inviteActorId == beInviteId) {
				return Result.valueOf(GameStatusCodeConstant.INVITE_CODE_BELONGS_TO_OWN);
			}
			int beInviteActorLevel = ActorHelper.getActorLevel(beInviteId);
			//被邀请者要小于指定等级,才能输入邀请码
			if (beInviteActorLevel > InviteService.getGlobalConfig().minInviteLevel) {
				return Result.valueOf(GameStatusCodeConstant.INVITE_CODE_NOT_EXIST);
			}
			
			int inviteActorLevel = ActorHelper.getActorLevel(inviteActorId);
			//邀请者要大于指定等级,才能显示邀请码
			if (inviteActorLevel < InviteService.getGlobalConfig().minInviteLevel) {
				return Result.valueOf(GameStatusCodeConstant.INVITE_CODE_NOT_EXIST);
			}
			
			Invite invite = inviteDao.get(inviteActorId);
			if (invite.inviteActor != 0L) {
				return Result.valueOf(GameStatusCodeConstant.INVITE_CODE_HAD_USED);
			}
			ChainLock lock = LockUtils.getLock(invite, beInvite);
			try {
				lock.lock();
				beInvite.targetInvite = inviteActorId;
				beInvite.rewardMap.put(0, Math.max(beInvite.rewardMap.get(0), ReceiveStatusType.CAN_RECEIVE.getType()));
				inviteDao.update(beInvite);
				
				invite.rewardMap.put(0, Math.max(invite.rewardMap.get(0), ReceiveStatusType.CAN_RECEIVE.getType()));
				invite.receivedChange(beInviteId, beInviteActorLevel);
				inviteDao.update(invite);
			} catch (Exception e) {
				LOGGER.error("{}", e);
			} finally {
				lock.unlock();
			}
			InvitePushHelper.pushInviteReward(beInvite, ActorHelper.getActorName(beInvite.inviteActor));
			return Result.valueOf(StatusCode.SUCCESS);
		} else {
			return Result.valueOf(GameStatusCodeConstant.SELF_HAD_RECEIVED);
		}
	}


	@Override
	public Result getInviteReward(long actorId, int inviteId) {
		Result result = baseConditionCheck(actorId);
		if (result.isFail()) {
			return result;
		}
		Invite beInvite = inviteDao.get(actorId);
		if (beInvite.rewardMap.containsKey(inviteId)) {
			int status = beInvite.rewardMap.get(inviteId);
			ReceiveStatusType type = ReceiveStatusType.getByType(status);
			if (type == ReceiveStatusType.DID_NOT_RECEIVE) {
				return Result.valueOf(GameStatusCodeConstant.REWARD_CONDITIONS_NOT_ACHIEVED);
			}
			if (type == ReceiveStatusType.HAVE_RECEIVED) {
				return Result.valueOf(GameStatusCodeConstant.REWARD_HAD_RECEIVED);
			}
			ChainLock lock = LockUtils.getLock(beInvite);
			try {
				lock.lock();
				beInvite.rewardMap.put(inviteId, ReceiveStatusType.HAVE_RECEIVED.getType());
				inviteDao.update(beInvite);
			} catch (Exception e) {
				LOGGER.error("{}", e);
			} finally {
				lock.unlock();
			}
			List<RewardObject> rewardObjects = InviteService.getRewardListByInviteLevel(inviteId);
			sendReward(actorId, rewardObjects);
		} else {
			return Result.valueOf(GameStatusCodeConstant.INVITE_REWARD_NOT_EXIST);
		}
		return Result.valueOf();
	}
	
	@Override
	public Result resetBeInviter(long actorId) {
		Result result = baseConditionCheck(actorId);
		if (result.isFail()) {
			return result;
		}
		Invite invite = inviteDao.get(actorId);
		if (invite.inviteActor == 0L) {
			return Result.valueOf(GameStatusCodeConstant.INVITE_NOT_USED);
		}
		
		int tickitNum = InviteService.getGlobalConfig().resetCost;
		boolean isEnough = vipFacade.hasEnoughTicket(actorId, tickitNum);
		if (isEnough) {
			vipFacade.decreaseTicket(actorId, TicketDecreaseType.INVITE, tickitNum, 0, 0);
		} else {
			return Result.valueOf(GameStatusCodeConstant.TICKET_NOT_ENOUGH);
		}
		Invite beInvite = inviteDao.get(invite.inviteActor);
		ChainLock lock = LockUtils.getLock(invite, beInvite);
		try {
			lock.lock();
			invite.inviteActor = 0L;
			beInvite.targetInvite = 0L;
			inviteDao.update(beInvite);
			inviteDao.update(invite);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		InvitePushHelper.pushInviteReward(invite, ActorHelper.getActorName(invite.inviteActor));
		InvitePushHelper.pushInviteReward(beInvite, ActorHelper.getActorName(beInvite.inviteActor));
		return Result.valueOf();
	}
	
	private Result baseConditionCheck(long actorId) {
		Actor actor = actorFacade.getActor(actorId);
		InviteGlobalConfig config = InviteService.getGlobalConfig();
		if(actor.level < config.openLevel){
			return Result.valueOf(GameStatusCodeConstant.INVITE_LEVEL_NOT_ENOUGH);
		}
		return Result.valueOf();
	}
	
	
	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param id
	 * @param num
	 * @param rewardType
	 */
	private void sendReward(long actorId, int id, int num, RewardType rewardType) {
		switch (rewardType) {
		case EQUIP: {
			for (int i = 0; i < num; i++) {
				equipFacade.addEquip(actorId, EquipAddType.INVITE_FRIEND, id);
			}
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.INVITE_FRIEND, id, num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.INVITE_FRIEND, id, num);
			break;
		}
		case TICKET: {
			vipFacade.addTicket(actorId, TicketAddType.INVITE_FRIEND, num);
			break;
		}

		default:
			LOGGER.error(String.format("类型错误，type:[%s]", rewardType.getCode()));
			break;
		}
	}

	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param list
	 */
	private void sendReward(long actorId, List<RewardObject> list) {
		for (RewardObject rewardObject : list) {
			sendReward(actorId, rewardObject.id, rewardObject.num,rewardObject.rewardType);
		}
	}

	@Override
	public void onEvent(Event paramEvent) {
		if(paramEvent.name == EventKey.ACTOR_LEVEL_UP){
			ActorLevelUpEvent event = paramEvent.convert();
			long beInviteId = event.actorId;
			int beInviteLevel = event.actor.level;
			Invite beInvite = inviteDao.get(beInviteId);
			if (beInvite.targetInvite != 0L) {
				//作为被邀请者
				boolean isNeedPush = InviteService.getNeedPushByInviteLevel(beInviteLevel);
				if (isNeedPush == true) {
					Invite invite = inviteDao.get(beInvite.targetInvite);
					ChainLock lock = LockUtils.getLock(beInvite);
					try {
						lock.lock();
						invite.receivedChange(beInvite.actorId, beInviteLevel);
						inviteDao.update(invite);
					} catch (Exception e) {
						LOGGER.error("{}", e);
					} finally {
						lock.unlock();
					}
				}
			}
		}
	}
}
