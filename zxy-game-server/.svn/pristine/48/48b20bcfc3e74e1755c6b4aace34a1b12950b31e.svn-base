package com.jtang.gameserver.module.adventures.vipactivity.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.GiveEquipRewardConfig;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dataconfig.service.GiveEquipRewardService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.VipPrivilege;
import com.jtang.gameserver.module.adventures.vipactivity.dao.VipPrivilegeDao;
import com.jtang.gameserver.module.adventures.vipactivity.facade.GiveEquipFacade;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.GiveEquipInfoResponse;
import com.jtang.gameserver.module.adventures.vipactivity.helper.VipActivityPushHelper;
import com.jtang.gameserver.module.adventures.vipactivity.model.GiveEquipVO;
import com.jtang.gameserver.module.adventures.vipactivity.model.VipBaseVO;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.model.Vip13Privilege;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class GiveEquipFacadeImpl implements GiveEquipFacade, ActorLoginListener, ZeroListener {

	@Autowired
	VipPrivilegeDao vipPrivilegeDao;

	@Autowired
	VipFacade vipFacade;

	@Autowired
	ActorFacade actorFacade;

	@Autowired
	NotifyFacade notifyFacade;

	@Autowired
	Schedule schedule;

	@Autowired
	PlayerSession playerSession;
	
	@Autowired
	ChatFacade chatFacade;

	@Override
	public TResult<Integer> giveEquip(long actorId, long otherId) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		//Vip12Privilege vip12Privileg = (Vip12Privilege) vipFacade.getVipPrivilege(Vip12Privilege.vipLevel);
		if (vipLevel != Vip13Privilege.vipLevel) {
			return TResult.valueOf(VIP_LEVEL_NO_ENOUGH);
		}
		VipPrivilege vipActivity = vipPrivilegeDao.get(actorId);
		GiveEquipVO giveEquipVO = (GiveEquipVO) vipActivity.getExtensionMap().get(vipLevel);
		if (giveEquipVO != null && DateUtils.isToday(giveEquipVO.sendTime)) {
			return TResult.valueOf(GameStatusCodeConstant.TREASURE_SEND_EQUIP);
		}

		GiveEquipRewardConfig reward = GiveEquipRewardService.random();
		//vipFacade.decreaseTicket(actorId, TicketDecreaseType.TREASURE, vip12Privileg.consumeTicketNum, 0, 0);
		notifyFacade.createGiveEquip(actorId, otherId, reward.equipId, reward.num);
		giveEquipVO = GiveEquipVO.valueOf();
		giveEquipVO.actorId = otherId;
		giveEquipVO.sendTime = TimeUtils.getNow();
		vipActivity.getExtensionMap().put(vipLevel, giveEquipVO);
		vipPrivilegeDao.update(vipActivity);
		Actor otherActor = actorFacade.getActor(otherId);
		int firstHeroId = 0;
		int level = otherActor.level;
		EquipConfig equipConfig = EquipService.get(reward.equipId);
		VipActivityPushHelper.pushGiveEquipInfo(actorId, 1, otherActor.actorName, level, firstHeroId);
		chatFacade.sendTreasureChat(actorId, otherId, equipConfig.getEquipType().getId(), equipConfig.getEquipId());
		return TResult.sucess(reward.equipId);
	}

	@Override
	public TResult<GiveEquipInfoResponse> giveEquipInfo(long actorId) {
		VipPrivilege vipPrivilege = vipPrivilegeDao.get(actorId);
		GiveEquipVO giveEquipVo = (GiveEquipVO) vipPrivilege.getExtensionMap().get(Vip13Privilege.vipLevel);
		if (giveEquipVo != null) {
			Actor otherActor = actorFacade.getActor(giveEquipVo.actorId);
			int firstHeroId = 0;
			int level = otherActor.level;
			return TResult.sucess(new GiveEquipInfoResponse(1, otherActor.actorName, level, firstHeroId));
		}
		return TResult.valueOf(StatusCode.SUCCESS);
	}

	@Override
	public void onLogin(long actorId) {
		VipPrivilege vipPrivilege = vipPrivilegeDao.get(actorId);
		clean(actorId, vipPrivilege);
	}
	
	@Override
	public void onZero() {
		Set<Long> actorIds = playerSession.onlineActorList();
		for (Long actorId : actorIds) {
			VipPrivilege vipPrivilege = vipPrivilegeDao.get(actorId);
			clean(actorId, vipPrivilege);
		}
	}

	private void clean(long actorId, VipPrivilege vipPrivilege) {
		Map<Integer, VipBaseVO> vipPrivilegeMap = vipPrivilege.getExtensionMap();
		if(vipPrivilegeMap == null || vipPrivilegeMap.isEmpty()){
			return;
		}
		
		if(vipPrivilege.getExtensionMap().get(Vip13Privilege.vipLevel) != null){
			int sendTime = ((GiveEquipVO) vipPrivilegeMap.get(Vip13Privilege.vipLevel)).sendTime;
			if (!DateUtils.isToday(sendTime)) {
				vipPrivilege.getExtensionMap().remove(Vip13Privilege.vipLevel);
				vipPrivilegeDao.update(vipPrivilege);
				VipActivityPushHelper.pushGiveEquipInfo(actorId, 0, "", 0, 0);
			}
		}
	}

}
