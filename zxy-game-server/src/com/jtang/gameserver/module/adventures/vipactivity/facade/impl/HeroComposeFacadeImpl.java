package com.jtang.gameserver.module.adventures.vipactivity.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.HERO_COMPOSE_REQUIRE_NUM_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.HERO_COMPOSE_STAR_DIFFERENT;
import static com.jiatang.common.GameStatusCodeConstant.HERO_COMPOSE_TODAY_IS_USED;
import static com.jiatang.common.GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH;
import static com.jtang.core.protocol.StatusCode.SUCCESS;
import static com.jtang.core.protocol.StatusCode.TICKET_NOT_ENOUGH;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.HeroComposeConfig;
import com.jtang.gameserver.dataconfig.service.HeroComposeService;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.module.adventures.vipactivity.facade.HeroComposeFacade;
import com.jtang.gameserver.module.adventures.vipactivity.facade.MainHeroFacade;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.HeroComposeResultResponse;
import com.jtang.gameserver.module.adventures.vipactivity.helper.ComposeHelper;
import com.jtang.gameserver.module.adventures.vipactivity.helper.VipActivityPushHelper;
import com.jtang.gameserver.module.adventures.vipactivity.type.VipActivityKey;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroDecreaseType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 仙人合成
 * @author 0x737263
 *
 */
@Component
public class HeroComposeFacadeImpl implements HeroComposeFacade, ActorLoginListener, ZeroListener{
	@Autowired
	HeroFacade heroFacade;
	@Autowired
	LineupFacade lineupFacade;
	@Autowired
	VipFacade vipFacade;
	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	@Autowired
	private PlayerSession playerSession;
	
	@Autowired
	private MainHeroFacade mainHeroFacade;
	@Autowired
	private Schedule schedule;
	
	@Override
	public TResult<HeroComposeResultResponse> compose(long actorId, boolean useTicket, List<Integer> heroIdList) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		if (vipPrivilege == null || vipPrivilege.addComposeHeroNum == 0) {
			return TResult.valueOf(VIP_LEVEL_NO_ENOUGH);
		}
//		// vip等级增加 合成次数增加
//		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
//		int times = vipPrivilege.addComposeHeroNum;
//		times = times >= 0? times : 0;
//		times += vip10Privilege.times;
		if (heroFacade.canCompose(actorId, vipPrivilege.addComposeHeroNum) == false) {
			return TResult.valueOf(HERO_COMPOSE_TODAY_IS_USED);
		}	
		
		short canConsume = heroFacade.canDelete(actorId, heroIdList);
		if (canConsume != SUCCESS) {
			return TResult.valueOf(canConsume);
		}
		
		TResult<Integer> starResult = HeroService.getStar(heroIdList);
		if (starResult.isFail()) {
			return TResult.valueOf(starResult.statusCode);
		}
		int star = starResult.item;
		HeroComposeConfig composeConfig = HeroComposeService.get(star);
		if (composeConfig == null) {
			return TResult.valueOf(HERO_COMPOSE_STAR_DIFFERENT);
		}
		if (composeConfig.getRequireHeroNum() != heroIdList.size()) {
			return TResult.valueOf(HERO_COMPOSE_REQUIRE_NUM_ERROR);
		}
		
		if (useTicket) {
			if (vipFacade.hasEnoughTicket(actorId,  composeConfig.getConsumeTicket()) == false) {
				return TResult.valueOf(TICKET_NOT_ENOUGH);
			}
			boolean ticketResult = vipFacade.decreaseTicket(actorId, TicketDecreaseType.HERO_COMPOSE, composeConfig.getConsumeTicket(), 0, 0);
			if (ticketResult == false) {
				return TResult.valueOf(TICKET_NOT_ENOUGH);
			}
		}
		
		
		//批量删除合成的仙人
		short statusCode = heroFacade.removeHero(actorId, HeroDecreaseType.COMPOSE, heroIdList);
		if (statusCode != SUCCESS) {
			return TResult.valueOf(statusCode);
		}
		heroFacade.recordCompose(actorId);
		int newHeroId = HeroComposeService.randHero(star, useTicket);
		int composeNum = heroFacade.getComposeNum(actorId);
		HeroComposeResultResponse heroComposeResultResponse = new HeroComposeResultResponse(newHeroId, composeNum, composeConfig.list);
		if (newHeroId == 0) {
			ComposeHelper.heroComposeFail(actorId, composeConfig.list);
			return TResult.sucess(heroComposeResultResponse);
		}
		
		HeroVO exist = heroFacade.getHero(actorId, newHeroId);
		if (exist != null){
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.COMPOSE, newHeroId, composeConfig.getHeroSoulNum());
		} else {
			TResult<HeroVO> addResult = heroFacade.addHero(actorId, HeroAddType.COMPOSE, newHeroId);
			if(addResult.isFail()) {
				return TResult.valueOf(addResult.statusCode);
			}
		}
		
		return TResult.sucess(heroComposeResultResponse);
	}


	@Override
	public void onLogin(long actorId) {
		heroFacade.checkAndReset(actorId);
	}

	@Override
	public void onZero() {
		Set<Long> actors = playerSession.onlineActorList();
		for (long actorId : actors) {
			heroFacade.checkAndReset(actorId);
			VipActivityPushHelper.pushVipActivity(actorId, VipActivityKey.HERO_COMPOSE_NUM, 0);
		}
	}

}
