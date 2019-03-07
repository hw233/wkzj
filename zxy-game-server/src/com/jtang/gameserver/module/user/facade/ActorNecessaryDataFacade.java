package com.jtang.gameserver.module.user.facade;

import com.jtang.gameserver.module.user.handler.response.ActorNecessaryDataResponse;

public interface ActorNecessaryDataFacade {
	ActorNecessaryDataResponse get(long actorId);
}
