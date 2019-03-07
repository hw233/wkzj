package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.CHAT_ALREADY_BE_FORBIDDEN;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.CHAT_FORBIDDEN_ACTOR_NOT_EXIST;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.CHAT_FORBIDDEN_TIME_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.CHAT_NOT_FORBIDDEN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.admin.facade.ChatMaintianFacade;
import com.jtang.gameserver.dbproxy.entity.Chat;
import com.jtang.gameserver.module.chat.dao.ChatDao;
import com.jtang.gameserver.module.chat.helper.MessageHelper;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class ChatMaintianFacadeImpl implements ChatMaintianFacade {

	@Autowired
	private ChatDao chatDao;

	@Autowired
	private ActorFacade actorFacade;
	
	@Override
	public Result sendForbiddenChat(long actorId, int relieveTime) {
		if (actorFacade.isExists(actorId) == false) {
			return Result.valueOf(CHAT_FORBIDDEN_ACTOR_NOT_EXIST);
		}
		
		if (relieveTime < 0) {
			return Result.valueOf(CHAT_FORBIDDEN_TIME_ERROR);
		}
		
		Chat chat = chatDao.get(actorId);
		int startTime = chat.forbiddenTime;
		int endTime = chat.unforbiddenTime;
		boolean isForbidden = MessageHelper.isForbidden(startTime, endTime);
		if (isForbidden) {
			return Result.valueOf(CHAT_ALREADY_BE_FORBIDDEN);
		}
		int now = TimeUtils.getNow();
		chat.forbiddenTime = now;
		chat.unforbiddenTime = now + relieveTime;
		chatDao.update(chat);
		return Result.valueOf();
	}

	@Override
	public Result sendUnforbiddenChat(long actorId) {
		if (actorFacade.isExists(actorId) == false) {
			return Result.valueOf(CHAT_FORBIDDEN_ACTOR_NOT_EXIST);
		}
		Chat chat = chatDao.get(actorId);
		int endTime = chat.unforbiddenTime;
		if (endTime == 0) {
			return Result.valueOf(CHAT_NOT_FORBIDDEN);
		}
		chat.forbiddenTime = TimeUtils.getNow();
		chat.unforbiddenTime = 0;
		chatDao.update(chat);
		return Result.valueOf();
	}

}
