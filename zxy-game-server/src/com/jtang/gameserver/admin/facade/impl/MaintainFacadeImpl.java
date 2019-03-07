package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.core.protocol.StatusCode.DATA_VALUE_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.DATA_CONFIG_FLUSH_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.DATA_CONFIG_NAME_NOT_NULL;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.DATA_CONFIG_NOT_NULL;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.MAINTAIN_KICK_ACTOR_OFFLINE;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.NOT_SUPPORT_TABLE;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jtang.core.cache.AbstractCacheListener;
import com.jtang.core.context.SpringContext;
import com.jtang.core.dataconfig.DataConfig;
import com.jtang.core.dataconfig.ReloadConfig;
import com.jtang.core.db.DBQueue;
import com.jtang.core.db.Entity;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.admin.GameAdminStatusCodeConstant;
import com.jtang.gameserver.admin.facade.MaintainFacade;
import com.jtang.gameserver.admin.model.PollingNotice;
import com.jtang.gameserver.admin.type.MaintainState;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.module.equip.dao.EquipDao;
import com.jtang.gameserver.module.equip.dao.impl.EquipDaoImpl;
import com.jtang.gameserver.module.goods.dao.GoodsDao;
import com.jtang.gameserver.module.goods.dao.impl.GoodsDaoImpl;
import com.jtang.gameserver.module.notice.facade.NoticeFacade;
import com.jtang.gameserver.module.notice.helper.NoticePushHelper;
import com.jtang.gameserver.module.notice.model.NoticeVO;
import com.jtang.gameserver.module.notice.type.NoticeType;
import com.jtang.gameserver.module.user.dao.ActorDao;
import com.jtang.gameserver.module.user.dao.impl.ActorDaoImpl;
import com.jtang.gameserver.module.user.helper.UserPushHelper;
import com.jtang.gameserver.module.user.type.KickOffType;
import com.jtang.gameserver.server.GatewaySocketServer;
import com.jtang.gameserver.server.session.PlayerSession;

//import org.springframework.context.support.AbstractApplicationContext;

