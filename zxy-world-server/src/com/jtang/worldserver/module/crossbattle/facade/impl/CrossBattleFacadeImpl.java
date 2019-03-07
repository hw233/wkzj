package com.jtang.worldserver.module.crossbattle.facade.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.crossbattle.model.ActorCrossData;
import com.jiatang.common.crossbattle.model.AttackNoticeVO;
import com.jiatang.common.crossbattle.model.CrossData;
import com.jiatang.common.crossbattle.model.DayEndRewardVO;
import com.jiatang.common.crossbattle.model.EndNoticeVO;
import com.jiatang.common.crossbattle.model.HomeServerRank;
import com.jiatang.common.crossbattle.model.HomeServerRankVO;
import com.jiatang.common.crossbattle.model.LastBattleResultVO;
import com.jiatang.common.crossbattle.model.ServerScoreList;
import com.jiatang.common.crossbattle.model.ServerScoreVO;
import com.jiatang.common.crossbattle.model.ViewLineupVO;
import com.jiatang.common.crossbattle.request.AttackActorResultG2W;
import com.jiatang.common.crossbattle.request.SignupG2W;
import com.jiatang.common.crossbattle.response.ActorCrossDataW2G;
import com.jiatang.common.crossbattle.response.ActorPointW2G;
import com.jiatang.common.crossbattle.response.AllEndW2G;
import com.jiatang.common.crossbattle.response.AttackPlayerW2G;
import com.jiatang.common.crossbattle.response.CrossBattleConfigW2G;
import com.jiatang.common.crossbattle.response.EndNoticeW2G;
import com.jiatang.common.crossbattle.response.EndRewardW2G;
import com.jiatang.common.crossbattle.response.ExchangePointW2G;
import com.jiatang.common.crossbattle.response.HomeServerRankW2G;
import com.jiatang.common.crossbattle.response.LastBattleResultW2G;
import com.jiatang.common.crossbattle.response.LineupW2G;
import com.jiatang.common.crossbattle.response.ServerRankW2G;
import com.jiatang.common.crossbattle.response.SignupW2G;
import com.jiatang.common.crossbattle.type.CrossBattleActorAttributeKey;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.TimeUtils;
import com.jtang.worldserver.component.oss.WorldOssLogger;
import com.jtang.worldserver.dataconfig.model.CrossBattleConfig;
import com.jtang.worldserver.dataconfig.model.CrossBattleDayConfig;
import com.jtang.worldserver.dataconfig.model.CrossBattleExchangeConfig;
import com.jtang.worldserver.dataconfig.model.CrossBattlePointConfig;
import com.jtang.worldserver.dataconfig.service.CrossBattleDayService;
import com.jtang.worldserver.dataconfig.service.CrossBattleEndAwardService;
import com.jtang.worldserver.dataconfig.service.CrossBattlePointService;
import com.jtang.worldserver.dataconfig.service.CrossBattleService;
import com.jtang.worldserver.dbproxy.entity.CrossBattle;
import com.jtang.worldserver.dbproxy.entity.CrossBattleActor;
import com.jtang.worldserver.dbproxy.entity.CrossBattleHurtRank;
import com.jtang.worldserver.module.crossbattle.dao.CrossBattleActorDao;
import com.jtang.worldserver.module.crossbattle.dao.CrossBattleDao;
import com.jtang.worldserver.module.crossbattle.dao.CrossBattleHurtRankDao;
import com.jtang.worldserver.module.crossbattle.facade.CrossBattleFacade;
import com.jtang.worldserver.module.crossbattle.helper.CrossBattleHelper;
import com.jtang.worldserver.module.crossbattle.helper.CrossBattlePushHelper;
import com.jtang.worldserver.module.crossbattle.model.ServerCrossData;
import com.jtang.worldserver.module.crossbattle.model.SpriteIdUtil;
import com.jtang.worldserver.module.crossbattle.type.CrossBattleResult;

@Component
public class CrossBattleFacadeImpl implements CrossBattleFacade, ApplicationListener<ContextRefreshedEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private Schedule schedule;

	@Autowired
	private CrossBattleActorDao crossBattleActorDao;
	
	@Autowired
	private CrossBattleDao crossBattleDao;
	
	@Autowired
	private CrossBattleHurtRankDao crossBattleHurtRankDao;
	/**
	 * key:服务器id
	 */
	private Map<Integer, ServerCrossData> serverData = new ConcurrentHashMap<Integer, ServerCrossData>();

	/**
	 * 联赛开始
	 */
	private boolean isStart = false;
	
	/**
	 * 报名开始
	 */
	private boolean isSignup = false;
	
	/**
	 * key:角色id
	 * value 是否已读
	 */
	private Map<Long, Boolean> rewardReadFlag = new HashMap<>();
	
	/**
	 * 攻击
	 */
	private static final byte ATTACK = 1;
	/**
	 * 被攻击
	 */
	private static final byte BE_ATTACK = 0;
	
