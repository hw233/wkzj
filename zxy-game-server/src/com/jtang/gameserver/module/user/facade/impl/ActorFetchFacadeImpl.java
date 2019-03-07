package com.jtang.gameserver.module.user.facade.impl;

//import java.util.ArrayList;
//import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.user.dao.ActorDao;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.ActorFetchFacade;
//import com.jtang.sm2.dbproxy.entity.Actor;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 
 * @author 0x737263
 *
 */
@Component
public class ActorFetchFacadeImpl implements ActorFetchFacade {
	
	@Autowired
	private ActorDao actorDao;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private PlayerSession playerSession;
	
	@Override
	public Map<Long, Integer> getLevelList(int loginTime, int num) {
		return actorDao.getLevelList(loginTime, num);
	}

	@Override
	public List<Long> getOnlineActorIds(List<Integer> channelIdList) {
		if (channelIdList == null || channelIdList.size() < 1) {
			return new ArrayList<>();
		}

		Set<Long> onlineActorIds = playerSession.onlineActorList();
		List<Long> filterList = new ArrayList<>();

		Actor actor = null;
		for (Long actorId : onlineActorIds) {
			actor = actorFacade.getActor(actorId);
			if (actor != null && channelIdList.contains(actor.channelId)) {
				filterList.add(actorId);
			}
		}

		return filterList;
	}




	
}
