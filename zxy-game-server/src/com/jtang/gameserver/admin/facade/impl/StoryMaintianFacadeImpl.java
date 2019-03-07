package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.BATTLE_IS_FIRST;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.BATTLE_IS_LAST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.admin.facade.StoryMaintianFacade;
import com.jtang.gameserver.dataconfig.model.BattleConfig;
import com.jtang.gameserver.dataconfig.service.StoryService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Stories;
import com.jtang.gameserver.module.story.dao.StoryDao;
import com.jtang.gameserver.module.story.facade.StoryFacade;
import com.jtang.gameserver.module.story.model.StoryVO;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class StoryMaintianFacadeImpl implements StoryMaintianFacade {

	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private StoryFacade storyFacade;
	@Autowired
	private StoryDao storyDao;

	@Override
	public TResult<Stories> addNextStory(long actorId, int star) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return TResult.valueOf(ACTOR_NOT_EXIST);
		}
		Stories stories = storyFacade.get(actorId);
		int battleId = stories.getBattleId();
		BattleConfig nextBattle = StoryService.getNextBattle(battleId);
		if (nextBattle == null) {
			return TResult.valueOf(BATTLE_IS_LAST);
		}
		stories.addBattleResult(nextBattle.getStoryId(), nextBattle.getBattleId(), (byte) star);
		stories.setBattleId(nextBattle.getBattleId());
		storyDao.update(stories);
		return TResult.sucess(stories);
	}

	@Override
	public TResult<Stories> deleteLastStory(long actorId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return TResult.valueOf(ACTOR_NOT_EXIST);
		}
		Stories stories = storyFacade.get(actorId);
		int battleId = stories.getBattleId();
		BattleConfig battleConfig = StoryService.get(battleId);
		BattleConfig beforBattle = StoryService.getBeforBattle(battleId);
		if (beforBattle == null) {
			return TResult.valueOf(BATTLE_IS_FIRST);
		}
		stories.setBattleId(beforBattle.getBattleId());
		StoryVO story = stories.getStoryMap().get(beforBattle.getStoryId());
		story.getBattleMap().remove(battleConfig.getBattleId());
		storyDao.update(stories);
		return TResult.sucess(stories);
	}

}
