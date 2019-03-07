package com.jtang.gameserver.module.extapp.welkin.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.RewardConfig;
import com.jtang.gameserver.dataconfig.model.WelkinConfig;
import com.jtang.gameserver.dataconfig.model.WelkinGlobalConfig;
import com.jtang.gameserver.dataconfig.model.WelkinRewardConfig;
import com.jtang.gameserver.dataconfig.service.WelkinService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Welkin;
import com.jtang.gameserver.module.applog.facade.LogFacade;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.module.extapp.welkin.dao.WelkinDao;
import com.jtang.gameserver.module.extapp.welkin.facade.WelkinFacade;
import com.jtang.gameserver.module.extapp.welkin.handler.response.WelkinRankResponse;
import com.jtang.gameserver.module.extapp.welkin.handler.response.WelkinResponse;
import com.jtang.gameserver.module.extapp.welkin.helper.WelkinPushHelper;
import com.jtang.gameserver.module.extapp.welkin.module.WelkinRankVO;
import com.jtang.gameserver.module.extapp.welkin.module.WelkinVO;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class WelkinFacadeImpl implements WelkinFacade, ActorLoginListener,
ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(WelkinFacadeImpl.class);
	@Autowired
	WelkinDao welkinDao;
	@Autowired
	Schedule schedule;
	@Autowired
	PlayerSession playerSession;
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	VipFacade vipFacade;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	HeroSoulFacade heroSoulFacade;
	@Autowired
	EquipFacade equipFacade;
	@Autowired
	SysmailFacade sysmailFacade;
	@Autowired
	ChatFacade chatFacade;
	@Autowired
	LogFacade logFacade;
	
	/**
	 * 是否开放
	 */
	private boolean isOpen = false;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable() {
			@Override
			public void run() {
				Set<Long> actorIds = playerSession.onlineActorList();
				WelkinGlobalConfig globalConfig = WelkinService.getWelKinGlobalConfig();
				for (Long actorId : actorIds) {
					int now = TimeUtils.getNow();
					if (isOpen) {// 开启才能关闭
						if (globalConfig.start >= now || now > globalConfig.end) {
							isOpen = false;
							WelkinPushHelper.pushState(actorId,0);
						}
					} else {// 关闭才能开启
						if (globalConfig.start <= now && now < globalConfig.end) {
							isOpen = true;
							WelkinPushHelper.pushState(actorId,1);
						}
					}
				}
			}
		}, 1);
		
		schedule.addFixedTime(new Runnable() {
			@Override
			public void run() { 
				TResult<WelkinRankResponse> result = getRank();
				List<WelkinRankVO> list = result.item.list;
				logFacade.saveWelkin(list);
//				for(WelkinRankVO rank:list){//给满足条件的上榜玩家发奖并清理数据
//					List<RewardObject> rewardObjects = WelkinService.getWelkinRankConfig(rank.rank , rank.useNum);
//					if(rewardObjects != null){
//						sysmailFacade.sendSysmail(rank.actorId, SysmailType.WELKIN_RANK, rewardObjects , rank.rank);
//						reset(rank.actorId,TimeUtils.getNow());
//					}
//				} 
				
				WelkinGlobalConfig config = WelkinService.getWelKinGlobalConfig();
				List<Welkin> rankList = welkinDao.getRuank(config.rankCount);
				for(Welkin welkin:rankList){
					reset(welkin.actorId,TimeUtils.getNow());
				}
				
				Set<Long> actorIds = playerSession.onlineActorList();
				int now = TimeUtils.getNow();
				for(Long actorId : actorIds){//重置在线玩家数据
					reset(actorId,now);
					WelkinPushHelper.pushWelkinResponse(actorId,getWelkinInfo(actorId).item);
				}
			}
		},0, 0, 5);
	}

	@Override 
	public void onLogin(long actorId) {
		if(isOpen){
			reset(actorId,TimeUtils.getNow());
		}
	}
	
	@Override
	public TResult<WelkinResponse> getWelkinInfo(long actorId) {
		reset(actorId,TimeUtils.getNow());
		Welkin welkin = welkinDao.getWelkin(actorId);
		WelkinGlobalConfig globalConfig = WelkinService.getWelKinGlobalConfig();
		int level = ActorHelper.getActorLevel(actorId);
		WelkinVO welkinVO = WelkinVO.valueOf(welkin, level);
		String place = WelkinService.getPlace();
		WelkinResponse response = new WelkinResponse(welkinVO,globalConfig.goldNum,globalConfig.useGoldCount,place);
		return TResult.sucess(response);
	}

	@Override
	public TResult<WelkinResponse> welkin(long actorId, int count) {
		if(isOpen == false){
			return TResult.valueOf(GameStatusCodeConstant.APP_CLOSED);
		}
		Welkin welkin = welkinDao.getWelkin(actorId);
		int level = ActorHelper.getActorLevel(actorId);
		int useCount = welkin.useCount + welkin.ticketUseCount;
		WelkinConfig welkinConfig = WelkinService.getWelkinConfig(useCount);
		if(welkinConfig.intervalEnd == -1){
			if(welkin.mastRewardCount < useCount){
				welkin.mastRewardCount = useCount + RandomUtils.nextInt(welkinConfig.startCount,welkinConfig.endCount);
			}
		}else if(welkinConfig.intervalEnd <= useCount){//达到此区间最大次数
			welkinConfig = WelkinService.getWelkinConfig(useCount+1);//获得下一个区间
		}else{
			if(welkinConfig.intervalEnd - useCount < count){//如果此次随机数后已到下一次,则只随机当前区间
				count = welkinConfig.intervalEnd - useCount;
			}
		}
		WelkinGlobalConfig globalConfig = WelkinService.getWelKinGlobalConfig();
		int welkinUseCount = 0;
		int welkinTicketUseCount = 0;
		if(welkin.useCount < globalConfig.useGoldCount){//使用金币
			int golsNum = globalConfig.goldNum * count;
			boolean isGold = actorFacade.decreaseGold(actorId, GoldDecreaseType.APP, golsNum);
			if(isGold == false){
				return TResult.valueOf(GameStatusCodeConstant.WELKIN_GOLD_NOT_ENOUGH);
			}
			welkinUseCount = 1 * count;
		}else{//使用点券
			int ticketNum = welkinConfig.costTicket * count;
			boolean isTicket = vipFacade.decreaseTicket(actorId, TicketDecreaseType.WELKIN, ticketNum, 0, 0);
			if(isTicket == false){
				return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
			}
			welkinTicketUseCount = 1 * count;
		}
		List<RewardObject> allReward = WelkinService.getAllReward(welkinConfig.id, level);//当前区间所有奖励
		List<WelkinRewardConfig> allRewardConfig = WelkinService.getAllRewardConfig(welkinConfig.id);//当前区间所有奖励配置
		List<RewardObject> sendChatReward = new ArrayList<>();//需要发放消息的奖励
		List<RewardObject> reward = new ArrayList<>();//获得的奖励
		int index = 0;
		if(welkin.mastRewardCount != 0 && welkin.mastRewardCount <= welkin.useCount + welkin.ticketUseCount){//超过最大区间进行的固定奖励
			index = WelkinService.getReward(welkinConfig.id,true);
			reward.add(allReward.get(index));
			for (int i = 0; i < count - 1; i++) {
				index = WelkinService.getReward(welkinConfig.id,false);
				reward.add(allReward.get(index));
				sendChat(allRewardConfig,sendChatReward,index,level);
			}
		}else if(WelkinService.isGoodReward(useCount,count)){//是否到达保底奖励次数
			index = WelkinService.getReward(welkinConfig.id,true);
			reward.add(allReward.get(index));
			for (int i = 0; i < count - 1; i++) {
				index = WelkinService.getReward(welkinConfig.id,false);
				reward.add(allReward.get(index));
				sendChat(allRewardConfig,sendChatReward,index,level);
			}
		}else{
			for (int i = 0; i < count; i++) {
				index = WelkinService.getReward(welkinConfig.id,false);
				reward.add(allReward.get(index));
				sendChat(allRewardConfig,sendChatReward,index,level);
			}
		}
		if(sendChatReward.isEmpty() == false){
			chatFacade.sendWelkinChat(actorId,sendChatReward,0);
		}
		sendReward(actorId,reward);
		ChainLock lock = LockUtils.getLock(welkin);
		try {
			lock.lock();
			welkin.useCount += welkinUseCount;
			welkin.ticketUseCount += welkinTicketUseCount;
			welkinDao.update(welkin);
		} catch (Exception e) {
			LOGGER.error("{}",e);
		} finally {
			lock.unlock();
		}
		WelkinConfig newWelkinConfig = WelkinService.getWelkinConfig(welkin.useCount + welkin.ticketUseCount+1);
		sendPlaceChat(actorId, level, welkinConfig, newWelkinConfig);//更换区间(给固定奖励并发公告)
		WelkinVO welkinVO = WelkinVO.valueOf(welkin, level);
		welkinVO.reward = reward;
		welkinVO.index = index;
		welkinVO.costTicket = newWelkinConfig.costTicket;
		welkinVO.place = newWelkinConfig.id;
		String place = WelkinService.getPlace();
		WelkinResponse response = new WelkinResponse(welkinVO,globalConfig.goldNum,globalConfig.useGoldCount,place);
		return TResult.sucess(response);
	}

	/**
	 * 到达区间固定奖励发送系统公告
	 * @param actorId
	 * @param level
	 * @param welkinConfig
	 * @param newWelkinConfig
	 */
	private void sendPlaceChat(long actorId, int level,
			WelkinConfig welkinConfig, WelkinConfig newWelkinConfig) {
		if(welkinConfig.id != newWelkinConfig.id){//发世界公告到达新阶段
			List<RewardConfig> list = welkinConfig.rewardList;
			List<RewardObject> rewardList = new ArrayList<>();
			for(RewardConfig rewardConfig:list){
				RewardObject rewardObject = new RewardObject();
				rewardObject.rewardType = RewardType.getType(rewardConfig.type);
				rewardObject.id = rewardConfig.rewardId;
				rewardObject.num = FormulaHelper.executeCeilInt(rewardConfig.num, level);
				rewardList.add(rewardObject);
			}
			chatFacade.sendWelkinChat(actorId,rewardList,welkinConfig.id);
			sendReward(actorId, rewardList);
		}
	}

	/**
	 * 发送系统公告
	 * @param allRewardConfig
	 * @param index
	 */
	private void sendChat(List<WelkinRewardConfig> allRewardConfig,List<RewardObject> sendChatReward, int index ,int level) {
		WelkinRewardConfig welkinRewardConfig = allRewardConfig.get(index);
		if(welkinRewardConfig.sendChat == 1){
			RewardObject rewardObject = WelkinService.parseToRewardObject(level, welkinRewardConfig);
			sendChatReward.add(rewardObject);
		}
	}

	private void sendReward(long actorId, List<RewardObject> reward) {
		for(RewardObject rewardObject:reward){
			switch (rewardObject.rewardType) {
			case GOODS:
				goodsFacade.addGoodsVO(actorId, GoodsAddType.PLANT, rewardObject.id,rewardObject.num);
				break;
			case EQUIP:
				equipFacade.addEquip(actorId, EquipAddType.PLANT, rewardObject.id);
				break;
			case HEROSOUL:
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.PLANT, rewardObject.id,rewardObject.num);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public TResult<WelkinRankResponse> getRank() {
		WelkinGlobalConfig globalConfig = WelkinService.getWelKinGlobalConfig();
		List<Welkin> welkinList = welkinDao.getRank(globalConfig.rankCount,globalConfig.rank);
		if(welkinList == null){
			return TResult.valueOf(GameStatusCodeConstant.WELKIN_ACTOR_NOT_RANK);
		}
		List<WelkinRankVO> list = new ArrayList<>();
		for(int i=0;i<welkinList.size();i++){
			Welkin welkin = welkinList.get(i);
			Actor actor = actorFacade.getActor(welkin.actorId);
			int vipLevel = vipFacade.getVipLevel(welkin.actorId);
			WelkinRankVO welkinRankVO = new WelkinRankVO();
			welkinRankVO.actorId = welkin.actorId;
			welkinRankVO.name = actor.actorName;
			welkinRankVO.level = actor.level;
			welkinRankVO.vipLevel = vipLevel;
			welkinRankVO.useNum = welkin.useCount + welkin.ticketUseCount;
			welkinRankVO.rank = i + 1;
			list.add(welkinRankVO);
		}
		WelkinRankResponse response = new WelkinRankResponse();
		response.list = list;
		return TResult.sucess(response);
	}

	
	private void reset(long actorId,int now) {
		Welkin welkin = welkinDao.getWelkin(actorId);
		ChainLock lock = LockUtils.getLock(welkin);
		try {
			lock.lock();
			if(DateUtils.isToday(welkin.operationTime) == false){
				welkin.reset();
				welkinDao.update(welkin);
			}
		} catch (Exception e) {
			LOGGER.error("{}",e);
		} finally {
			lock.unlock();
		}
	}
	
}
