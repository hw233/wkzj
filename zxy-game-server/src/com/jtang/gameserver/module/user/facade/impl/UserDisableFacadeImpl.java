package com.jtang.gameserver.module.user.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.ACTOR_USEABLE;
import static com.jtang.core.protocol.StatusCode.ACTOR_DISABLED;
import static com.jtang.core.protocol.StatusCode.SUCCESS;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.UserDisabled;
import com.jtang.gameserver.module.user.dao.UserDisabledDao;
import com.jtang.gameserver.module.user.facade.UserDisableFacade;

@Component
public class UserDisableFacadeImpl implements UserDisableFacade {

	@Autowired
	UserDisabledDao userDisabledDao;

	@Override
	public Result disable(long actorId, int time) {
		UserDisabled userDisable = UserDisabled.valueOf(actorId, new Date(System.currentTimeMillis() + time * 1000));
		userDisabledDao.disableUser(actorId, userDisable);
		return Result.valueOf();
	}

	@Override
	public Result enable(long actorId) {
		List<UserDisabled> disabledList = userDisabledDao.getDisableList(actorId, "", "", "", "");
		if (disabledList.isEmpty()) {
			return Result.valueOf(ACTOR_USEABLE);
		}
		for (UserDisabled disabled : disabledList) {
			if (disabled != null) {
				userDisabledDao.enableUser(disabled);
			}
		}
		return Result.valueOf(SUCCESS);
	}

	@Override
	public TResult<UserDisabled> isDisable(long actorId, String sim, String mac, String imei, String remoteIp) {
		List<UserDisabled> disabledList = userDisabledDao.getDisableList(actorId, sim, mac, imei, remoteIp);
		for (UserDisabled disabled : disabledList) {
			if (disabled != null) {
				TResult<UserDisabled> result = TResult.valueOf(ACTOR_DISABLED);
				result.item = disabled;
				return result;
			}
		}
		return TResult.valueOf(SUCCESS);
	}

}
