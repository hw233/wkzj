package com.jtang.gameserver.module.adventures.vipactivity.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.EQUIP_COMPOSE_LEVEL_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_COMPOSE_REQUIRE_NUM_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_COMPOSE_STAR_DIFFERENT;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_COMPOSE_TODAY_IS_USED;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_COMPOSE_TYPE_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH;
import static com.jtang.core.protocol.StatusCode.SUCCESS;
import static com.jtang.core.protocol.StatusCode.TICKET_NOT_ENOUGH;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.EquipType;
import com.jiatang.common.model.EquipVO;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.EquipComposeConfig;
import com.jtang.gameserver.dataconfig.service.EquipComposeService;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.module.adventures.vipactivity.constant.EquipComposeRule;
import com.jtang.gameserver.module.adventures.vipactivity.facade.EquipComposeFacade;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.EquipComposeResultResponse;
import com.jtang.gameserver.module.adventures.vipactivity.helper.ComposeHelper;
import com.jtang.gameserver.module.adventures.vipactivity.helper.VipActivityPushHelper;
import com.jtang.gameserver.module.adventures.vipactivity.model.FailReward;
import com.jtang.gameserver.module.adventures.vipactivity.type.VipActivityKey;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.equip.type.EquipDecreaseType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class EquipComposeFacadeImpl implements EquipComposeFacade, ActorLoginListener, ZeroListener {

	@Autowired
	EquipFacade equipFacade;
	@Autowired
	LineupFacade lineupFacade;
	@Autowired
	VipFacade vipFacade;
	
	@Autowired
	private PlayerSession playerSession;
	
	@Autowired
	private Schedule schedule;
	
	@Override
	public TResult<EquipComposeResultResponse> compose(long actorId, int equipType, boolean useTicket, List<Long> uuidList) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		//TODO V5开启功能
		if (vipPrivilege == null || vipPrivilege.addComposeEquipNum == 0) { 
			return TResult.valueOf(VIP_LEVEL_NO_ENOUGH);
		}
		
//		Vip7Privilege vip7Privilege = (Vip7Privilege) vipFacade.getVipPrivilege(Vip7Privilege.vipLevel);
//		//vip等级增加 合成次数增加
//		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
//		int num = vipPrivilege.addComposeEquipNum;
//		num = num >= 0? num : 0;
//		num += vip7Privilege.times;
		if (equipFacade.canCompose(actorId, vipPrivilege.addComposeEquipNum) == false) {
			return TResult.valueOf(EQUIP_COMPOSE_TODAY_IS_USED);
		}		
		
		EquipType type = EquipType.getType(equipType);
		if (type == null) {
			return TResult.valueOf(EQUIP_COMPOSE_TYPE_ERROR);
		}
		int level = ActorHelper.getActorLevel(actorId);
		
		//合成装备vip等级不同开放不同
		switch (type) {
			case WEAPON:
				if (level < EquipComposeRule.ATK_EQUIP_COMPOSE) {
					return TResult.valueOf(EQUIP_COMPOSE_LEVEL_NOT_ENOUGH);
				}
				break;
			case ARMOR:
				if (level < EquipComposeRule.DEF_EQUIP_COMPOSE) {
					return TResult.valueOf(EQUIP_COMPOSE_LEVEL_NOT_ENOUGH);
				}
				break;
			case ORNAMENTS:
				if (level < EquipComposeRule.HP_EQUIP_COMPOSE) {
					return TResult.valueOf(EQUIP_COMPOSE_LEVEL_NOT_ENOUGH);
				}
				break;
	
			default:
				break;
		}

//		if (uuidList.size() < vip7Privilege.consumeEquipNum) {
//			return TResult.valueOf(EQUIP_COMPOSE_UUID_ERROR);
		//TODO 判断装备合成消耗个数
//		}

		short canDelete = equipFacade.canDelete(actorId, uuidList);
		if (canDelete != SUCCESS) {
			return TResult.valueOf(canDelete);
		}
		List<Integer> equipIdList = new ArrayList<>();
		for (long uuid : uuidList) {
			EquipVO vo = equipFacade.get(actorId, uuid);
			equipIdList.add(vo.equipId);
		}

		if (uuidList.size() != equipIdList.size()) {
			return TResult.valueOf(EQUIP_COMPOSE_STAR_DIFFERENT);
		}

		//获取合成的星级
		TResult<Integer> starResult = EquipService.getStar(equipIdList);
		if (starResult.isFail()) {
			return TResult.valueOf(starResult.statusCode);
		}

		int star = starResult.item;
		EquipComposeConfig composeConfig = EquipComposeService.get(equipType, star);
		if (composeConfig == null || composeConfig.getRequireEquipNum() != equipIdList.size()) {
			return TResult.valueOf(EQUIP_COMPOSE_REQUIRE_NUM_ERROR);
		}
		

		if (useTicket) {
			boolean ticketResult = vipFacade.decreaseTicket(actorId, TicketDecreaseType.EQUIP_COMPOSE, composeConfig.getConsumeTicket(), 0, 0);
			if (ticketResult == false) {
				return TResult.valueOf(TICKET_NOT_ENOUGH);
			}
		}
		
		//批量删除合成的装备
		short statusCode = equipFacade.delEquip(actorId, EquipDecreaseType.COMPOSE, uuidList);
		if (statusCode != SUCCESS) {
			return TResult.valueOf(statusCode);
		}

		equipFacade.recordCompose(actorId);
		int newEquipId = EquipComposeService.randEquip(equipType, star, useTicket);
		int composeNum = equipFacade.getComposeNum(actorId);
		List<FailReward> failList = Collections.emptyList();
		if (newEquipId == 0) {
			failList = composeConfig.list;
			EquipComposeResultResponse equipComposeResultResponse = new EquipComposeResultResponse(0, composeNum, failList);
			ComposeHelper.equipComposeFail(actorId, composeConfig.list);
			return TResult.sucess(equipComposeResultResponse);
		} 
		
		TResult<Long> result = equipFacade.addEquip(actorId, EquipAddType.EQUIP_COMPOSE, newEquipId);
		if (result.isFail()) {
			return TResult.valueOf(result.statusCode);
		}
		EquipComposeResultResponse equipComposeResultResponse = new EquipComposeResultResponse(result.item, composeNum, failList);
		return TResult.sucess(equipComposeResultResponse);
	}

	@Override
	public void onLogin(long actorId) {
		equipFacade.chechAndResetCompose(actorId);
	}

	@Override
	public void onZero() {
		Set<Long> actors = playerSession.onlineActorList();
		for (long actorId : actors) {
			equipFacade.chechAndResetCompose(actorId);
			VipActivityPushHelper.pushVipActivity(actorId, VipActivityKey.EQUIP_COMPOSE_NUM, 0);
		}
	}


}
