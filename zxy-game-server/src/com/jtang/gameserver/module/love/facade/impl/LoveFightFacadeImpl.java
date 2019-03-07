package com.jtang.gameserver.module.love.facade.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.LoveGlobalConfig;
import com.jtang.gameserver.dataconfig.model.LoveRankRewardConfig;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.service.GmService;
import com.jtang.gameserver.dataconfig.service.LoveService;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Love;
import com.jtang.gameserver.dbproxy.entity.LoveRank;
import com.jtang.gameserver.module.battle.constant.BattleSkipPlayType;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackPlayerRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.love.dao.LoveDao;
import com.jtang.gameserver.module.love.dao.LoveFightDao;
import com.jtang.gameserver.module.love.facade.LoveFightFacade;
import com.jtang.gameserver.module.love.handler.response.FightVideoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveFightInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveFightResponse;
import com.jtang.gameserver.module.love.handler.response.LoveRankInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveRankListResponse;
import com.jtang.gameserver.module.love.helper.LovePushHelper;
import com.jtang.gameserver.module.love.model.FightVO;
import com.jtang.gameserver.module.love.model.LoveFightVO;
import com.jtang.gameserver.module.love.model.LoveRankInfo;
import com.jtang.gameserver.module.notify.dao.FightVideoDao;
import com.jtang.gameserver.module.notify.type.FightVideoRemoveType;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.sysmail.type.SysmailType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class LoveFightFacadeImpl implements LoveFightFacade,BattleCallBack,ActorLoginListener, ApplicationListener<ContextRefreshedEvent>,ZeroListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoveFightFacadeImpl.class);
	
	@Autowired
	private LoveDao loveDao;
	@Autowired
	private LoveFightDao loveFightDao;
	@Autowired
	private BattleFacade battleFacade;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private IconFacade iconFacade;
	@Autowired
	private FightVideoDao fightVideoDao;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private Schedule schedule;
	@Autowired
	private SysmailFacade sysmailFacade;
	
	/**
	 * 交换排名锁
	 */
	private static Object CHANGE_RANK_LOCK = new Object();
	
	
	@Override
	public TResult<LoveRankInfoResponse> getInfo(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return TResult.valueOf(GameStatusCodeConstant.NOT_RANK);
		}
		LoveRank loveRank = loveFightDao.get(actorId,love.getLoveActorId());
		int power = generateLoveFightVO(loveRank).power;
		LoveRankInfoResponse response = new LoveRankInfoResponse(love,loveRank.rank,power);
		return TResult.sucess(response);
	}
	
	@Override
	public Result loveFight(long actorId,long targetActorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_RANK);
		}
		Love targetLove = loveDao.get(targetActorId);
		if(targetLove.married() == false){
			return Result.valueOf(GameStatusCodeConstant.TARGET_NOT_RANK);
		}
		LoveGlobalConfig globalConfig = LoveService.getGlobalConfig();
		if(globalConfig.fightNum <= love.rankFightNum){
			return Result.valueOf(GameStatusCodeConstant.LOVE_RANK_FIGHT_NOT_ENOUGH);
		}
		Actor actor = actorFacade.getActor(actorId);
		Actor targetActor = actorFacade.getActor(targetLove.getPkId());
		
		LineupFightModel atkTeam = LineupHelper.getAllyLineupFight(actorId,love.getLoveActorId());
		LineupFightModel defTeam = LineupHelper.getAllyLineupFight(targetLove.getPkId(),targetLove.getLoveActorId());
		MapConfig map = MapService.get(5);
		

		AttackPlayerRequest attackReq = new AttackPlayerRequest(EventKey.POWER_BATTLE, map, actorId, atkTeam, targetLove.getPkId(), defTeam, actor.morale,
				targetActor.morale, null, BattleType.LOVE_FIGHT);

		Result result = battleFacade.submitAtkPlayerRequest(attackReq, this);
		return result;
	}

	@Override
	public void execute(BattleResult result) {
		AttackPlayerRequest attackReq = (AttackPlayerRequest) result.battleReq;
		FightData data = result.fightData;
		data.setCanSkipPlay(BattleSkipPlayType.PVP_CLIENT_DECIDE);
		WinLevel winLevel = data.result;
		Love love1 = loveDao.get(attackReq.actorId);
		LoveRank loveRank1 = loveFightDao.get(love1.getPkId(),love1.getLoveActorId());
		Love love2 = loveDao.get(attackReq.targetActorId);
		LoveRank loveRank2 = loveFightDao.get(love2.getPkId(),love2.getLoveActorId());
		LoveFightResponse response = new LoveFightResponse(data,loveRank2.rank);
		synchronized(CHANGE_RANK_LOCK){
			love1.rankFightNum += 1;
			love1.rankFightTime = TimeUtils.getNow();
			loveDao.update(love1);
			if (winLevel.isWin()) {
				if(loveRank1.rank > loveRank2.rank){
					//对方排名变更给对方记录战斗录像
					long videoId = fightVideoDao.create(loveRank1.loveId1, loveRank2.loveId1, data.getBytes());
					FightVO fightInfoVO = new FightVO();
					fightInfoVO.actorId = loveRank1.loveId1;
					fightInfoVO.fightResult = 1;
					fightInfoVO.fightVideo = videoId;
					fightInfoVO.fightTime = TimeUtils.getNow();
					//战斗记录满了新增条数替换最旧条数
					LoveGlobalConfig globalConfig = LoveService.getGlobalConfig();
					if(loveRank2.fightList.size() >= globalConfig.fightInfo){
						FightVO fightVO = loveRank2.fightList.remove();
						fightVideoDao.remove(fightVO.actorId, fightVO.fightVideo, FightVideoRemoveType.TYPE1);
					}
					loveRank2.fightList.add(fightInfoVO);
					//推送信息告诉对方两个人
					LovePushHelper.pushLoveFightInfo(loveRank2.loveId1, getFightInfo(loveRank2.loveId1).item);
					LovePushHelper.pushLoveFightInfo(loveRank2.loveId2, getFightInfo(loveRank2.loveId2).item);
					//进行排名交换
					loveFightDao.changeRank(loveRank1, loveRank2);
					//TODO 发送世界公告
				}
			}
		}
		LovePushHelper.pushFightData(attackReq.actorId, response);
	}

	@Override
	public Result buyFightNum(long actorId) {
		LoveGlobalConfig globalConfig = LoveService.getGlobalConfig();
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_RANK);
		}
		ChainLock lock = LockUtils.getLock(love);
		try{
			lock.lock();
			if(love.rankFightNum == 0){
				return Result.valueOf(GameStatusCodeConstant.FIGHT_NUM_NOT_USE);
			}
			if(love.rankFlushNum >= globalConfig.ticketMap.size()){
				return Result.valueOf(GameStatusCodeConstant.FLUSH_FIGHT_NUM_MAX);
			}
			int ticketNum = globalConfig.getCostTicket(love.rankFlushNum + 1);
			if(vipFacade.decreaseTicket(actorId, TicketDecreaseType.LOVE_FIGHT_NUM, ticketNum, 0, 0) == false){
				return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
			}
			love.rankFightNum = 0;
			love.rankFlushNum += 1;
			love.rankFlushTime = TimeUtils.getNow();
			loveDao.update(love);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}

	@Override
	public TResult<LoveRankListResponse> getTopRank() {
		int viewRank = LoveService.getGlobalConfig().viewRank;
		Collection<LoveRank> loveRankList = loveFightDao.getTopRanks(1, viewRank);
		List<LoveRankInfo> fightVOList = new ArrayList<>();
		for(LoveRank r : loveRankList){
			fightVOList.add(generateLoveFightVO(r));
		}
		LoveRankListResponse response = new LoveRankListResponse(fightVOList);
		return TResult.sucess(response);
	}
	
	@Override
	public TResult<LoveRankListResponse> getFightRank(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return TResult.valueOf(GameStatusCodeConstant.NOT_RANK);
		}
		LoveRank loveRank = loveFightDao.get(actorId, love.getLoveActorId());
		LoveGlobalConfig globalConfig = LoveService.getGlobalConfig();
		Collection<LoveRank> rankList = loveFightDao.getRankList(globalConfig.viewRank, loveRank.rank, globalConfig.viewUp, globalConfig.viewDown, globalConfig.getUpList(loveRank.rank));
		List<LoveRank> fightList = new ArrayList<>();
		List<LoveRank> upList = new ArrayList<>();
		List<LoveRank> downList = new ArrayList<>();
		List<LoveRankInfo> list = new ArrayList<>();
		for (LoveRank r : rankList) {
			if(actorId == r.loveId1 || actorId == r.loveId2){
				continue;
			}else if (globalConfig.getUpList(loveRank.rank).contains(r.rank)) {
				fightList.add(r);
			} else if (r.rank < loveRank.rank && r.rank >= Math.max(1,loveRank.rank - globalConfig.viewUp)) {
				upList.add(r);
			} else {
				downList.add(r);
			}
		}
		int size = globalConfig.upListNum + globalConfig.upRankNum + globalConfig.downRankNum;
		if(fightList.isEmpty() == false){//向上节点人数
			int[] randomPower = RandomUtils.uniqueRandom(size - globalConfig.upRankNum - globalConfig.downRankNum, 0, fightList.size() - 1);
			for (int i = 0; i < randomPower.length; i++) {
				LoveRank r = fightList.get(randomPower[i]);
				list.add(generateLoveFightVO(r));
				size --;
			}
		}
		if(upList.isEmpty() == false){//向上周围人数
			int[] randomPower = RandomUtils.uniqueRandom(size - globalConfig.downRankNum, 0, upList.size() -1);
			for(int i = 0; i < randomPower.length; i++){
				LoveRank r = upList.get(randomPower[i]);
				list.add(generateLoveFightVO(r));
				size --;
			}
		}
		if(downList.isEmpty() == false){//向下周围人数
			int[] randomPower = RandomUtils.uniqueRandom(size, 0, downList.size() -1);
			for(int i = 0; i < randomPower.length; i++){
				LoveRank r = downList.get(randomPower[i]);
				list.add(generateLoveFightVO(r));
			}
		}
		LoveRankListResponse response = new LoveRankListResponse(new ArrayList<>(list));
		return TResult.sucess(response);
	}

	@Override
	public TResult<LoveFightInfoResponse> getFightInfo(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return TResult.valueOf(GameStatusCodeConstant.NOT_RANK);
		}
		LoveRank loveRank = loveFightDao.get(actorId, love.getLoveActorId());
		LoveFightInfoResponse response = new LoveFightInfoResponse();
		for(FightVO vo:loveRank.fightList){
			getFightInfoVO(response, vo);
		}
		return TResult.sucess(response);
	}

	@Override
	public TResult<FightVideoResponse> getFightVideo(long actorId,long fightVideoId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return TResult.valueOf(GameStatusCodeConstant.NOT_RANK);
		}
		LoveRank loveRank = loveFightDao.get(actorId, love.getLoveActorId());
		List<FightVO> list = new ArrayList<>();
		list.addAll(loveRank.fightList);
		byte[] bytes = null;
		for(FightVO fightInfo:loveRank.fightList){
			if(fightInfo.fightVideo == fightVideoId){
				bytes = fightVideoDao.get(fightVideoId);
			}
		}
		if(bytes == null){
			return TResult.valueOf(GameStatusCodeConstant.FIGHT_VIDEO_NOT_FIND);
		}
		FightVideoResponse response = new FightVideoResponse();
		response.fightData = bytes;
		return TResult.sucess(response);
	}
	
	@Override
	public Result removeRank(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_RANK);
		}
		loveFightDao.remove(actorId,love.getLoveActorId());
		return Result.valueOf();
	}
	
	@Override
	public Result unMarry(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_MARRY);
		}
		loveFightDao.remove(actorId,love.getLoveActorId());
		return Result.valueOf();
	}

	@Override
	public void onZero() {
		for(Long actorId:playerSession.onlineActorList()){
			Love love = loveDao.get(actorId);
			ChainLock lock = LockUtils.getLock(love);
			try{
				lock.lock();
				if(love.married() == false){
					return;
				}
				love.reset();
				loveDao.update(love);
				LovePushHelper.pushLoveRankReset(actorId);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addFixedTime(new Runnable() {
			@Override
			public void run() {
				//每次发奖之前将表排名检查
				loveFightDao.formatTable();
				LoveGlobalConfig globalConfig = LoveService.getGlobalConfig();
				Collection<LoveRank> loveRankList = loveFightDao.getTopRanks(globalConfig.minRank,globalConfig.maxRank);
				Map<Long, Integer> rewardActorMaps = new HashMap<>();
				for (LoveRank loveRank : loveRankList) {
					rewardActorMaps.put(loveRank.loveId1, loveRank.rank);
					rewardActorMaps.put(loveRank.loveId2, loveRank.rank);
				}
				
				//奖励物品
				for (Entry<Long, Integer> powerEntry : rewardActorMaps.entrySet()) {
					long actorId = powerEntry.getKey();
					int rank = powerEntry.getValue();

					LoveRankRewardConfig config = LoveService.getRankReward(rank);
					if (config == null) {
						continue;
					}
					
					// 发送奖励
					sysmailFacade.sendSysmail(actorId, SysmailType.LOVE_RANK, config.rewardList,String.valueOf(rank));
					
				}
			}
		},LoveService.getGlobalConfig().sendTime);
	}

	@Override
	public void onLogin(long actorId) {
		Love love = loveDao.get(actorId);
		if(GmService.isGm(actorId) == false && love.married()){
			if(love.rankFightTime != 0 && DateUtils.isToday(love.rankFightTime) == false){
				ChainLock lock = LockUtils.getLock(love);
				try{
					lock.lock();
					love.reset();
					loveDao.update(love);
				}catch(Exception e){
					LOGGER.error("{}",e);
				}finally{
					lock.unlock();
				}
			}
		}
	}
	
	private LoveRankInfo generateLoveFightVO(LoveRank loveRank) {
		Actor actor1 = actorFacade.getActor(loveRank.loveId1);
		Actor actor2 = actorFacade.getActor(loveRank.loveId2);
		IconVO iconVO1 = iconFacade.getIconVO(loveRank.loveId1);
		IconVO iconVO2 = iconFacade.getIconVO(loveRank.loveId2);
		int vipLevel1 = vipFacade.getVipLevel(loveRank.loveId1);
		int vipLevel2 = vipFacade.getVipLevel(loveRank.loveId2);
		if (actor1 != null && actor2!=null && loveRank != null && iconVO1 != null && iconVO2 != null){
			LineupFightModel lineup1 = LineupHelper.getLineupFight(loveRank.loveId1);
			float atk = 0f;
			float def = 0f;
			float hp = 0f;
			for(Entry<Integer,HeroVO> entry:lineup1.getHeros().entrySet()){
				HeroVO heroVO = entry.getValue();
				Map<AttackerAttributeKey, Integer> map = LineupHelper.getInstance().getEquipAndBaseAtt(loveRank.loveId1, heroVO.heroId);
				atk += map.get(AttackerAttributeKey.ATK);
				def += map.get(AttackerAttributeKey.DEFENSE);
				hp += map.get(AttackerAttributeKey.HP);
			}
			int powerNum1 = (int) Math.ceil(atk/10 + def/20 + hp/10);
			LineupFightModel lineup2 = LineupHelper.getLineupFight(loveRank.loveId2);
			atk = 0f;
			def = 0f;
			hp = 0f;
			for(Entry<Integer,HeroVO> entry:lineup2.getHeros().entrySet()){
				HeroVO heroVO = entry.getValue();
				Map<AttackerAttributeKey, Integer> map = LineupHelper.getInstance().getEquipAndBaseAtt(loveRank.loveId2, heroVO.heroId);
				atk += map.get(AttackerAttributeKey.ATK);
				def += map.get(AttackerAttributeKey.DEFENSE);
				hp += map.get(AttackerAttributeKey.HP);
			}
			int powerNum2 = (int) Math.ceil(atk/10 + def/20 + hp/10);
			LoveRankInfo loveRankInfo = new LoveRankInfo(actor1, actor2, vipLevel1, vipLevel2, iconVO1, iconVO2, loveRank.rank, powerNum1 + powerNum2);
			return loveRankInfo;
		} else {
			return null;
		}
	}
	
	private void getFightInfoVO(LoveFightInfoResponse response, FightVO vo) {
		LoveFightVO fightVO = new LoveFightVO();
		fightVO.iconVO = iconFacade.getIconVO(vo.actorId);
		fightVO.actorId = vo.actorId;
		fightVO.actorName = actorFacade.getActor(vo.actorId).actorName;
		fightVO.vipLevel = vipFacade.getVipLevel(vo.actorId);
		fightVO.actorLevel = ActorHelper.getActorLevel(vo.actorId);
		fightVO.fightResult = vo.fightResult;
		fightVO.fightVideoId = vo.fightVideo;
		fightVO.fightTime = vo.fightTime;
		response.fightInfo.add(fightVO);
	}

}
