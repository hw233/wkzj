package com.jtang.gameserver.server.firewall;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.gameserver.server.session.PlayerSession;


/**
 * 防火墙配置类
 * @author 0x737263
 *
 */
@Component
public class Firewall {
	private static final Log LOGGER = LogFactory.getLog(Firewall.class);
	
	/**
	 * 当前玩家session
	 */
	@Autowired
	public PlayerSession playerSession;

	/**
	 * 防火墙配置参数
	 */
	@Autowired
	public FirewallConfig config;
	
	@PostConstruct
	private void init() {
		if (config.enableFirewall) {
			LOGGER.info(String.format("firewall enabled! max connection:[%d]", config.maxClientsLimit));
		}
	}

	/**
	 * AtomicInteger 一种线程安全的加减操作接口用于给session分配自增编号
	 */
	private static final AtomicInteger ATOMIC_ID = new AtomicInteger();

	/**
	 * ip黑名单列表
	 */
	private static ConcurrentHashMap<String, Long> BLOCKED_IPS = new ConcurrentHashMap<String, Long>(1);

	/**
	 * 玩家黑名单列表
	 */
	private static ConcurrentHashMap<Long, Long> BLOCKED_PLAYER_IDS = new ConcurrentHashMap<Long, Long>(1);

	/**
	 * 可疑ip列表
	 */
	private static ConcurrentHashMap<String, AtomicInteger> SUSPICIOUS_IPS = new ConcurrentHashMap<String, AtomicInteger>(1);

	/**
	 * 可疑玩家列表
	 */
	private static ConcurrentHashMap<Long, AtomicInteger> SUSPICIOUS_PLAYERIDS = new ConcurrentHashMap<Long, AtomicInteger>(1);

	public int getClients() {
		return ATOMIC_ID.get();
	}

	/**
	 * 当前值加1
	 * @return
	 */
	public int increaseClients() {
		return ATOMIC_ID.incrementAndGet();
	}

	/**
	 * 当前值减1
	 * @return
	 */
	public int decreaseClients() {
		return ATOMIC_ID.decrementAndGet();
	}

	public boolean isMaxClientLimit(int currClients) {
		return currClients > config.maxClientsLimit;
	}

	public boolean isBlocked(IoSession session) {
		return isIpBlock(session) || isPlayerIdBlock(session);
	}

	private boolean isIpBlock(IoSession session) {
		String remoteIp = playerSession.getRemoteIp(session);
		if (remoteIp == null || remoteIp.isEmpty()) {
			return false;
		}

		Long blockedTime = (Long) BLOCKED_IPS.get(remoteIp);
		if (blockedTime == null) {
			return false;
		}

		if (blockedTime.longValue() <= System.currentTimeMillis()) {
			BLOCKED_IPS.remove(remoteIp);
			return false;
		}
		return true;
	}

	private boolean isPlayerIdBlock(IoSession session) {
		long actorId = playerSession.getActorId(session);
		if (actorId <= 0) {
			return false;
		}
		
		Long blockedTime = BLOCKED_PLAYER_IDS.get(actorId);
		if (blockedTime == null) {
			return false;
		}

		if (blockedTime.longValue() <= System.currentTimeMillis()) {
			BLOCKED_PLAYER_IDS.remove(actorId);
			return false;
		}
		return true;
	}

	public void blockIp(String ip) {
		long currentTimeMillis = System.currentTimeMillis();
		int blockIpMillis = getBlockIpMinutesOfMilliSecond();
		BLOCKED_IPS.put(ip, Long.valueOf(currentTimeMillis + blockIpMillis));
	}

	public void unblockIp(String remoteIp) {
		BLOCKED_IPS.remove(remoteIp);
	}

	public void blockPlayer(long playerId) {
		long currTime = System.currentTimeMillis();
		int blockUserTime = getBlockMinutesOfMilliSecond();
		BLOCKED_PLAYER_IDS.put(Long.valueOf(playerId), Long.valueOf(currTime + blockUserTime));
	}

	public void unblockPlayer(long playerId) {
		BLOCKED_PLAYER_IDS.remove(Long.valueOf(playerId));
	}

	public boolean blockedByBytes(IoSession session, int bytes) {
		return checkBlock(session, FirewallType.BYTE, bytes);
	}

	public boolean blockedByPacks(IoSession session, int packs) {
		return checkBlock(session, FirewallType.PACK, packs);
	}

	public boolean blockedByAuthCodeErrors(IoSession session, int errors) {
		return checkBlock(session, FirewallType.AUTHCODE, errors);
	}
	
	public boolean isEnableFirewall() {
		return this.config.enableFirewall != null && this.config.enableFirewall == true;
	}

