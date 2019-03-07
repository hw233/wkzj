package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.facade.HeroMaintianFacade;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroDecreaseType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulDecreaseType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class HeroMaintianFacadeImpl implements HeroMaintianFacade {

	@Autowired
	ActorFacade actorFacade;

	@Autowired
	HeroFacade heroFacade;

	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	LineupFacade lineupFacade;

	@Override
	public Result deleteHero(long actorId, int heroId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		List<Integer> heroIds = new ArrayList<Integer>();
		heroIds.add(heroId);
		boolean inLineup = lineupFacade.isHeroInLineup(actorId, heroId);
		if(inLineup){
			Result result = lineupFacade.unassignHero(actorId, heroId);
			result.statusCode = heroFacade.removeHero(actorId, HeroDecreaseType.ADMIN, heroIds);
			return result;
		}else{
			short statusCode = heroFacade.removeHero(actorId, HeroDecreaseType.ADMIN, heroIds);
			return Result.valueOf(statusCode);
		}
	}

	@Override
	public Result addHeroExp(long actorId, int heroId, int exp) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		Result result = heroFacade.addHeroExp(actorId, heroId, exp);
		return result;
	}

	@Override
	public Result addHeroSoul(long actorId, int heroId, int num) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		Result result = heroSoulFacade.addSoul(actorId, HeroSoulAddType.ADMIN, heroId, num);
		return result;
	}

	@Override
	public Result deleteHeroSoul(long actorId, int heroId, int num) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		Result result = heroSoulFacade.reductSoul(actorId, HeroSoulDecreaseType.ADMIN, heroId, num);
		return result;
	}
	
	@Override
	public Result addAllHeroSoul(long actorId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		Collection<HeroConfig> collection = HeroService.getAll();
		for (HeroConfig heroConfig : collection) {
			Result result = heroSoulFacade.addSoul(actorId, HeroSoulAddType.ADMIN, heroConfig.getHeroId(), 100);
			if (result.isFail()) {
				continue;
			}
		}
		return Result.valueOf();
	}

}
