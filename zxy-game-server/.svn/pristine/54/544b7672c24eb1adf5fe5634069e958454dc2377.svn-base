package com.jtang.gameserver.module.msg.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Message;
import com.jtang.gameserver.module.msg.constant.MsgRule;
import com.jtang.gameserver.module.msg.dao.MsgDao;

@Repository
public class MsgDaoImpl implements MsgDao, CacheListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(MsgDaoImpl.class);
	
	@Autowired
	IdTableJdbc jdbc;
	
	@Autowired
	DBQueue dbQueue;
	
	/**
	 * 角色列表.key:接收者actorId value:List<Message>
	 */
	private static ConcurrentLinkedHashMap<Long, List<Message>> MSG_MAP = new ConcurrentLinkedHashMap.Builder<Long, List<Message>>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Message createMsg(long fromActorId, long toActorId, String content, int sendTime) {
		List<Message> msgList = get(toActorId);
		Message msg = Message.valueOf(fromActorId, toActorId, content, sendTime);
		long msgMId = jdbc.save(msg);
		msg.setPkId(msgMId);
		msgList.add(msg);
		return msg;
	}

	@Override
	public List<Message> get(long toActorId) {
		if (MSG_MAP.containsKey(toActorId)) {
			return MSG_MAP.get(toActorId);
		}

		LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("toActorId", toActorId);
		List<Message> msgList = jdbc.getList(Message.class, condition);
		if (msgList == null) {
			msgList = new ArrayList<>();
		}
		MSG_MAP.put(toActorId, msgList);
		return msgList;
	}

	@Override
	public List<Long> removeOldMessage(long toActorId, long mId) {
		List<Long> deletedMsgList = null;
		List<Message> msgList = get(toActorId);
		if(msgList.size() > MsgRule.MSG_MAX_NUM_LIMIT){//则删除最先创建的消息
			deletedMsgList = new ArrayList<>();
			try{
				while(msgList.size() > MsgRule.MSG_MAX_NUM_LIMIT){
					long min = mId;
					Message deletedMsg = null;
					for(Message message : msgList){
						if(min > message.getPkId()){//pkId越小则创建时间越早
							deletedMsg = message;
							min = message.getPkId();
						}
					}
					if(deletedMsg != null){
						deletedMsgList.add(min);
						msgList.remove(deletedMsg);
						jdbc.delete(deletedMsg);
					}
				}
			}
			catch(Exception e){
				LOGGER.error("remove old message", e);
			}
			
		}
		return deletedMsgList;
	}

	@Override
	public void setReaded(long actorId) {
		List<Message> updateList = new ArrayList<>();
		List<Message> msgList = get(actorId);
		try {
			for (Message msg : msgList) {
				if (msg.isReaded == 0) {
					msg.isReaded = 1;
					updateList.add(msg);
				}
			}
			jdbc.update(updateList);
		} catch (Exception e) {
			LOGGER.error("set message as readed", e);
		}
	}

	@Override
	public void remove(long actorId, List<Long> mIdList) {
		List<Message> removeList = get(actorId);
		try {
			Iterator<Message> iterator = removeList.iterator();
			while (iterator.hasNext()) {
				Message msg = iterator.next();
				if (mIdList.contains(msg.getPkId())) {
					iterator.remove();
					jdbc.delete(msg);
				}
			}
		} catch (Exception e) {
			LOGGER.error("remove message", e);
		}
	}

	@Override
	public int cleanCache(long actorId) {
		MSG_MAP.remove(actorId);
		return MSG_MAP.size();
	}

	@Override
	public List<Message> getSendMsg(long fromActorId) {
		LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("fromActorId", fromActorId);
		List<Message> msgList = jdbc.getList(Message.class, condition);
		if (msgList == null) {
			msgList = new ArrayList<>();
		}
		return msgList;
	}
}
