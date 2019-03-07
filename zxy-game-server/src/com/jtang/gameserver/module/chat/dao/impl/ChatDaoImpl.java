package com.jtang.gameserver.module.chat.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Chat;
import com.jtang.gameserver.module.chat.dao.ChatDao;

/**
 * ChatDao实现
 * @author lig
 *
 */
@Component
public class ChatDaoImpl implements ChatDao, CacheListener {

	/**
	 * 禁言列表 的缓存
	 * <pre>
	 * key:角色actorId  value:Chat
	 * </pre>
	 */
	private static ConcurrentLinkedHashMap<Long, Chat> CHAT_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Chat>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();

	@Autowired
	private DBQueue dbQueue;
	
	@Autowired
	private IdTableJdbc jdbcTemplate;
	
	
	@Override
	public Chat get(long actorId) {
		if (CHAT_MAPS.containsKey(actorId)) {
			return CHAT_MAPS.get(actorId);
		}
		Chat chat = jdbcTemplate.get(Chat.class, actorId);
		if (chat == null){
			chat = Chat.valueOf(actorId);
		}
		return chat;
	}

	@Override
	public void update(Chat updateChat) {
		dbQueue.updateQueue(updateChat);
	}

	@Override
	public int cleanCache(long actorId) {
		CHAT_MAPS.remove(actorId);
		return CHAT_MAPS.size();
	}
}
