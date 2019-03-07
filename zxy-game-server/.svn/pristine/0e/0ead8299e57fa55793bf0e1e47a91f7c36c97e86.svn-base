package com.jtang.gameserver.module.adventures.vipactivity.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.HERO_NOT_EXITS;
import static com.jiatang.common.GameStatusCodeConstant.MAIN_HERO_ALREADY_SETED;
import static com.jiatang.common.GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Heros;
import com.jtang.gameserver.module.adventures.vipactivity.facade.MainHeroFacade;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.MainHeroResponse;
import com.jtang.gameserver.module.adventures.vipactivity.helper.VipActivityPushHelper;
import com.jtang.gameserver.module.buffer.facade.BufferFacade;
import com.jtang.gameserver.module.buffer.type.BufferSourceType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.helper.HeroPushHelper;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class MainHeroFacadeImpl implements MainHeroFacade, ActorLoginListener, ZeroListener{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainHeroFacadeImpl.class);
	@Autowired
	private HeroFacade heroFacade;
	
	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private Schedule schedule;
	
	@Autowired
	private BufferFacade bufferFacade;
	
	@Autowired
	private PlayerSession playerSession;
	
	@Autowired
	private ActorFacade actorFacade;
	
	@Override
	public TResult<MainHeroResponse> setMainHero(long actorId, int heroId, boolean useGold) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege =  vipFacade.getVipPrivilege(vipLevel);
		if (vipPrivilege == null || vipPrivilege.mainHeroAttributePercent == 0){
			return TResult.valueOf(VIP_LEVEL_NO_ENOUGH);
		}
		
		Heros heros = heroFacade.getHeros(actorId);
		if(heros.mainHeroId > 0){
			if(!TimeUtils.beforeTodayZero(heros.mainHeroTime)){
				return TResult.valueOf(MAIN_HERO_ALREADY_SETED);
			}
			resetMainHero(actorId, true);
		}
		
		HeroVO heroVO = heros.getHeroVO(heroId);
		if(heroVO == null){
			return TResult.valueOf(HERO_NOT_EXITS);
		}
		if (useGold) {
			
			Actor actor = actorFacade.getActor(actorId);
			int exp = FormulaHelper.executeInt(vipPrivilege.mainHeroAddExp, actor.level);
//			int consumeGold = FormulaHelper.executeInt(vip9Privilege.consumeGoldExpression, exp);
			//TODO: 主力仙人设置扣除金钱
			
//			boolean flag = actorFacade.decreaseGold(actorId, GoldDecreaseType.MAIN_HERO, 0);
//			if (flag == false) {
//				return TResult.valueOf(MAIN_HERO_GOLD_NOT_ENOUGH);
//			}
			
			heroFacade.addHeroExp(actorId, heroId, exp);
		}
		HeroConfig cfg = HeroService.get(heroId);
		int id = cfg.getRaomdomAttributeKey();
		HeroVOAttributeKey key = HeroVOAttributeKey.getByCode((byte)id);
		setMainHeroAttribute(actorId, heroVO,  key, heros);
		
		MainHeroResponse response = new MainHeroResponse(heroId, key.getCode(), caculateRate(actorId));
		return TResult.sucess(response);
	}
	
	private int setMainHeroAttribute(long actorId, HeroVO heroVO,  HeroVOAttributeKey key, Heros heros) {
		int attributeValue = getAddAttributeValue(actorId, heroVO, key);
		heros.attributeId = key.getCode();
		heros.mainHeroId = heroVO.heroId;
		heros.mainHeroTime = TimeUtils.getNow();
		
		heroFacade.updateHero(actorId, heroVO);
		addMainHeroBuff(actorId,key, attributeValue, heroVO.heroId, true);
		return attributeValue;
	}

	public void resetMainHero(long actorId, boolean isPush) {
		Heros heros = heroFacade.getHeros(actorId);
		HeroVOAttributeKey key = HeroVOAttributeKey.getByCode(heros.attributeId);
		int heroId = heros.mainHeroId;
		HeroVO heroVO = heros.getHeroVO(heroId);
		if(heroVO != null){
			heros.attributeId = 0;
			heros.mainHeroId = 0;
			heros.mainHeroTime = 0;
			
			heroFacade.updateHero(actorId, heroVO);
			removeMainHeroBuff(actorId, heroVO.heroId, key, isPush);
		}
	}
	
	private void removeMainHeroBuff(long actorId, int heroId, HeroVOAttributeKey key, boolean isPush) {
		bufferFacade.removeBufferBySourceType(actorId, heroId, BufferSourceType.MAIN_HERO_BUFFER);
		if (isPush) {
			
			HeroPushHelper.pushUpdateHero(actorId, heroId, key);
			
			//推送客户端
			List<BufferVO> list = bufferFacade.getBufferList(actorId, heroId);
			HeroPushHelper.pushHeroBuffers(actorId, heroId, list);
		}
	}
	
	private void addMainHeroBuff(long actorId, HeroVOAttributeKey key, int attributeValue, int heroId, boolean isPush) {
		BufferVO bufferVO = new BufferVO(key, attributeValue, BufferSourceType.MAIN_HERO_BUFFER);
		bufferFacade.addBuff(actorId, heroId, bufferVO);
		if (isPush) {
			HeroPushHelper.pushUpdateHero(actorId, heroId, key);
			
			//推送客户端
			List<BufferVO> list = bufferFacade.getBufferList(actorId, heroId);
			HeroPushHelper.pushHeroBuffers(actorId, heroId, list);
		}
	}
	
	@Override
	public TResult<MainHeroResponse> getMainHeroInfo(Long actorId) {
		Heros heros = heroFacade.getHeros(actorId);
		MainHeroResponse response = null;
		if(heros.mainHeroId > 0){
			HeroVOAttributeKey key = HeroVOAttributeKey.getByCode(heros.attributeId);
			response = new MainHeroResponse(heros.mainHeroId, key.getCode(), caculateRate(actorId));
		} else {
			response = new MainHeroResponse(0, (byte)0, caculateRate(actorId));
		}
		return TResult.sucess(response);
	}

	@Override
	public void onLogin(long actorId) {
		reset(actorId, false);
	}
	
	private void reset(long actorId, boolean isPush){
		Heros heros = heroFacade.getHeros(actorId);
		ChainLock lock = LockUtils.getLock(heros);
		try {
			lock.lock();
			if(heros.mainHeroId > 0){
				if(TimeUtils.beforeTodayZero(heros.mainHeroTime)){
					resetMainHero(actorId, isPush);
					VipActivityPushHelper.pushMainHeroReset(actorId, caculateRate(actorId));
				}
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}

	private int getAddAttributeValue(long actorId, HeroVO heroVO, HeroVOAttributeKey heroVOAttributeKey) {
		Map<AttackerAttributeKey, Integer> value = LineupHelper.getInstance().getEquipAndBaseAtt(actorId, heroVO.heroId);
		int result = 0;
		int baseValue = 0;
		switch (heroVOAttributeKey) {
		case ATK:
			baseValue = value.get(AttackerAttributeKey.ATK);
			break;
		case HP:
			baseValue = value.get(AttackerAttributeKey.HP);
			break;
		case DEFENSE:
			baseValue = value.get(AttackerAttributeKey.DEFENSE);
			break;

		default:
			break;
		}
		int mainHeroAttributePercent = caculateRate(actorId);
		result =  Math.round(baseValue * mainHeroAttributePercent / 1000);
		return result;
	}


	@Override
	public boolean isMainHero(long actorId, int heroId) {
		Heros heros = heroFacade.getHeros(actorId);
		if (heros.mainHeroId == heroId) {
			return true;
		}
		return false;
	}
	
	private int caculateRate(long actorId) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		if (vipPrivilege == null) {
			return 0;
		}
		int mainHeroAttributePercent = vipPrivilege.mainHeroAttributePercent;
		return mainHeroAttributePercent;
	}
	
	@Override
	public void onZero() {
		Set<Long> ids = playerSession.onlineActorList();
		for (long actorId : ids) {
			resetMainHero(actorId, true);
			VipActivityPushHelper.pushMainHeroReset(actorId, caculateRate(actorId));
		}
	}
	
	
}
