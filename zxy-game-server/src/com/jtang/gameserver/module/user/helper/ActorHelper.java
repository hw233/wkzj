package com.jtang.gameserver.module.user.helper;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class ActorHelper {
	@Autowired
	ActorFacade actorFacade;
	
	private static ObjectReference<ActorHelper> ref = new ObjectReference<ActorHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	/**
	 * 获取角色名
	 * @param actorId
	 * @return
	 */
	public static String getActorName(long actorId) {
		Actor actor = ref.get().actorFacade.getActor(actorId);
		if (actor == null) {
			return "";
		}
		return actor.actorName;
	}
	
	/**
	 * 获取角色等级
	 * @param actorId
	 * @return
	 */
	public static int getActorLevel(long actorId) {
		Actor actor = ref.get().actorFacade.getActor(actorId);
		if (actor == null) {
			return 0;
		}
		return actor.level;
	}
	
}
