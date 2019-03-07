package com.jtang.gameserver.module.hero.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.jtang.core.result.Result;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.HeroSoul;
import com.jtang.gameserver.module.hero.constant.HeroRule;
import com.jtang.gameserver.module.hero.dao.HeroSoulDao;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.helper.HeroPushHelper;
import com.jtang.gameserver.module.hero.model.HeroSoulVO;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulDecreaseType;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class HeroSoulFacadeImpl implements HeroSoulFacade {
	
	@Autowired
	HeroSoulDao heroSoulDao;
	@Autowired
	ActorFacade actorFacade;
	
	@Override
	public HeroSoul getSoul(long actorId) {
		return this.heroSoulDao.get(actorId);
	}

	@Override
	public Result addSoul(long actorId,HeroSoulAddType type, int heroId, int count) {
		HeroConfig config = HeroService.get(heroId);
		if (config == null) {
			return Result.valueOf(HERO_CONFIG_NOT_EXITST);
		}

		HeroSoul soul = getSoul(actorId);
		if (soul.getHeroCount() >= HeroRule.HERO_SOUL_MAX_COUNT && !soul.isHeroSoulExists(heroId)) {
			return Result.valueOf(TOO_MUCH_HEROS_SOUL);
		}
		soul.addSoul(heroId, count);
		heroSoulDao.update(soul);

		// 推送
		int totalCount = soul.getSoulCount(heroId);
		HeroPushHelper.pushHeroSoul(actorId, heroId, totalCount);
		
		//oss
		Actor actor = actorFacade.getActor(actorId);
		GameOssLogger.heroSoulAdd(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, type, heroId, count);
		
		return Result.valueOf();
	}

	@Override
	public Result reductSoul(long actorId, HeroSoulDecreaseType type, int heroId, int count) {
		Assert.isTrue(heroId > 0, "heroId不能为0");
		HeroSoul soul = getSoul(actorId);
		if (soul == null) {
			return Result.valueOf(HERO_SOUL_NOT_EXITS);
		}
		
		boolean flag = soul.reduceSoul(heroId, count);
		if (flag) {
			int totalCount = soul.getSoulCount(heroId);
			HeroPushHelper.pushHeroSoul(actorId, heroId, totalCount);
			this.heroSoulDao.update(soul);
			
			//oss
			Actor actor = actorFacade.getActor(actorId);
			GameOssLogger.heroSoulDecrease(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, type, heroId, count);
			
			return Result.valueOf();
		}
		
		return Result.valueOf(HERO_SOUL_NOT_ENOUGH);
	}
	
	@Override
	public List<HeroSoulVO> getList(long actorId) {
		return this.heroSoulDao.getList(actorId);
	}

	@Override
	public int getSoulNum(long actorId, int heroId) {
		HeroSoul soul = this.getSoul(actorId);
		return soul.getSoulCount(heroId);
	}

}
