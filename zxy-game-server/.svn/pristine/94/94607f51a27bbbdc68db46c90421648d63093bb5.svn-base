package com.jtang.gameserver.module.chat.helper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.helper.EmojiFilter;
import com.jtang.gameserver.module.chat.constant.ChatRule;
import com.jtang.gameserver.module.msg.constant.MsgRule;

public class MessageHelper {

	private static ConcurrentMap<Long, Integer> msgIntervalTimeController = new ConcurrentHashMap<>();
	
	/**
	 * 检查与上次发消息之间的时间间隔是否过低
	 * @param actorId
	 * @return
	 */
	public static boolean checkIntervalTime(long actorId) {
		if (msgIntervalTimeController.containsKey(actorId)) {
			if (DateUtils.getNowInSecondes() - msgIntervalTimeController.get(actorId) < MsgRule.MSG_INTERVAL_TIME_LIMIT) {
				return false;
			}
		}
		msgIntervalTimeController.put(actorId, DateUtils.getNowInSecondes());
		return true;
	}
	
	public static boolean isValid(String msg) {
		// 过滤emoji表情
		msg = EmojiFilter.filterEmoji(msg);

		if (msg == null || msg.equals("")) {
			return false;
		}

		if (msg.length() > ChatRule.MSG_LENGTH_LIMIT) {
			return false;
		}
		return true;
	}
	
	public static boolean isForbidden(int forbiddenTime, int unforbiddenTime) {
		if (unforbiddenTime == 0) {
			return false;
		}
		int now = TimeUtils.getNow();
		if (forbiddenTime < now && unforbiddenTime > now) {
			return true;
		}
		return false;
	}
}