	/**
	 * 收包检查
	 * @param session
	 * @param type
	 * @param amount
	 * @return
	 */
	private boolean checkBlock(IoSession session, FirewallType type, int amount) {
		if (session == null) {
			return false;
		}

		if (isBlocked(session)) {
			return true;
		}

		if (amount <= 0) {
			return false;
		}

		FloodRecord floodRecord = playerSession.getFloodRecord(session);
		if (floodRecord == null) {
			floodRecord = new FloodRecord();
			playerSession.setFloodRecord(session, floodRecord);
		}

		boolean suspicious = false;
		if (type == FirewallType.BYTE)
			suspicious = avalidateWithBytes(amount, floodRecord);
		else if (type == FirewallType.PACK)
			suspicious = avalidateWithPackages(amount, floodRecord);
		else if (type == FirewallType.AUTHCODE) {
			suspicious = avalidateWithAuthcode(amount, floodRecord);
		}

		boolean isBlack = false;
		if (suspicious) {
			String remoteIp = playerSession.getRemoteIp(session);
			Long playerId = playerSession.getActorId(session);
			
			//没登陆则进行停封ip处理
			if (playerId.longValue() <= 0L) {
				AtomicInteger blocks = (AtomicInteger) SUSPICIOUS_IPS.get(remoteIp);
				if (blocks == null) {
					SUSPICIOUS_IPS.put(remoteIp, new AtomicInteger());
					blocks = (AtomicInteger) SUSPICIOUS_IPS.get(remoteIp);
				}

				if (blocks.incrementAndGet() >= config.blockDetectCount) {
					blocks.set(0);
					isBlack = true;
					blockIp(remoteIp);
				}
			} else {
				AtomicInteger blocks = (AtomicInteger) SUSPICIOUS_PLAYERIDS.get(playerId);
				if (blocks == null) {
					SUSPICIOUS_PLAYERIDS.putIfAbsent(playerId, new AtomicInteger());
					blocks = (AtomicInteger) SUSPICIOUS_PLAYERIDS.get(playerId);
				}

				if (blocks.incrementAndGet() >= config.blockDetectCount) {
					blocks.set(0);
					isBlack = true;
					blockPlayer(playerId.longValue());
				}
			}

			LOGGER.warn(String.format("{%s}", floodRecord.toString()));
			LOGGER.warn(String.format("ip: %s, playerId: %d, block: %s", remoteIp, playerId.longValue(), String.valueOf(isBlack)));
		}

		return isBlack;
	}

	/**
	 * 
	 * @param session
	 */
	public void removeBlockCounter(IoSession session) {
		if (session != null) {
			try {
				SUSPICIOUS_IPS.remove(playerSession.getRemoteIp(session));
				SUSPICIOUS_PLAYERIDS.remove(playerSession.getActorId(session));
			} catch (Exception e) {
				LOGGER.error("{}", e);
			}
		}
	}

	private boolean avalidateWithAuthcode(int amount, FloodRecord floodCheck) {
		long currentMillis = System.currentTimeMillis();
		long currMinue = currentMillis / 60000L;
		long lastMin = floodCheck.getLastAuthCodeTime() / 60000L;
		floodCheck.setLastAuthCodeTime(currentMillis);
		if (lastMin == currMinue) {
			floodCheck.addLastMinuteAuthCodes(amount);
		} else {
			floodCheck.setLastMinuteAuthCodes(amount);
		}

		int lastMinuteAuthCodes = floodCheck.getLastMinuteAuthCodes();
		if (lastMinuteAuthCodes >= config.maxAuthCodeErrorsPerMinute.intValue()) {
			floodCheck.setLastMinuteAuthCodes(0);
			LOGGER.error(String.format("AuthCode errors overflow: lastMinuteAuthCodes[%d], maxAuthCodeErrorsPerMinute[%d]", lastMinuteAuthCodes,
					config.maxAuthCodeErrorsPerMinute));
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param amount
	 * @param floodCheck
	 * @return
	 */
	private boolean avalidateWithPackages(int amount, FloodRecord floodCheck) {
		long currentMillis = System.currentTimeMillis();
		long currMinue = currentMillis / 60L;
		long lastMin = floodCheck.getLastPackTime() / 60L;
		floodCheck.setLastPackTime(currentMillis);
		if (lastMin == currMinue) {
			floodCheck.addLastMinutePacks(amount);
		} else {
			floodCheck.setLastMinutePacks(amount);
		}

		int lastMinutePacks = floodCheck.getLastMinutePacks();
		if (lastMinutePacks >= config.maxPacksPerMinute.intValue()) {
			floodCheck.setLastMinutePacks(0);
			LOGGER.error(String.format("Packs overflow: lastMinutePacks[%d], maxPacksPerMinute[%d]", lastMinutePacks,config.maxPacksPerMinute));
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param amount
	 * @param floodCheck
	 * @return
	 */
	private boolean avalidateWithBytes(int amount, FloodRecord floodCheck) {
		long currentMillis = System.currentTimeMillis();
		long currMinue = currentMillis / 60000L;
		long lastMinue = floodCheck.getLastSizeTime() / 60000L;
		floodCheck.setLastSizeTime(currentMillis);
		if (lastMinue == currMinue) {
			floodCheck.addLastMinuteSizes(amount);
		} else {
			floodCheck.setLastMinuteSizes(amount);
		}

		int lastMinuteSizes = floodCheck.getLastMinuteSizes();
		if (lastMinuteSizes >= config.maxBytesPerMinute.intValue()) {
			floodCheck.setLastMinuteSizes(0);
			LOGGER.error(String.format("Bytes overflow: lastMinuteSizes[%d], maxBytesPerMinute[%d]", lastMinuteSizes,config.maxBytesPerMinute));
			return true;
		}

		return false;
	}

	public int getMaxPacksPerMinute() {
		return config.maxPacksPerMinute.intValue();
	}

	public int getMaxBytesPerMinute() {
		return config.maxBytesPerMinute.intValue();
	}

	public int getMaxAuthCodeErrorsPerMinute() {
		return config.maxAuthCodeErrorsPerMinute.intValue();
	}

	public int getBlockDetectCount() {
		return config.blockDetectCount.intValue();
	}

	public int getBlockIpMinutesOfMilliSecond() {
		return config.blockIpMinutes.intValue() * 60000;
	}

	public int getBlockMinutesOfMilliSecond() {
		return config.blockUserMinutes.intValue() * 60000;
	}

	public int getMaxClientsLimit() {
		return config.maxClientsLimit.intValue();
	}

	static enum FirewallType {
		PACK,

		BYTE,

		AUTHCODE;
	}
}