@Component
public class MaintainFacadeImpl implements MaintainFacade, ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MaintainFacadeImpl.class);
	@Autowired
	private PlayerSession playerSession;

	@Autowired
	private NoticeFacade noticeFacade;

	@Autowired
	private Schedule schedule;
	
	@Autowired
	private AbstractCacheListener cacheListener;

	@Autowired
	private DBQueue dbQueue;

	@Autowired
	private DataConfig dataConfig;

	@Autowired
	private ReloadConfig reloadConfig;

	// private final static int CLOSE_SERVER_TIME = 1 * 60;//1分钟

	private final List<PollingNotice> notices = new ArrayList<>();
	
	
	@Override
	public void delayKickActor(int time) {
		if (time > 0) {
			schedule.addDelaySeconds(new Runnable() {

				@Override
				public void run() {
					kickAllActor();
				}
			}, time);
		} else {
			kickAllActor();
		}
	}

	/**
	 * 立即踢人
	 */
	private void kickAllActor() {
		Set<Long> actors = playerSession.onlineActorList();
		for (long actorId : actors) {
			UserPushHelper.kickOff(actorId, KickOffType.SERVER_MAINTENANCE);
		}
		LOGGER.info("all actor kick complete...");
	}

	// /**
	// * 关闭服务器
	// * @param time 延迟时间（用于踢人时间）
	// */
	// private void closeServer(int time){
	// LOGGER.info("close server start....");
	// //延迟CLOSE_SERVER_TIME分钟关闭
	// schedule.addDelaySeconds(new Runnable() {
	//
	// @Override
	// public void run() {
	// AbstractApplicationContext ac =
	// (AbstractApplicationContext)applicationContext;
	// ac.close();
	// System.exit(0);
	// }
	// }, CLOSE_SERVER_TIME + time);// 踢人时间与关机延迟时间
	// }

	@Override
	public Result changeServerState(byte flag, int time) {
		MaintainState maintainState = MaintainState.getByType(flag);
		if (maintainState == null) {
			return Result.valueOf(GameAdminStatusCodeConstant.MAINTAIN_OPEN_CLOSE_SERVER_ARG_ERROR);
		}
		Game.maintain = maintainState;// 0.正常状态（所有人可访问）
										// 1.维护状态（允许ip列表访问），2,关闭状态（所有人不可访问）
		if (Game.maintain.equals(MaintainState.MAINTAIN)) {// 延迟踢出用户，允许ip列表访问
			delayKickActor(time);
		} else if (Game.maintain.equals(MaintainState.CLOSE)) {// 所有人不可访问
			delayKickActor(time);
			// closeServer(time);
		}
		GameOssLogger.reflushLogger();
		LOGGER.warn(String.format("服务器状态：0.正常状态（所有人可访问） 1.维护状态（允许ip列表访问），2,关闭状态（所有人不可访问），当前服务器状态:[%s]", Game.maintain));

		return Result.valueOf();
	}

	// @Override
	// public Result sendNotice(String message) {
	// if (message == null || message.isEmpty()) {
	// Result.valueOf(AdminStatusCodeConstant.MAINTAIN_SEND_NOTICE_ARG_ERROR);
	// }
	// noticeFacade.boadCastSystemNotice(message);
	// return Result.valueOf();
	// }

	@Override
	public Result sendNotice(String message, int pollingNum, int delayTime, List<Integer> channelIds) {
		if (message == null || message.isEmpty() || pollingNum < 1 || delayTime < 1) {
			Result.valueOf(GameAdminStatusCodeConstant.MAINTAIN_SEND_NOTICE_ARG_ERROR);
		}
		PollingNotice pollingNotice = new PollingNotice(message, pollingNum, delayTime, channelIds);
		notices.add(pollingNotice);
		return Result.valueOf();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(pollingTask(), 1);
	}

	private Runnable pollingTask() {
		return new Runnable() {

			@Override
			public void run() {
				Iterator<PollingNotice> iterator = notices.iterator();
				while (iterator.hasNext()) {
					PollingNotice pollingNotice = iterator.next();
					if (pollingNotice.isTimeOut()) {
						int times = pollingNotice.polling();
						if (times == 0) {
							iterator.remove();
						}
						pollingNotice.setLastPollingTime(TimeUtils.getNow());
						String message = pollingNotice.getMessage();
						if (message == null || message.isEmpty()) {
							Result.valueOf(GameAdminStatusCodeConstant.MAINTAIN_SEND_NOTICE_ARG_ERROR);
						}
						NoticeVO noticeVO = NoticeVO.valueOf(NoticeType.SYSTEM_NOTICE, pollingNotice.getMessage());
						NoticePushHelper.broadcastNotice(pollingNotice.getChannelIds(), noticeVO);

						// sendNotice(pollingNotice.getMessage());
					}

				}
			}
		};
	}

	@Override
	public int getOnlinePlayerNum() {
		return playerSession.onlineActorCount();
	}

	@Override
	public int getHistorMinPlayerNum() {
		return playerSession.getMinOnlineCount();
	}

	@Override
	public int getHistorMaxPlayerNum() {
		return playerSession.getMaxOnlineCount();
	}

	@Override
	public Result shutdownServer() {
		LOGGER.info("shutdown server.....");
		Game.maintain = MaintainState.CLOSE;
		dbQueue.changeBlockTime(1000);
		delayKickActor(0);
		GameOssLogger.reflushLogger();
		long now = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - now > 30000) {
				break;
			}
			int count = playerSession.onlineActorCount();
			if (count == 0) {
				break;
			}
		}
		GatewaySocketServer.getInstance().stop();
		schedule.addDelaySeconds(new Runnable() {

			@Override
			public void run() {
				System.exit(0);
			}
		}, 5);
		return Result.valueOf();
	}

	@Override
	public Result kickPlayr(long actorId) {
		IoSession session = playerSession.getOnlineSession(actorId);
		if (session != null) {
			UserPushHelper.kickOff(actorId, KickOffType.USER_BLOCK);
			return Result.valueOf();
		} else {
			return Result.valueOf(MAINTAIN_KICK_ACTOR_OFFLINE);
		}
	}

	@Override
	public Result flushDataConfig(String fileName, String data) {
		if (fileName == null || fileName.isEmpty()) {
			return Result.valueOf(DATA_CONFIG_NAME_NOT_NULL);
		}
		if (data == null || data.isEmpty()) {
			return Result.valueOf(DATA_CONFIG_NOT_NULL);
		}
		LOGGER.info("flush dataConfig:[" + fileName + "]");
		boolean result = dataConfig.checkModelAdapter(fileName, new ByteArrayInputStream(data.getBytes()));
		result = reloadConfig.flushFile(fileName, data);
		if (!result) {
			LOGGER.error("reload file check error..");
			return Result.valueOf(DATA_CONFIG_FLUSH_ERROR);
		} else {
			LOGGER.info("reload file check success..");
		}
		return Result.valueOf();
	}

	@Override
	public TResult<String> entity2JSON(String tableName, long actorId) {
		if (StringUtils.isBlank(tableName)) {
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		String result = "";
		tableName = tableName.toLowerCase().trim();
		Entity<?> entity = null;
		
		switch (tableName) {
		case "actor": {
			ActorDao dao = (ActorDao) SpringContext.getBean(ActorDaoImpl.class);
			entity = dao.getActor(actorId);
			break;
		}
		case "goods": {
			GoodsDao dao = (GoodsDao) SpringContext.getBean(GoodsDaoImpl.class);
			entity = dao.get(actorId);
			break;
		}
		case "equips": {
			EquipDao dao = (EquipDao) SpringContext.getBean(EquipDaoImpl.class);
			entity = dao.get(actorId);
			break;
		}

		default:
			return TResult.valueOf(NOT_SUPPORT_TABLE);
		}
		
		if (entity != null) {
			result = JSON.toJSONString(entity);
		}
		return TResult.sucess(result);
	}

	@Override
	public Result addUid(String uid) {
		if(Game.getDebugUids().contains(uid)){
			return Result.valueOf();
		}
		Game.addDebugUids(uid);
		return Result.valueOf();
	}

	@Override
	public Result cleanUid() {
		Game.getDebugUids().clear();
		return Result.valueOf();
	}
	

	@Override
	public Result clearDBEntityCache(long actorId) {
		cacheListener.clearSpecifyActorId(actorId);
		return Result.valueOf();
	}

}
