package com.jtang.gameserver.server.session;

import static com.jiatang.common.GameStatusCodeConstant.USER_RECONNECT_ID_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.USER_RECONNECT_TIME_OUT;
import static com.jtang.core.protocol.StatusCode.SERVER_VERSION_ERROR;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

import javax.annotation.PostConstruct;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.delay.DelayedSession;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.module.user.platform.PlatformLoginResult;
import com.jtang.gameserver.server.firewall.FloodRecord;

/**
 * 玩家Session管理类
 * 
 * @author 0x737263
 * 
 */
@Component
public class PlayerSession {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlayerSession.class);

	/**
	 * 匿名Session
	 * key:sessionId value:IoSession
	 */
	private static final ConcurrentHashMap<Long, IoSession> ANONYMOUS_MAP = new ConcurrentHashMap<Long, IoSession>();

	/**
	 * 在线角色Session
	 * key:actorId value:IoSession
	 */
	private static final ConcurrentHashMap<Long, IoSession> ACTORID_MAP = new ConcurrentHashMap<Long, IoSession>();

	/**
	 * 延迟关闭session队列
	 */
	private static final DelayQueue<DelayedSession> SESSION_CLOSE_QUEUES = new DelayQueue<DelayedSession>();
	
	/**
	 * 用户数据 key：reconnectId
	 */
	private static final Map<String, UserReconnectData> USER_LOGIN_DATA = new com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap.Builder<String, UserReconnectData>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();
		
	/**
	 * 最大在线数
	 */
	private int maxOnlineCount = 0;

	/**
	 * 最小大线数
	 */
	private int minOnlineCount = 0;
	
	/**
	 * 获取在线玩家最大数量
	 * @return
	 */
	public int getMaxOnlineCount() {
		return this.maxOnlineCount;
	}

	/**
	 * 获取在线玩家最小数量
	 * @return
	 */
	public int getMinOnlineCount() {
		return this.minOnlineCount;
	}
	
	@PostConstruct
	private void initialize() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (true)
					try {
						DelayedSession take = (DelayedSession) SESSION_CLOSE_QUEUES.take();
						if (take != null) {							
							take.getSession().close(false);
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug(String.format("延迟关闭sessionId:[%s] ", take.getSession().getId()));
							}
						}
					} catch (InterruptedException e) {
						LOGGER.error("阻塞延迟队列异常: {}", e);
					}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * 延迟关闭Session
	 * @param session		当前session
	 * @param delaySecond	延迟秒
	 */
	public void delayCloseSession(IoSession session, int delaySecond) {
		if (delaySecond < 0 || session == null) {
			return;
		}
		DelayedSession element = new DelayedSession(session, new Date(), delaySecond);
		if (!SESSION_CLOSE_QUEUES.contains(element)) {
			SESSION_CLOSE_QUEUES.add(element);
		}
	}
	
	/**
	 * 发送状态码
	 * @param session		当前session
	 * @param statusCode	状态码
	 * @param isCloseSession	是否关闭session
	 */
	public void writeStatusCode(IoSession session, short statusCode, boolean isCloseSession) {
		if (session == null) {
			return;
		}
		Response response = new Response();
		response.setStatusCode(statusCode);
		session.write(response);

		if (isCloseSession) {
			closeIoSession(session, false);
		}
	}

	/**
	 * 角色加入在线列表
	 * @param session
	 * @param actorId
	 * @param kickOrignSession	是否删除在这之前actroId相同的session
	 */
	public void put2OnlineList(IoSession session, long actorId,boolean kickOrignSession) {
		if (session == null) {
			return;
		}

		long sessionId = session.getId();
		if (ANONYMOUS_MAP.containsKey(sessionId)) {
			ANONYMOUS_MAP.remove(sessionId);
		}
		
		if(kickOrignSession) {
			IoSession orignSession = ACTORID_MAP.put(actorId, session);
			if (orignSession != null && orignSession.isConnected()) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(String.format("actorid:[%d] 加入在线用户列表前关闭之前的Session", actorId));
				}
				orignSession.close(true);
			}			
		} else {
			ACTORID_MAP.remove(actorId);
			ACTORID_MAP.put(actorId, session);			
		}
		
		setActorId(session, actorId);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("session:[%d] bind actorid:[ %d ] ", session.getId(), actorId));
		}
		
		int onlineCount = onlineActorCount();
		if (this.maxOnlineCount < onlineCount) {
			this.maxOnlineCount = onlineCount;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("actorid:[%d] join online session list", actorId));
		}
	}

	/**
	 * 移除在线玩家
	 * @param actorId
	 */
	public void removeOnlineList(long actorId) {
		IoSession session = ACTORID_MAP.remove(actorId);
		if (session == null) {
			return;
		}

		if (session.isConnected()) {
			ANONYMOUS_MAP.put(session.getId(), session);
		}
		
		int onlineCount = onlineActorCount();
		if (this.minOnlineCount > onlineCount) {
			this.minOnlineCount = onlineCount;
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("remove actorId:[%d] in online session list", actorId));
		}
	}

	/**
	 * 移除在线玩家
	 * @param session
	 */
	public void removeFromOnlineList(IoSession session) {
		Long playerId = getActorId(session);
		IoSession oldSession = getOnlineSession(playerId.longValue());

		if ((oldSession != null) && (session == oldSession)) {
			removeOnlineList(playerId.longValue());
		}
	}

	/**
	 * 添加匿名玩家
	 * @param session
	 */
	public void put2AnonymousList(IoSession session) {
		if (session != null) {
			ANONYMOUS_MAP.put(session.getId(), session);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("session:[%d] join anonymous session list", session.getId()));
			}
		}
	}

	/**
	 * 移除匿名玩家
	 * @param session
	 */
	public void removeFromAnonymousList(IoSession session) {
		if (session != null) {
			if (ANONYMOUS_MAP.containsKey(session.getId())) {
				ANONYMOUS_MAP.remove(session.getId());
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(String.format("remove session:[%d] in anonymous session list", session.getId()));
				}
			}
		}
	}

	/**
	 * 角色是否在线
	 * @param actorId
	 * @return
	 */
	public boolean isOnline(long actorId) {
		return ACTORID_MAP.containsKey(actorId);
	}
	
	/**
	 * 角色是否在线
	 * @param session
	 * @return
	 */
	public boolean isOnline(IoSession session) {
		long actorId = getActorId(session);
		if(actorId < 1L) {
			return false;
		}
		
		return ACTORID_MAP.containsKey(actorId);
	}

	/**
	 * 获取在线玩家playerId列表
	 * @return
	 */
	public Set<Long> onlineActorList() {		
		return ACTORID_MAP.keySet();
	}

	/**
	 * 获取在线玩家总数
	 * @return
	 */
	public int onlineActorCount() {
		return ACTORID_MAP.size();
	}

	/**
	 * 获取在线玩家的IoSession
	 * @param actorId
	 * @return
	 */
	public IoSession getOnlineSession(long actorId) {
		return ACTORID_MAP.get(actorId);
	}


	/**
	 * 获取匿名玩家Session列表
	 * @return
	 */
	public List<IoSession> anonymousSessionList() {
		return (List<IoSession>)ANONYMOUS_MAP.values();
	}


	/**
	 * 广播消息给单个用户
	 * 
	 * @param actorId
	 * @param response
	 */
	public void push(long actorId, Response response) {
		IoSession session = getOnlineSession(actorId);
		push(session, response);
	}

	/**
	 * 推送消息给用户列表
	 * 
	 * @param actorIdList
	 * @param response
	 */
	public void push(Collection<Long> actorIdList, Response response) {
		if ((actorIdList == null) || (actorIdList.isEmpty())) {
			return;
		}

		for (Long actorId : actorIdList) {
			IoSession session = getOnlineSession(actorId);
			if (session != null) {
				push(session, response);					
			}
		}
	}
	
	/**
	 * 推送消息给当前Session玩家
	 * @param session
	 * @param response
	 */
	public void push(IoSession session, Response response) {
		if (session == null) {
			return;
		}

		if (session.isConnected()) {
			if (response != null) {
				session.write(response);
			}
		} else {
			if (LOGGER.isDebugEnabled()) {
				Long actorId = getActorId(session);
				LOGGER.debug(String.format("braodcast to actorid:[%d] fail. session not found.", actorId));
			}
			closeIoSession(session, false);
		}
	}

	/**
	 * 推送消息给所有在线玩家
	 * @param response
	 */
	public void pushAllOnline(Response response) {
		push(ACTORID_MAP.keySet(), response);
	}
	
	/**
	 * 关闭当前Session
	 * @param session
	 * @param immediately	是否立即关闭
	 */
	public void closeIoSession(IoSession session, boolean immediately) {
		if (session != null) {
			session.close(immediately);
		}
	}
	
	/**
	 * 获取actorId
	 * @param session 当前用户session对象
	 * @return
	 */
	public Long getActorId(IoSession session) {
		Long actorId = null;
		if (session != null) {
			actorId = (Long)session.getAttribute(SessionKey.ACTOR_ID);
		}
		return actorId == null ? 0L : actorId;
	}
	
	/**
	 * 设置ACTOR_ID
	 * @param session
	 * @param actorId
	 */
	private void setActorId(IoSession session, Long actorId) {
		session.setAttribute(SessionKey.ACTOR_ID, actorId);
	}

	/**
	 * 帐号是否已登陆
	 * @param session
	 * @return
	 */
	public boolean userIsLogin(IoSession session) {
		int platformType = getPlatformType(session);
		String uid = getUid(session);
		if(platformType < 1 || uid == null || uid.isEmpty()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 设置用户登陆信息
	 * @param session
	 * @param platformType
	 * @param uid
	 */
	public void setUserLogin(IoSession session, int platformType,String uid) {
		if (platformType > 0 && uid != null && uid.isEmpty() == false) {
			session.setAttribute(SessionKey.PLATFORM_TYPE, platformType);
			session.setAttribute(SessionKey.PLATFORM_UID, uid);
			
		}
	}
	
	public String setLoginData(int platformType,String uid, PlatformLoginResult platformLoginResult) {
		UserReconnectData userLoginData = new UserReconnectData(platformType, platformLoginResult);
		USER_LOGIN_DATA.put(userLoginData.getReconnectId(), userLoginData);
		return userLoginData.getReconnectId();
	}
	
	
	/**
	 * 获取远程ip
	 * @param session 当前session
	 * @return
	 */
	public String getRemoteIp(IoSession session) {
		if (session == null) {
			return "";
		}

		String remoteIp = (String)session.getAttribute(SessionKey.REMOTE_HOST);	
		if (isBlank(remoteIp) == false) {
			return remoteIp;
		}

		SocketAddress add = session.getRemoteAddress();
		if(add != null) {
			remoteIp = ((InetSocketAddress) add).getAddress().getHostAddress();
		}
		if (isBlank(remoteIp)) { // StringUtils.isBlank(remoteIp)
			remoteIp = ((InetSocketAddress) session.getLocalAddress()).getAddress().getHostAddress();
		}
		session.setAttribute(SessionKey.REMOTE_HOST, remoteIp);
		
		return remoteIp;
	}

	private static Boolean isBlank(String s) {
		if (s == null || s.isEmpty())
			return true;
		return false;
	}
	
	/**
	 * 获取创建Session时，分配的唯一自增id
	 * @param session
	 * @return
	 */
	public Integer getAtomicId(IoSession session) {
		return (Integer) session.getAttribute(SessionKey.ATOMIC_ID);
	}
	
	/**
	 * 设置自增id
	 * @param session
	 * @param atomicId
	 */
	public void setAtomicId(IoSession session,Integer atomicId) {
		session.setAttribute(SessionKey.ATOMIC_ID, atomicId);
	}
		
	/**
	 * 获取是否第一次请求
	 * @param session
	 * @return
	 */
	public Boolean getFirstRequest(IoSession session) {
		Boolean firstRequest = (Boolean)session.getAttribute(SessionKey.FIRST_REQUEST);
		if(firstRequest == null) {
			firstRequest = true;
			setFirstRequest(session,firstRequest);
		}
		return firstRequest;
	}
	
	/**
	 * 设置是否第一次请求
	 * @param session
	 * @param firstRequest
	 */
	public void setFirstRequest(IoSession session,Boolean firstRequest) {
		session.setAttribute(SessionKey.FIRST_REQUEST, firstRequest);
	}
	
	/**
	 * 获取洪水记录对象
	 * @param session
	 * @return
	 */
	public FloodRecord getFloodRecord(IoSession session) {
		return (FloodRecord)session.getAttribute(SessionKey.FLOOD_RECORD);
	}
	
	/**
	 * 设置洪水记录对象
	 * @param session
	 * @param floodRecord
	 */
	public void setFloodRecord(IoSession session,FloodRecord floodRecord) {
		session.setAttribute(SessionKey.FLOOD_RECORD, floodRecord);
	}
		
	/**
	 * 获取平台Id
	 * @param session
	 */
	public int getPlatformType(IoSession session) {
		Object type = session.getAttribute(SessionKey.PLATFORM_TYPE);
		if(type == null) {
			return 0;
		}
				
		return (int)type;
	}

	/**
	 * 获取token
	 * @param session
	 * @return
	 */
	public String getUid(IoSession session) {
		String uid = (String)session.getAttribute(SessionKey.PLATFORM_UID);
		if(uid == null) {
			return "";
		}
		return uid;
	}
	
	/**
	 * 获取角色登陆的游戏服Id
	 * @param session
	 */
	public int getServerId(IoSession session) {
		return (int)session.getAttribute(SessionKey.SERVER_ID);
	}
	
	/**
	 * 设置角色登陆的游戏服Id
	 * @param session
	 * @param token
	 */
	public void setServerId(IoSession session,int serverId) {
		session.setAttribute(SessionKey.SERVER_ID, serverId);
	}	
	
	/**
	 * 验证重新登录
	 * @param session
	 * @return
	 */
	public TResult<UserReconnectData> validateReconnect(String reconnectId, String version) {
		if (Game.getVersion().isEmpty() || version.equals(Game.getVersion()) == false) {
			TResult.valueOf(SERVER_VERSION_ERROR);
		}
		if (StringUtils.isBlank(reconnectId) || !USER_LOGIN_DATA.containsKey(reconnectId)) {
			return TResult.valueOf(USER_RECONNECT_ID_ERROR);
		} 
		UserReconnectData userLoginData = USER_LOGIN_DATA.get(reconnectId);
		if (userLoginData.isTimeOut()) {
			USER_LOGIN_DATA.remove(reconnectId);
			return TResult.valueOf(USER_RECONNECT_TIME_OUT);
		}
		
		if (userLoginData.getReconnectId().equals(reconnectId) == false) {
			return TResult.valueOf(USER_RECONNECT_ID_ERROR);
		}
		userLoginData.setTime(TimeUtils.getNow());
		return TResult.sucess(userLoginData);
	}

} 