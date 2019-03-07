package com.jtang.gameserver.server.broadcast;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import com.jtang.core.protocol.Response;

/**
 * 广播上下文
 * 
 * @author 0x737263
 * 
 */
public class BroadcastContext {
	
	/**
	 * 当前推送id
	 */
	private long pusherId;
	
	/**
	 * 响应给客户端的协议结构
	 */
	private Response response;
	
	/**
	 * 广播类型
	 */
	private BroadcastType type = BroadcastType.ALL;

	/**
	 * 推送角色对象列表
	 */
	private Collection<Long> actorIdList = null;

	/**
	 * pusherid自增分配
	 */
	private static final AtomicLong automicId = new AtomicLong();

	public Response getResponse() {
		return this.response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public BroadcastType getType() {
		return this.type;
	}

	public void setType(BroadcastType type) {
		this.type = type;
	}

	public Collection<Long> getActorIdList() {
		return this.actorIdList;
	}

	public void setActorIdList(Collection<Long> actorIdList) {
		this.actorIdList = actorIdList;
	}

	public long getPusherId() {
		return this.pusherId;
	}

	private static long getAtomicId() {
		if (automicId.get() >= 9223372036854775552L) {
			automicId.set(1L);
		}
		return automicId.getAndIncrement();
	}

	public static BroadcastContext push2AllOnline(Response response) {
		BroadcastContext context = new BroadcastContext();
		context.response = response;
		context.type = BroadcastType.ALL;
		context.pusherId = getAtomicId();
		return context;
	}

	public static BroadcastContext push2Actor(long actorId, Response response) {
		BroadcastContext context = new BroadcastContext();
		context.pusherId = actorId;
		context.response = response;
		context.type = BroadcastType.GROUP;
		context.actorIdList = Arrays.asList(actorId);
		return context;
	}

	public static BroadcastContext push2Actors(Collection<Long> actorIdList, Response response) {
		BroadcastContext context = new BroadcastContext();
		context.response = response;
		context.type = BroadcastType.GROUP;
		context.pusherId = getAtomicId();
		if (actorIdList != null) {
			context.actorIdList = actorIdList; 
		}
		return context;
	}
}