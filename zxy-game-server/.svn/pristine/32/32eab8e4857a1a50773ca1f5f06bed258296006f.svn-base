package com.jtang.gameserver.module.adventures.favor.facade.impl;
import static com.jiatang.common.GameStatusCodeConstant.*;

import java.util.ArrayList;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.GameEvent;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.BableTimesEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.component.event.SnatchResultEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.FavorRightConfig;
import com.jtang.gameserver.dataconfig.service.FavorRightService;
import com.jtang.gameserver.dbproxy.entity.Favor;
import com.jtang.gameserver.module.adventures.favor.constant.FavorRule;
import com.jtang.gameserver.module.adventures.favor.dao.FavorDao;
import com.jtang.gameserver.module.adventures.favor.effect.FavorParser;
import com.jtang.gameserver.module.adventures.favor.effect.FavorTrigger;
import com.jtang.gameserver.module.adventures.favor.facade.FavorFacade;
import com.jtang.gameserver.module.adventures.favor.helper.FavorPushHelper;
import com.jtang.gameserver.module.adventures.favor.model.FavorVO;
import com.jtang.gameserver.module.adventures.favor.model.PrivilegeVO;
import com.jtang.gameserver.module.adventures.favor.type.FavorParserType;
import com.jtang.gameserver.module.adventures.favor.type.FavorTriggerType;
import com.jtang.gameserver.module.adventures.favor.type.FavorType;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class FavorFacadeImpl implements FavorFacade, Receiver, ApplicationListener<ContextRefreshedEvent>, ActorLoginListener {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private FavorDao favorDao;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private EventBus eventBus;
	
	@Autowired
	private BattleFacade battleFacade;
	
	@Autowired
	private Schedule schedule;
	
	@Autowired
	private PlayerSession playerSession;
	
	@PostConstruct
	private void init(){
		eventBus.register(EventKey.SNATCH_RESULT, this);
		eventBus.register(EventKey.TICKETS_RECHARGE, this);
		eventBus.register(EventKey.BABLE_BATTLE_TIMES, this);
	}
	
	@Override
	public TResult<FavorVO> get(long actorId) {
		Favor favor = favorDao.get(actorId);
		boolean flag = checkExistForEver(actorId);
		if (flag){
			FavorVO favorVO = FavorVO.valueOf(0, favor.getPrivilegeList().values(), FavorType.PERMANENT.getType());
			return TResult.sucess(favorVO);
		} else {
			int coutDown = favor.getCountDown();
			FavorVO favorVO = null;
			if (coutDown > 0) {
				favorVO = FavorVO.valueOf(coutDown, favor.getPrivilegeList().values(), FavorType.TEMP.getType());
			} else {
				favorVO = FavorVO.valueOf(0, new ArrayList<PrivilegeVO>(), FavorType.TEMP.getType());
			}
			return TResult.sucess(favorVO);
		}
	}
	


	@Override
	public TResult<FavorVO> usePrivilege(long actorId, int privilegeId) {
		FavorRightConfig config = FavorRightService.getById(privilegeId);
		if (config == null) {
			return TResult.valueOf(FAVOR_UN_EXISTS);
		}
		FavorParserType favorParserType = FavorParserType.getByType(config.parseId);
		FavorParser favorParser = FavorParser.getFavorParser(favorParserType);
		Favor favor = favorDao.get(actorId);
		TResult<PrivilegeVO> flag = favorParser.execute(actorId, favor);
		if (flag.isOk()) {
			favorDao.update(favor);
			TResult<FavorVO> result = get(actorId);
			return result;
		} else {
			return TResult.valueOf(flag.statusCode);
		}
	}

	@Override
	public void onEvent(Event paramEvent) {
		GameEvent gameEvent = paramEvent.convert();
		trigger(gameEvent);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable() {
			
			@Override
			public void run() {
				Set<Long> ids = playerSession.onlineActorList();
				for (long actorId : ids) {
					if (checkDisapper(actorId)) {
						FavorPushHelper.pushDisapperFavor(actorId);
					}
				}
			}
		}, 1);
		
		schedule.addFixedTime(new Runnable() {
			
			@Override
			public void run() {
				Set<Long> ids = playerSession.onlineActorList();
				for (long actorId : ids) {
					FavorVO favorVO = checkReset(actorId);
					if (favorVO != null) {
						FavorPushHelper.pushReset(actorId, favorVO);
					}
				}
			}
		}, FavorRule.REFRESH_TIME);
	}

	@Override
	public void onLogin(long actorId) {
		checkReset(actorId);
		checkDisapper(actorId);
	}
	
	/**
	 * 触发
	 * @param gameEvent
	 */
	private void trigger(GameEvent gameEvent){
		int time = TimeUtils.getNow();
		Favor favor = favorDao.get(gameEvent.actorId);
		if (gameEvent instanceof SnatchResultEvent){
			FavorTrigger trigger = FavorTrigger.getTrigger(FavorTriggerType.TYPE1);
			boolean hasTrigger = favor.hasTrigger(trigger.getTriggerType());
			if (!hasTrigger){
				boolean flag = trigger.isTrigger(gameEvent.actorId);
				if (flag){
					favor.setAppearTime(time);
					favor.addTrigger(trigger.getTriggerType());
					favorDao.update(favor);
					pushTriggerAlert(gameEvent.actorId);
				}
			}
		} else if (gameEvent instanceof BableTimesEvent){
			FavorTrigger trigger = FavorTrigger.getTrigger(FavorTriggerType.TYPE2);
			boolean hasTrigger = favor.hasTrigger(trigger.getTriggerType());
			if (!hasTrigger){
				boolean flag = trigger.isTrigger(gameEvent.actorId);
				if (flag){
					favor.setAppearTime(time);
					favor.addTrigger(trigger.getTriggerType());
					favorDao.update(favor);
					pushTriggerAlert(gameEvent.actorId);
				}
			}
		} else if (gameEvent instanceof RechargeTicketsEvent) {
			pushTriggerAlert(gameEvent.actorId);
		}
	}
	
	/**
	 * 推送触发
	 * @param actorId
	 */
	private void pushTriggerAlert(long actorId){
		FavorVO favorVO = get(actorId).item;
		FavorPushHelper.pushActiveFavor(actorId, favorVO);
	}
	/**
	 * 检查是否消失
	 * @param actorId
	 */
	private boolean checkDisapper(long actorId){
		if (checkExistForEver(actorId)){
			return false;
		}
		Favor favor = favorDao.get(actorId);
		int countDownTime = favor.getCountDown();
		if (countDownTime == 0){
			ChainLock lock = LockUtils.getLock(favor);
			lock.lock();
			try {
				favor.setAppearTime(0);
				favorDao.update(favor);
			} catch (Exception e) {
				LOGGER.error("{}", e);
			} finally {
				lock.unlock();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 是否永久福神眷顾
	 * @param actorId
	 * @return
	 */
	private boolean checkExistForEver(long actorId){
		int totalTicket = vipFacade.getTotalRechargeTicket(actorId);
		if (totalTicket >= FavorRule.PERMANET_NEED_TOTAL_TICKET){
			return true;
		}
		return false;
	}
	
	/**
	 * 检查是否重置
	 * @param actorId
	 */
	private FavorVO checkReset(long actorId){
		if (checkExistForEver(actorId)) {
			Favor favor = favorDao.get(actorId);
			ChainLock lock = LockUtils.getLock(favor);
			lock.lock();
			try {
				if (!DateUtils.isToday(favor.resetTime)) {
					for (PrivilegeVO privilegeVO : favor.getPrivilegeList().values()) {
						privilegeVO.setUsedNum(0);
					}
					favor.resetTime = TimeUtils.getNow();
					favorDao.update(favor);
					return get(actorId).item;
				}
			} catch (Exception e) {
				LOGGER.error("{}", e);
			} finally {
				lock.unlock();
			}
		}
		return null;
	}
	
}