//	/**
//	 * 攻击冷却时间
//	 */
//	private Map<Long, Integer> attackCd = new ConcurrentHashMap<>();
//	
//	/**
//	 * 复活保护时间
//	 */
//	private Map<Long, Integer> protectTimes = new ConcurrentHashMap<>();

	private void start() {
		LOGGER.info("crossbattle start...");
		int dayNum = CrossBattleService.getDayNum();
		if (dayNum == 1) {//新赛季
			crossBattleDao.deleteLast();
		}
		isStart = true;

	}

	private void end() {
		isStart = false;
		CrossBattlePushHelper.pushBattleCrossNotice(false);
		CrossBattleConfig cfg = CrossBattleService.get();
		// 计算对阵服务器胜败计算获得积分
		int dayNum = CrossBattleService.getDayNum();
		Set<Integer> homeServerId = CrossBattleDayService.getDayServerId(dayNum);
		List<CrossBattle> updateCrossBattleList = new ArrayList<>();
		for (int serverId : homeServerId) {
			CrossBattleDayConfig crossBattleDayCfg = CrossBattleDayService.get(dayNum, serverId);
			ServerCrossData data = serverData.get(crossBattleDayCfg.getHomeServerId());
			ServerCrossData awayData = serverData.get(crossBattleDayCfg.getAwayServerId());
			
			if (data != null && awayData != null) {
				CrossBattle crossBattle = crossBattleDao.getCrossBattle(data.getServerId());
				CrossBattle awayCrossBattle = crossBattleDao.getCrossBattle(awayData.getServerId());
				if (data.getTotalHurt() > awayData.getTotalHurt()) {
					data.setCrossBattleResult(CrossBattleResult.WIN);
					awayData.setCrossBattleResult(CrossBattleResult.FAIL);
					crossBattle.score += cfg.getPoint(CrossBattleResult.WIN);
					awayCrossBattle.score += cfg.getPoint(CrossBattleResult.FAIL);
				} else if (data.getTotalHurt() == awayData.getTotalHurt()) {
					data.setCrossBattleResult(CrossBattleResult.DRAW);
					awayData.setCrossBattleResult(CrossBattleResult.DRAW);
					crossBattle.score += cfg.getPoint(CrossBattleResult.DRAW);
					awayCrossBattle.score += cfg.getPoint(CrossBattleResult.DRAW);
				} else {
					data.setCrossBattleResult(CrossBattleResult.FAIL);
					awayData.setCrossBattleResult(CrossBattleResult.WIN);
					crossBattle.score += cfg.getPoint(CrossBattleResult.FAIL);
					awayCrossBattle.score += cfg.getPoint(CrossBattleResult.WIN);
				}
				crossBattle.dayIsWin = data.getCrossBattleResult().getId();
				awayCrossBattle.dayIsWin = awayData.getCrossBattleResult().getId();
				crossBattle.groupId = data.getGroupId();
				awayCrossBattle.groupId = awayData.getGroupId();
				if (updateCrossBattleList.contains(crossBattle) == false) {
					updateCrossBattleList.add(crossBattle);
				}
				if (updateCrossBattleList.contains(awayCrossBattle) == false) {
					updateCrossBattleList.add(awayCrossBattle);
				}
			} else if (data != null && awayData == null) {
				CrossBattle crossBattle = crossBattleDao.getCrossBattle(data.getServerId());
				crossBattle.score += cfg.getNotTargetPoint();
				crossBattle.groupId = data.getGroupId();
				if (updateCrossBattleList.contains(crossBattle) == false) {
					updateCrossBattleList.add(crossBattle);
				}
			} else if (data == null && awayData != null) {
				CrossBattle awayCrossBattle = crossBattleDao.getCrossBattle(awayData.getServerId());
				awayCrossBattle.score += cfg.getNotTargetPoint();
				awayCrossBattle.groupId = awayData.getGroupId();
				if (updateCrossBattleList.contains(awayCrossBattle) == false) {
					updateCrossBattleList.add(awayCrossBattle);
				}
			}
		}
		crossBattleDao.update(updateCrossBattleList);
		
		
		Map<Integer, List<CrossBattle>> groupMap = new HashMap<>();
		for (CrossBattle crossBattle : updateCrossBattleList) {
			List<CrossBattle> groupList = null;
			if (groupMap.containsKey(crossBattle.groupId)){
				groupList = groupMap.get(crossBattle.groupId);
			} else {
				groupList = new ArrayList<>();
				groupMap.put(crossBattle.groupId, groupList);
			}
			groupList.add(crossBattle);
		}
		
		for (List<CrossBattle> groupList : groupMap.values()) {
			Collections.sort(groupList, new Comparator<CrossBattle>() {

				@Override
				public int compare(CrossBattle o1, CrossBattle o2) {
					if (o1.score > o2.score) {
						return -1;
					} else if (o1.score < o2.score) {
						return 1;
					}
					return 0;
				}
			});
			
			for (int i = 0; i < groupList.size(); i++) {
				CrossBattle cb = groupList.get(i);
				if (i == 0) {
					cb.rank = i + 1;
				} else {
					CrossBattle cbLast = groupList.get(i - 1);
					if (cb.score == cbLast.score) {
						cb.rank = cbLast.rank;
					} else {
						cb.rank = i + 1;
					}
				}
			}
		}
		
		// 计算没个玩家获得的贡献点
		List<CrossBattleHurtRank> crossBattleHurtRanks = new ArrayList<>();
		List<CrossBattleActor> crossBattleActors = new ArrayList<>();
		for (Map.Entry<Integer, ServerCrossData> entry : serverData.entrySet()) {
			int serverId = entry.getKey();
			ServerCrossData serverCrossData = entry.getValue();
			Collection<ActorCrossData> actorCrossDatas = serverCrossData.getActorCrossDataMap().values();
			List<ActorCrossData> hurtRankList = new ArrayList<>();
			hurtRankList.addAll(actorCrossDatas);
			Collections.sort(hurtRankList, new Comparator<ActorCrossData>() {

				@Override
				public int compare(ActorCrossData o1, ActorCrossData o2) {
					if (o1.getRewardHurt() > o2.getRewardHurt()) {
						return -1;
					} else if (o1.getRewardHurt() < o2.getRewardHurt()) {
						return 1;
					}
					return 0;
				}
			});
			
			for (int i = 0; i < hurtRankList.size(); i++) {
				ActorCrossData actorCrossData = hurtRankList.get(i);
				if (i > 0) {
					ActorCrossData lastActorCrossData = hurtRankList.get(i - 1);
					if (actorCrossData.getRewardHurt() == lastActorCrossData.getRewardHurt()) {
						actorCrossData.hurtRank = lastActorCrossData.hurtRank;
					} else {
						actorCrossData.hurtRank = i + 1;
					}
				} else {
					actorCrossData.hurtRank = i + 1;
				}
				
			}
			
			int maxDay = CrossBattleDayService.getTotalDay();
			for (ActorCrossData actorCrossData : hurtRankList) {
				if (actorCrossData.hurtRank == 1) {
					CrossBattle crossBattle = crossBattleDao.getCrossBattle(serverId);
					crossBattle.bestActorId = actorCrossData.actorId;
					crossBattle.bestActorName = actorCrossData.actorName;
					crossBattleDao.update(crossBattle);
				}
				
				CrossBattleActor crossBattleActor = crossBattleActorDao.getCrossBattleActor(actorCrossData.actorId, serverId);
				CrossBattleHurtRank crossBattleHurtRank = crossBattleHurtRankDao.getCrossBattleHurtRank(actorCrossData.actorId, serverId);
				int winPoint = cfg.getDayExchangePoint(serverCrossData.getCrossBattleResult());
				CrossBattlePointConfig rankCfg = CrossBattlePointService.get(actorCrossData.hurtRank);
				int rankPoint = 0;
				if (rankCfg != null) {
					rankPoint = rankCfg.getExchangePoint();
				}
				int killPoint = cfg.getDayKillExchangePoint(serverCrossData.getCrossBattleResult(), actorCrossData.getKillNum());
				List<RewardObject> rewardObjects = CrossBattleService.get().getReward(serverCrossData.getCrossBattleResult(), actorCrossData.level);
				if (actorCrossData.getAttackNum() > 0) { // 有主动攻击才有贡献点
					crossBattleActor.exchangePoint += winPoint;
					crossBattleActor.exchangePoint += rankPoint;
					crossBattleActor.exchangePoint += killPoint;
					crossBattleHurtRank.dayWinServerExchangePoint = winPoint;
					crossBattleHurtRank.dayRankExchangePoint = rankPoint;
					crossBattleHurtRank.dayKillExchangePoint = killPoint;
					crossBattleActors.add(crossBattleActor);
					int addPoint = winPoint + rankPoint + killPoint;
					WorldOssLogger.exchangePointAdd(serverId, crossBattleActor.actorId, addPoint, crossBattleActor.exchangePoint);
				} else {
					crossBattleHurtRank.dayWinServerExchangePoint = 0;
					crossBattleHurtRank.dayRankExchangePoint = 0;
					crossBattleHurtRank.dayKillExchangePoint = 0;
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(String.format("actorId:[%s]没有主动攻击，不能获得贡献点", actorCrossData.actorId));
					}
				}
				
				
				crossBattleHurtRank.beKilledNum = actorCrossData.getBeKilledNum();
				crossBattleHurtRank.continueKillNum = actorCrossData.getMaxContuneKillNum();
				crossBattleHurtRank.rank = actorCrossData.hurtRank;
				crossBattleHurtRank.totalHurt = actorCrossData.getRewardHurt();
				crossBattleHurtRank.totalKillNum = actorCrossData.getKillNum();
				crossBattleHurtRank.actorName = actorCrossData.actorName;
				crossBattleHurtRank.dayGoodsRecordList = rewardObjects;
				crossBattleHurtRank.serverId = serverCrossData.getServerId();
				
				// 联赛结束奖励
//				if (dayNum == maxDay) {
//					CrossBattle crossBattle = crossBattleDao.getCrossBattle(serverCrossData.getServerId());
//					int scoreRank = crossBattle.rank;
//					List<RewardObject> allEndReward = CrossBattleEndAwardService.getDayEndReward(scoreRank, actorCrossData.level);
//					crossBattleHurtRank.endAwardRecordList = allEndReward;
//				}
				
				crossBattleHurtRanks.add(crossBattleHurtRank);
				
				TResult<EndRewardW2G> result = dayBattleEndRewardResult(serverId, actorCrossData.actorId);
				CrossBattlePushHelper.pushDayEndReward(serverId,actorCrossData.actorId, result.item);
				rewardReadFlag.put(actorCrossData.actorId, false);
			}
			
			
			if (dayNum == maxDay) {
				CrossBattle crossBattle = crossBattleDao.getCrossBattle(serverCrossData.getServerId());
				int scoreRank = crossBattle.rank;
				if (CrossBattleEndAwardService.getMaxRank() >= scoreRank) {
					String allEndReward = CrossBattleEndAwardService.getCrossBattleEndReward(scoreRank);
					AllEndW2G allEndW2G = new AllEndW2G(scoreRank, allEndReward);
					CrossBattlePushHelper.pushAllEnd(serverId, 0, allEndW2G);
				}
			}
			
			// 推送系统消息
			if (serverCrossData.getCrossBattleResult().equals(CrossBattleResult.WIN)) {
				CrossBattle crossBattle = crossBattleDao.getCrossBattle(serverId);
				ActorCrossData best = serverCrossData.getActorCrossData(crossBattle.bestActorId);
				CrossBattleHurtRank beastCrossBattleHurtRank = crossBattleHurtRankDao.getCrossBattleHurtRank(crossBattle.bestActorId, serverId);
				EndNoticeVO endNoticeVO = new EndNoticeVO(serverCrossData.getOtherServerId(),best.actorName,best.level, best.vipLevel, beastCrossBattleHurtRank.totalKillNum, beastCrossBattleHurtRank.totalHurt, beastCrossBattleHurtRank.continueKillNum);
				EndNoticeW2G endNoticeW2G = new EndNoticeW2G(endNoticeVO);
				CrossBattlePushHelper.pushNotice(serverCrossData.getServerId(), endNoticeW2G);
			}
		}
		crossBattleHurtRankDao.update(crossBattleHurtRanks);
		crossBattleActorDao.update(crossBattleActors);
		isSignup = false;
		LOGGER.info("crossbattle end....");
		
	}

	private void signupStart() {
//		if (CrossBattleDayService.dayConfigSuccess(CrossBattleService.getDayNum()) == false) {
//			LOGGER.error("配置天数与比赛时间不匹配");
//			return;
//		}
		serverData.clear();
		isSignup = true;
		CrossBattlePushHelper.pushBattleCrossNotice(true);
		CrossBattleConfig cfg = CrossBattleService.get();
		SignupW2G signupResponse = new SignupW2G(cfg.getTopPowerRank());
		CrossBattlePushHelper.pushSignup(signupResponse);
		LOGGER.info("crossbattle signup.....");
	}

	@Override
	public void signup(int serverId, SignupG2W signupRequest) {

		Map<Long, ActorCrossData> actorCrossDataMap = new ConcurrentHashMap<>();
		for (ActorCrossData actorCrossData : signupRequest.actorCrossData) {
			int joinLevel = CrossBattleService.joinActorLevel();
			if (actorCrossData.level < joinLevel) {
				continue;
			}
			List<Long> ids = SpriteIdUtil.getSpriteId(actorCrossData.getFightModel().getHeros().size());
			actorCrossData.getFightModel().setSpriteId(ids);
			actorCrossDataMap.put(actorCrossData.actorId, actorCrossData);
		}
		int dayNum = CrossBattleService.getDayNum();
		int otherServerId = CrossBattleDayService.getOtherServerId(dayNum, serverId);
		CrossBattleDayConfig cfg = CrossBattleDayService.get(dayNum, serverId);
		if (cfg == null || otherServerId == 0) {
			return;
		}
		ServerCrossData data = new ServerCrossData(serverId, 0, actorCrossDataMap, cfg.getGroupId(), otherServerId);
		serverData.put(serverId, data);
		LOGGER.info(String.format("serverId:[%s] signup success.", serverId));
	}

	@Override
	public ServerCrossData getCrossData(int serverId) {
		if (serverData.containsKey(serverId)) {
			return serverData.get(serverId);
		}
		return null;
	}

	@Override
	public TResult<AttackPlayerW2G> attackPlayer(int serverId, long actorId, long targetActorId) {
		if (isStart == false) {
			return TResult.valueOf(GameStatusCodeConstant.CROSS_BATTLE_NOT_START);
		}
		ServerCrossData serverCrossData = serverData.get(serverId);
		ServerCrossData otherServerCrossData = serverData.get(serverCrossData.getOtherServerId());

		ActorCrossData actorCrossData = serverCrossData.getActorCrossData(actorId);
		ActorCrossData otherActorCrossData = otherServerCrossData.getActorCrossData(targetActorId);
		
		if (actorCrossData == null || otherActorCrossData == null) {
			return TResult.valueOf(GameStatusCodeConstant.CROSS_DATA_NOT_EXSIT);
		}
		int attackTime = actorCrossData.getAttackTime();
		int now = TimeUtils.getNow();
		int cdTime = CrossBattleService.get().getAtkCDTime();
		if (now - attackTime < cdTime) {
			return TResult.valueOf(GameStatusCodeConstant.CROSS_BATTLE_CD);
		}
		int reviveTargetTime = otherActorCrossData.getReviveTime();
		int protectTime = CrossBattleService.get().getProtectTime();
		if (now - reviveTargetTime < protectTime) {
			return TResult.valueOf(GameStatusCodeConstant.CROSS_BATTLE_PROTECT);
		}
		
//		Map<CrossBattleActorAttributeKey, Number> atts = new HashMap<>();
//		actorCrossData.setAttackTime(now);
//		actorCrossData.setReviveTime(0);// 攻击别人取消复活保护
//		atts.put(CrossBattleActorAttributeKey.ATTACK_TIME, actorCrossData.getAttackTime());
//		CrossBattlePushHelper.pushActorAttributeChange(serverCrossData.getServerId(), actorCrossData.actorId, atts, targetActorId, new HashMap<CrossBattleActorAttributeKey, Number>() );
//		CrossBattlePushHelper.pushActorAttributeChange(otherServerCrossData.getServerId(), actorCrossData.actorId, atts, targetActorId, new HashMap<CrossBattleActorAttributeKey, Number>() );

		AttackPlayerW2G attackPlayerResponse = new AttackPlayerW2G(targetActorId, actorCrossData.getFightModel().getBytes(),
				otherActorCrossData.getFightModel().getBytes(), actorCrossData.morale, otherActorCrossData.morale);
		return TResult.sucess(attackPlayerResponse);
	}
	
	/**
	 * 设置攻击时间
	 * @param now
	 * @param serverId
	 * @param actorId
	 * @param targetActorId
	 */
	private void setAttactTime(int now, int serverId, long actorId, long targetActorId) {
		Map<CrossBattleActorAttributeKey, Number> atts = new HashMap<>();
		ServerCrossData serverCrossData = serverData.get(serverId);
		ServerCrossData otherServerCrossData = serverData.get(serverCrossData.getOtherServerId());
		ActorCrossData actorCrossData = serverCrossData.getActorCrossData(actorId);
		actorCrossData.setAttackTime(now);
		actorCrossData.setReviveTime(0);// 攻击别人取消复活保护
		atts.put(CrossBattleActorAttributeKey.ATTACK_TIME, actorCrossData.getAttackTime());
		CrossBattlePushHelper.pushActorAttributeChange(serverCrossData.getServerId(), actorCrossData.actorId, atts, targetActorId, new HashMap<CrossBattleActorAttributeKey, Number>() );
		CrossBattlePushHelper.pushActorAttributeChange(otherServerCrossData.getServerId(), actorCrossData.actorId, atts, targetActorId, new HashMap<CrossBattleActorAttributeKey, Number>() );
	}

	@Override
	public void attackPlayerResult(int serverId, long actorId, AttackActorResultG2W attackPlayerResultRequest) {
		
		ServerCrossData serverCrossData = serverData.get(serverId);
		ServerCrossData otherServerCrossData = serverData.get(serverCrossData.getOtherServerId());
		ActorCrossData actorCrossData = serverCrossData.getActorCrossData(actorId);
		ActorCrossData targetActorCrossData = otherServerCrossData.getActorCrossData(attackPlayerResultRequest.targetActorId);
		
		Map<CrossBattleActorAttributeKey, Number> actorAtts = new HashMap<>();
		Map<CrossBattleActorAttributeKey, Number> targeactorAtts = new HashMap<>();
		Map<Integer, Long> hurtMap = new HashMap<>();
		
		
		int actorBaseHurt = CrossBattleHelper.getBaseTotalHurt(attackPlayerResultRequest.targetHurtMap);
		int targetActorBaseHurt = CrossBattleHelper.getBaseTotalHurt(attackPlayerResultRequest.actorHurtMap);
		int extActorHurt = 0;
		int targetExtActorHurt = 0;
		
		ChainLock lock = LockUtils.getLock(actorCrossData, targetActorCrossData, serverCrossData, otherServerCrossData);
		try {
			lock.lock();
			if (actorCrossData.isDead() == false && targetActorCrossData.isDead() == false) {
				actorCrossData.addAttackNum();
				//更新血
				actorCrossData.updateHero(attackPlayerResultRequest.actorHurtMap, attackPlayerResultRequest.targetHurtMap);
				targetActorCrossData.updateHero(attackPlayerResultRequest.targetHurtMap, attackPlayerResultRequest.actorHurtMap);
				
				//计算伤害数据
				if (actorCrossData.isDead()) {
					targetActorCrossData.updateKillNum();
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(String.format("actorId[%s]连杀数:[%s]", actorCrossData.actorId, actorCrossData.getContinueKillNum()));
					}
					actorAtts.put(CrossBattleActorAttributeKey.DEAD_TIME, actorCrossData.getDeadTime());
					targeactorAtts.put(CrossBattleActorAttributeKey.CONTINUE_KILL_NUM, targetActorCrossData.getContinueKillNum());
					targeactorAtts.put(CrossBattleActorAttributeKey.KILL_NUM, targetActorCrossData.getKillNum());
					targetExtActorHurt += CrossBattleHelper.killActorRewardHurt(actorCrossData.powerRank, actorCrossData.hpMax);
				} else if (targetActorCrossData.isDead()) {
					actorCrossData.updateKillNum();
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(String.format("actorId[%s]连杀数:[%s]", targetActorCrossData.actorId, targetActorCrossData.getContinueKillNum()));
					}
					targeactorAtts.put(CrossBattleActorAttributeKey.DEAD_TIME, targetActorCrossData.getDeadTime());
					actorAtts.put(CrossBattleActorAttributeKey.CONTINUE_KILL_NUM, actorCrossData.getContinueKillNum());
					actorAtts.put(CrossBattleActorAttributeKey.KILL_NUM, actorCrossData.getKillNum());
					extActorHurt += CrossBattleHelper.killActorRewardHurt(targetActorCrossData.powerRank, targetActorCrossData.hpMax);
				}
				if (actorCrossData.powerRank < targetActorCrossData.powerRank) {
					extActorHurt = CrossBattleHelper.powerRankRewardHurt(targetActorCrossData.powerRank, actorBaseHurt);
				} else {
					targetExtActorHurt = CrossBattleHelper.powerRankRewardHurt(actorCrossData.powerRank, targetActorBaseHurt);
				}
				
				// 更新获得伤害
				actorCrossData.addRewardHurt(extActorHurt + actorBaseHurt);
				serverCrossData.updateHurt(actorBaseHurt, extActorHurt );
				targetActorCrossData.addRewardHurt(targetExtActorHurt + targetActorBaseHurt);
				otherServerCrossData.updateHurt(targetActorBaseHurt, targetExtActorHurt);
				targeactorAtts.put(CrossBattleActorAttributeKey.TOTAL_HURT, targetActorCrossData.getRewardHurt());
				actorAtts.put(CrossBattleActorAttributeKey.TOTAL_HURT, actorCrossData.getRewardHurt());
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(String.format("服务器[%s]总伤害:[%s]", serverCrossData.getServerId(), serverCrossData.getTotalHurt()));
					LOGGER.debug(String.format("服务器[%s]总伤害:[%s]", otherServerCrossData.getServerId(), otherServerCrossData.getTotalHurt()));
				}
				
				//记录双方血变更
				actorAtts.put(CrossBattleActorAttributeKey.HP, actorCrossData.hp);
				targeactorAtts.put(CrossBattleActorAttributeKey.HP, targetActorCrossData.hp);
				
				//记录服务器伤害
				hurtMap.put(serverCrossData.getServerId(), serverCrossData.getTotalHurt());
				hurtMap.put(otherServerCrossData.getServerId(), otherServerCrossData.getTotalHurt());
				setAttactTime(TimeUtils.getNow(), serverId, actorId, targetActorCrossData.actorId);
				CrossBattlePushHelper.pushBattleResult(serverId, actorId, StatusCode.SUCCESS);
			} else {
				CrossBattlePushHelper.pushBattleResult(serverId, actorId,GameStatusCodeConstant.CROSS_BATTLE_REFIVE);
				return;
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
			CrossBattlePushHelper.pushBattleResult(serverId, actorId,GameStatusCodeConstant.CROSS_BATTLE_REFIVE);
			return;
		} finally {
			lock.unlock();
		}
		//推送战斗结果
		CrossBattlePushHelper.pushActorAttributeChange(serverCrossData.getServerId(), actorCrossData.actorId, actorAtts, targetActorCrossData.actorId, targeactorAtts );
		CrossBattlePushHelper.pushActorAttributeChange(otherServerCrossData.getServerId(), targetActorCrossData.actorId, targeactorAtts, actorCrossData.actorId, actorAtts  );
		CrossBattlePushHelper.pushTotalHurtChange(serverCrossData.getServerId(), hurtMap);
		CrossBattlePushHelper.pushTotalHurtChange(otherServerCrossData.getServerId(), hurtMap);
		AttackNoticeVO attackNoticeVO = new AttackNoticeVO(ATTACK, targetActorCrossData.actorName, actorBaseHurt, extActorHurt);
		CrossBattlePushHelper.pushAttackNotice(serverCrossData.getServerId(), actorId, attackNoticeVO);
		attackNoticeVO = new AttackNoticeVO(BE_ATTACK, actorCrossData.actorName, targetActorBaseHurt, targetExtActorHurt);
		CrossBattlePushHelper.pushAttackNotice(otherServerCrossData.getServerId(), attackPlayerResultRequest.targetActorId, attackNoticeVO);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable() {

			@Override
			public void run() {

				if (CrossBattleService.isOpenDate() && CrossBattleService.signupStart() && isSignup == false) {
					signupStart();
				}

				if (CrossBattleService.isOpen() && CrossBattleService.isOpenTime() && isStart == false && isSignup) {
					start();
					return;
				}

				if (isStart == true && (CrossBattleService.isOpen() == false || CrossBattleService.isOpenTime() == false)) {
					end();
					return;
				}
			}
		}, 1);
		
		schedule.addEverySecond(new Runnable() {
			
			@Override
			public void run() {
				if (isStart == false) {
					return;
				}
				CrossBattleConfig cfg = CrossBattleService.get();
				for (Entry<Integer, ServerCrossData> entry : serverData.entrySet()) {
					ServerCrossData serverCrossData = entry.getValue();
					for (ActorCrossData actorCrossData : serverCrossData.getActorCrossDataMap().values()) {
						ChainLock lock = LockUtils.getLock(actorCrossData);
						Map<CrossBattleActorAttributeKey, Number> atts = new HashMap<>();
						try {
							lock.lock();
							if (actorCrossData.getDeadTime() > 0 && cfg.getReliveTime() <= TimeUtils.getNow() - actorCrossData.getDeadTime()) {
								actorCrossData.revive();
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug(String.format("actorId:[%s] 复活, 当前hp:[%s]", actorCrossData.actorId, actorCrossData.hp));
								}
								atts.put(CrossBattleActorAttributeKey.HP, actorCrossData.hp);
								atts.put(CrossBattleActorAttributeKey.REVIVE_TIME, actorCrossData.getReviveTime());
							}
						} catch (Exception e) {
							
						} finally {
							lock.unlock();
						}
						if (atts.isEmpty() == false) {
							
							CrossBattlePushHelper.pushActorAttributeChange(serverCrossData.getServerId(), actorCrossData.actorId, atts, 0, new HashMap<CrossBattleActorAttributeKey, Number>() );
							CrossBattlePushHelper.pushActorAttributeChange(serverCrossData.getOtherServerId(), actorCrossData.actorId, atts, 0, new HashMap<CrossBattleActorAttributeKey, Number>() );
						}
					}
				}
			}
		}, 1);
	}

	@Override
	public TResult<ActorCrossDataW2G> getCrossData(int serverId, long actorId) {
		if (isStart == false) {
			return TResult.valueOf(GameStatusCodeConstant.CROSS_BATTLE_NOT_START);
		}
		CrossBattleDayConfig cfg = CrossBattleDayService.get(CrossBattleService.getDayNum(), serverId);
		if (cfg == null) {
			return TResult.valueOf(GameStatusCodeConstant.CROSS_DATA_NOT_EXSIT);
		}
		int awayServerId = 0;
		if (serverId == cfg.getHomeServerId()) {
			awayServerId = cfg.getAwayServerId();
		} else {
			awayServerId = cfg.getHomeServerId();
		}
		ServerCrossData awayServerCrossData = serverData.get(awayServerId);
		ServerCrossData homServerCrossData = serverData.get(serverId);
		if (awayServerCrossData == null || homServerCrossData == null) {
			return TResult.valueOf(GameStatusCodeConstant.CROSS_DATA_NOT_EXSIT);
		}
		CrossData crossData = new CrossData(homServerCrossData.getTotalHurt(), awayServerCrossData.getTotalHurt(), homServerCrossData
				.getAttributeRankList(), awayServerCrossData.getAttributeRankList(), homServerCrossData.getServerId(), awayServerCrossData.getServerId());
		ActorCrossDataW2G actorCrossDataW2G = new ActorCrossDataW2G(crossData);
		return TResult.sucess(actorCrossDataW2G);
	}

	@Override
	public TResult<LineupW2G> getLineup(int serverId, long actorId, long targetActorId) {
		if (isStart == false) {
			return TResult.valueOf(GameStatusCodeConstant.CROSS_BATTLE_NOT_START);
		}
		ActorCrossData actorCrossData = null;
		for (ServerCrossData serverCrossData : serverData.values()) {
			ActorCrossData data = serverCrossData.getActorCrossData(targetActorId);
			if (data != null && data.actorId == targetActorId) {
				actorCrossData = data;
				break;
			}
		}
		if (actorCrossData == null) {
			return TResult.valueOf(GameStatusCodeConstant.CROSS_DATA_NOT_EXSIT);
		}
		ViewLineupVO viewLineupVO = actorCrossData.getViewLineupVO();
		LineupW2G lineupW2G = new LineupW2G(viewLineupVO);
		return TResult.sucess(lineupW2G);
	}

	@Override
	public TResult<ServerRankW2G> getServerRank(int serverId, long actorId) {
		Map<Integer, List<ServerScoreVO>> map = new HashMap<Integer, List<ServerScoreVO>>();
		List<CrossBattle> list = crossBattleDao.getList();
		for (CrossBattle crossBattle : list) {
			List<ServerScoreVO> groupList = null;
			if (map.containsKey(crossBattle.groupId) == false) {
				groupList = new ArrayList<>();
				map.put(crossBattle.groupId, groupList);
			} else {
				groupList = map.get(crossBattle.groupId);
			}
			ServerScoreVO serverScoreVO = new ServerScoreVO(crossBattle.groupId, crossBattle.serverId, crossBattle.bestActorName, crossBattle.score);
			groupList.add(serverScoreVO);
		}
		ServerScoreList serverRank = new ServerScoreList(map);
		ServerRankW2G serverRankW2G = new ServerRankW2G(serverRank );
		return TResult.sucess(serverRankW2G);
	}

	@Override
	public TResult<HomeServerRankW2G> getHomeServerRank(int serverId, long actorId) {
		List<HomeServerRankVO> list = new ArrayList<>();
		
		List<CrossBattleHurtRank> hurtRanks = crossBattleHurtRankDao.getCrossBattleServerHurtRank(serverId);
		int selfRank = 0;
		for (CrossBattleHurtRank hurtRank : hurtRanks) {
			if (hurtRank.actorId == actorId) {
				selfRank = hurtRank.rank;
			}
			HomeServerRankVO homeServerRankVO = new HomeServerRankVO(hurtRank.actorName, hurtRank.totalHurt, hurtRank.totalKillNum);
			list.add(homeServerRankVO);
		}
		HomeServerRank homeServerRank = new HomeServerRank(selfRank, list);
		HomeServerRankW2G homeServerRankW2G = new HomeServerRankW2G(homeServerRank);
		return TResult.sucess(homeServerRankW2G);
	}

	@Override
	public TResult<EndRewardW2G> dayBattleEndRewardResult(int serverId, long actorId) {
		CrossBattle crossBattle = crossBattleDao.getCrossBattle(serverId);
		CrossBattleHurtRank crossBattleHurtRank = crossBattleHurtRankDao.getCrossBattleHurtRank(actorId, serverId);
		DayEndRewardVO endReward = new DayEndRewardVO(crossBattle.score, crossBattleHurtRank.totalHurt, crossBattleHurtRank.rank,
				crossBattleHurtRank.dayRankExchangePoint, crossBattleHurtRank.totalKillNum, crossBattleHurtRank.dayKillExchangePoint, 
				crossBattleHurtRank.dayWinServerExchangePoint, crossBattleHurtRank.dayGoodsRecordList, crossBattle.dayIsWin);
		EndRewardW2G endRewardW2G = new EndRewardW2G(endReward);
		return TResult.sucess(endRewardW2G);
	}

	@Override
	public TResult<ExchangePointW2G> exchangePoint(int serverId, long actorId, int configId) {
		CrossBattleExchangeConfig exchangeConfig = CrossBattleService.getExchangeConfig(configId);
		if(exchangeConfig == null){
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		CrossBattleActor crossBattleActor = crossBattleActorDao.getCrossBattleActor(actorId, serverId);
		int point = 0;
		ChainLock lock = LockUtils.getLock(crossBattleActor);
		try {
			lock.lock();
			int cost = exchangeConfig.getCostExchangePoint();
			point = crossBattleActor.costExchangePoint(cost);
			if (point < 0) {
				return TResult.valueOf(GameStatusCodeConstant.CROSS_BATTLE_POINT_NOT_ENOUGH);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("serverId:[{}] actorId:[{}] 兑换[{}] 剩余[{}]积分", serverId, actorId, configId, point);
			}
			crossBattleActorDao.update(crossBattleActor);
			WorldOssLogger.exchangePointDecrease(serverId, actorId, cost, crossBattleActor.exchangePoint);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		List<RewardObject> rewardList = exchangeConfig.getRewardList();
		ExchangePointW2G exchangePointW2G = new ExchangePointW2G(rewardList, point);
		return TResult.sucess(exchangePointW2G);
	}
	
	@Override
	public TResult<CrossBattleConfigW2G> getConfig(long actorId) {
		CrossBattleConfig config = CrossBattleService.get();
		int dayNum = CrossBattleService.getDayNum();
		Map<Integer, Integer> map = CrossBattleDayService.getServerMap(dayNum);
		byte readFlag = 1;
		if (rewardReadFlag.containsKey(actorId)) {
			boolean flag = rewardReadFlag.get(actorId);
			readFlag = flag? (byte) 1:0;
		}
		CrossBattleConfigW2G battleConfigW2G = new CrossBattleConfigW2G(config.startDate, config.endDate, config.startTime, config.signupTime, config.endTime, map, readFlag);
		return TResult.sucess(battleConfigW2G);
	}

	@Override
	public TResult<ActorPointW2G> getActorPoint(long actorId,int serverId) {
		CrossBattleActor crossBattleActor = crossBattleActorDao.getCrossBattleActor(actorId, serverId);
		ActorPointW2G actorPointW2G = new ActorPointW2G(crossBattleActor.exchangePoint);
		return TResult.sucess(actorPointW2G);
	}

	@Override
	public TResult<LastBattleResultW2G> getLastBattleResult(long actorId) {
		int dayNum = CrossBattleService.getDayNum();
		
		Date now = new Date();
		if (now.before(CrossBattleService.get().getEndTime())) { // 获取上次
			if(dayNum > 1){
				dayNum = dayNum - 1;
			} else {  //第一天就不处理
				dayNum = 0;
			}
		} 
		
		List<LastBattleResultVO> list = new ArrayList<>();
		if (dayNum > 0) {
			Map<Integer, Integer> map = CrossBattleDayService.getServerMap(dayNum);
			for (Entry<Integer, Integer> entry : map.entrySet()) {
				LastBattleResultVO vo = new LastBattleResultVO();
				CrossBattle crossBattle = crossBattleDao.getCrossBattle(entry.getKey());
				vo.homeServerId = entry.getKey();
				vo.otherServerId = entry.getValue();
				if(crossBattle.dayIsWin == CrossBattleResult.WIN.getId()){
					vo.winServerId = entry.getKey();
				}else if (crossBattle.dayIsWin == CrossBattleResult.DRAW.getId()){
					vo.winServerId = 0;
				} else {
					vo.winServerId = entry.getValue();
					
				}
				list.add(vo);
			}
		}
		
		LastBattleResultW2G lastBattleResultW2G = new LastBattleResultW2G(list);
		return TResult.sucess(lastBattleResultW2G);
	}

	@Override
	public Result setReadFlag(long actorId) {
		if (rewardReadFlag.containsKey(actorId)) {
			rewardReadFlag.put(actorId, true);
		}
		return Result.valueOf();
	}
	
}
