package com.jtang.gameserver.module.love.facade.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.LoveMonsterConfig;
import com.jtang.gameserver.dataconfig.model.LoveMonsterGlobalConfig;
//import com.jtang.gameserver.dataconfig.model.LoveMonsterLeastConfig;
import com.jtang.gameserver.dataconfig.model.LoveMonsterRewardConfig;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.service.LoveService;
import com.jtang.gameserver.dataconfig.service.MapService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Love;
import com.jtang.gameserver.dbproxy.entity.LoveMonster;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.model.AttackMonsterRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.love.dao.LoveDao;
import com.jtang.gameserver.module.love.dao.LoveMonsterDao;
import com.jtang.gameserver.module.love.facade.LoveMonsterFacade;
import com.jtang.gameserver.module.love.handler.response.LoveMonsterFightResponse;
import com.jtang.gameserver.module.love.handler.response.LoveMonsterInfoResponse;
import com.jtang.gameserver.module.love.helper.LovePushHelper;
import com.jtang.gameserver.module.love.model.BossFightVO;
import com.jtang.gameserver.module.love.model.BossStateVO;
import com.jtang.gameserver.module.love.model.BossVO;
import com.jtang.gameserver.module.monster.facade.LoveDemonMonsterFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class LoveMonsterFacadeImpl implements LoveMonsterFacade,BattleCallBack,ZeroListener,ActorLoginListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoveMonsterFacadeImpl.class);
	
	@Autowired
	private LoveDao loveDao;
	@Autowired
	private LoveMonsterDao loveMonsterDao;
	@Autowired
	private LoveDemonMonsterFacade loveDemonMonsterFacade;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private BattleFacade battleFacade;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	private static String DEF_ID = "DEF_ID";
	private static String LEVEL = "LEVEL";
	
	@Override
	public TResult<LoveMonsterInfoResponse> getInfo(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return TResult.valueOf(GameStatusCodeConstant.NOT_MONSTER);
		}
		LoveMonster loveMonster = loveMonsterDao.get(actorId, love.getLoveActorId());
		ChainLock lock = LockUtils.getLock(love,loveMonster);
		try{
			lock.lock();
			if(love.fightStateMap.size() == 0){//如果没有boss状态数据则初始化
				love.fightStateMap = LoveService.initFight();
				loveDao.update(love);
			}
			int level = ActorHelper.getActorLevel(actorId) + ActorHelper.getActorLevel(love.getLoveActorId());
			if(loveMonster.bossMap.size() == 0){//如果没有boss血量数据则初始化
				loveMonster.bossMap = LoveService.initMonster(level);
				loveMonsterDao.update(loveMonster);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		Map<Integer,BossStateVO> stateMap = parserBossVO(love,loveMonster);
		LoveMonsterInfoResponse response = new LoveMonsterInfoResponse(stateMap);
		return TResult.sucess(response);
	}

	@Override
	public Result startFight(long actorId, int id) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_MONSTER);
		}
		LoveMonsterGlobalConfig globalConfig = LoveService.getLoveMonsterGlobalConfig(id);
		if(globalConfig == null){
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		LoveMonster loveMonster = loveMonsterDao.get(actorId, love.getLoveActorId());
		Result result = Result.valueOf();
		ChainLock lock = LockUtils.getLock(loveMonster,love);
		try{
			lock.lock();
			if(loveMonster.bossMap.containsKey(id) == false){
				return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
			}
			BossFightVO bossFightVO = love.fightStateMap.get(id);
			if(bossFightVO.state == 0){
				return Result.valueOf(GameStatusCodeConstant.BOSS_LOCKED);
			}
			if(bossFightVO.state == 2){
				return Result.valueOf(GameStatusCodeConstant.BOOS_KILLED);
			}
			if(bossFightVO.monsterFightNum >= globalConfig.fightNum){
				return Result.valueOf(GameStatusCodeConstant.FIGHT_NUM_ENOUGH);
			}
			Actor actor1 = actorFacade.getActor(actorId);
			Actor actor2 = actorFacade.getActor(love.getLoveActorId());
			int level = actor1.level + actor2.level;
			BossVO bossVO = loveMonster.bossMap.get(id);
			//攻击之前判断boss是否已经死亡
			Map<Integer, MonsterVO> monsterMap = getMonsterVO(id, bossVO, level);
			boolean bossDie = true;
			for(MonsterVO monsterVO : monsterMap.values()){
				if (monsterVO.getHp() != 0) {
					bossDie = false;
					break;
				}
			}
			if(bossDie){
				return Result.valueOf(GameStatusCodeConstant.BOOS_KILLED);
			}
			
			LineupFightModel model = LineupHelper.getAllyLineupFight(actorId, love.getLoveActorId());
			int morale = actor1.morale + actor2.morale;
			MapConfig map = MapService.get(globalConfig.map);
			LoveMonsterConfig monsterConfig = LoveService.getLoveMonsterConfig(id);
			
			Map<String,Object> args = new HashMap<>();
			args.put(DEF_ID, id);
			args.put(LEVEL, level);
			
			AttackMonsterRequest event = new AttackMonsterRequest(EventKey.STORY_BATTLE, map, actorId, model, monsterMap, morale, monsterConfig.getMorale(), args,
					BattleType.STORY);
			result = battleFacade.submitAtkMonsterRequest(event, this);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return result;
	}

	@Override
	public void execute(BattleResult result) {
		FightData fightData = result.fightData;
		fightData.setCanSkipPlay((byte)2);
		@SuppressWarnings("unchecked")
		Map<String,Object> args = (Map<String, Object>) result.battleReq.args;
		int id = (int) args.get(DEF_ID);
		int level = (int) args.get(LEVEL);
		Love love1 = loveDao.get(result.battleReq.actorId);
		LoveMonster loveMonster = loveMonsterDao.get(love1.getPkId(), love1.getLoveActorId());
		ChainLock lock = LockUtils.getLock(love1,loveMonster);
		try{
			lock.lock();
			//数据计算之前判断boss是否已经死亡
			boolean bossDie = true;
			for(MonsterVO monsterVO : getMonsterVO(id, loveMonster.bossMap.get(id), level).values()){
				if (monsterVO.getHp() != 0) {
					bossDie = false;
					break;
				}
			}
			if(bossDie){
				LovePushHelper.pushBossFightError(love1.getPkId(),GameStatusCodeConstant.BOOS_KILLED);
				return;
			}
			//根据伤害扣除boss血量
			BossVO bossVO = loveMonster.bossMap.get(id);
			Collection<MonsterVO> boss = getMonsterVO(id, loveMonster.bossMap.get(id), level).values();
			for (Fighter fighter : result.defendsTeam) {
				for (MonsterVO monsterVO : boss) {
					if (fighter.getHeroId() == monsterVO.getHeroId()) {
						int hp = monsterVO.getHp() - fighter.getHert();
						hp = hp < 0 ? 0 : hp;
						if(hp <= 0){
							bossVO.monsterHPMap.put(monsterVO.getHeroId(),0);
						}else{
							bossVO.monsterHPMap.put(monsterVO.getHeroId(), hp);
						}
						monsterVO.setHp(hp);
						bossVO.lastHurtNum += fighter.getHert();
						continue;
					}
				}
			}
			
			int hp = 0;
			for(Integer i : bossVO.monsterHPMap.values()){
				hp += i;
			}
			//给双方推送boss血量数据
			LovePushHelper.pushBossHp(love1.getPkId(),id,hp);
			LovePushHelper.pushBossHp(love1.getLoveActorId(),id,hp);
			
			bossDie = true;
			for(MonsterVO monsterVO : boss){
				if (monsterVO.getHp() != 0) {
					bossDie = false;
					break;
				}
			}
			
			BossFightVO bossFightVO = love1.fightStateMap.get(id);
			bossFightVO.monsterFightNum ++;
			love1.fightTime = TimeUtils.getNow();
			loveDao.update(love1);
			
			//boss如果被击杀,给双方发放奖励
			LoveMonsterGlobalConfig globalConfig = LoveService.getLoveMonsterGlobalConfig(id);
			if(bossDie){
				love1.fightStateMap.get(id).state = 2;
				love1.fightStateMap.get(id).rewardList = new ArrayList<>(globalConfig.rewardList);
				loveDao.update(love1);
				
				Love love2 = loveDao.get(love1.getLoveActorId());
				if(love2.fightStateMap.isEmpty()){//结缘对象在模块上线后没登陆,给发奖需要初始化boss状态列表
					love2.fightStateMap = LoveService.initFight();
				}
				if(love2.fightStateMap.get(id).state != 0){//如果结婚对象未解锁此boss不能获取奖励
//					love2.fightTime = TimeUtils.getNow();
					love2.fightStateMap.get(id).state = 2;
					love2.fightStateMap.get(id).rewardList = new ArrayList<>(globalConfig.rewardList);
					loveDao.update(love2);
				}
			}else{
				fightData.result = WinLevel.FAIL;
			}
			
			bossVO.fightNum ++;
			loveMonsterDao.update(loveMonster);
			
			List<RewardObject> rewardList = new ArrayList<>();
//			LoveMonsterLeastConfig leastConfig = LoveService.getLeastConfig(id);
			LoveMonsterRewardConfig loveMonsterRewardConfig = LoveService.getLoveMonsterRewardConfig(id);
			if (loveMonsterRewardConfig != null) {
				
				BossFightVO fightVO = love1.fightStateMap.get(id);
				if(fightVO.leastNum == 0){
					fightVO.leastNum = loveMonsterRewardConfig.randomLeast();
				}
				//保底奖励
				if(fightVO.noRewardNum >= fightVO.leastNum){
					rewardList.addAll(loveMonsterRewardConfig.getLeastrewardList());
					fightVO.noRewardNum = 0;
				}else{
					//节日活动随机掉落奖励
					List<RewardObject> extReward = loveMonsterRewardConfig.getReward();
					if(extReward.isEmpty() == false){
						fightVO.noRewardNum = 0;
					}else{
						fightVO.noRewardNum ++;
					}
					rewardList.addAll(extReward);
				}
			}
			loveDao.update(love1);
			
			//每次攻击boss必出奖励
			rewardList.addAll(globalConfig.mustRewardList);
			
			if(rewardList.size() > 0){
				sendReward(love1.getPkId(), rewardList);
			}
			LoveMonsterFightResponse response = new LoveMonsterFightResponse(fightData,rewardList);
			LovePushHelper.pushBossBattle(love1.getPkId(),response);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}

	@Override
	public Result unLockBoss(long actorId, int id) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_MONSTER);
		}
		LoveMonsterGlobalConfig globalConfig = LoveService.getLoveMonsterGlobalConfig(id);
		if(globalConfig == null){
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		LoveMonster loveMonster = loveMonsterDao.get(actorId, love.getLoveActorId());
		ChainLock lock = LockUtils.getLock(love,loveMonster);
		try{
			lock.lock();
			BossFightVO bossFightVO = love.fightStateMap.get(id);
			if(bossFightVO.state != 0){
				return Result.valueOf(GameStatusCodeConstant.BOSS_UNLOCK);
			}
			int goodsNum = goodsFacade.getCount(actorId, globalConfig.costGoods);
			if(goodsNum < globalConfig.costNum){//点券解锁
				boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.LOVE_MONSTER, globalConfig.costTicket, 0, 0);
				if(isOk == false){
					return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
				}
			}else{
				goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.LOVE_MONSTER, globalConfig.costGoods, globalConfig.costNum);
			}
			bossFightVO.state = 1;
			loveMonsterDao.update(loveMonster);
			loveDao.update(love);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}

	@Override
	public Result flushFight(long actorId, int id) {
 		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_MONSTER);
		}
		LoveMonsterGlobalConfig globalConfig = LoveService.getLoveMonsterGlobalConfig(id);
		if(globalConfig == null){
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		LoveMonster loveMonster = loveMonsterDao.get(actorId, love.getLoveActorId());
		ChainLock lock = LockUtils.getLock(love,loveMonster);
		try{
			lock.lock();
			BossFightVO bossFightVO = love.fightStateMap.get(id);
			if(bossFightVO.state == 0){
				return Result.valueOf(GameStatusCodeConstant.BOSS_LOCKED_NOT_FLUSH);
			}
			if(bossFightVO.state == 2){
				return Result.valueOf(GameStatusCodeConstant.BOSS_DIE_NOT_FLUSH);
			}
			if(globalConfig.fightNum > bossFightVO.monsterFightNum){
				return Result.valueOf(GameStatusCodeConstant.NOT_FLUSH);
			}
			int costTicket = LoveService.getMonstFlushTicket(bossFightVO.monsterFlushNum + 1);
			boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.LOVE_MONSTER_FLUSH, costTicket, 0, 0);
			if(isOk == false){
				return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
			}
			bossFightVO.monsterFightNum = 0;
			bossFightVO.monsterFlushNum ++;
			love.fightTime = TimeUtils.getNow();
			loveDao.update(love);
			loveMonsterDao.update(loveMonster);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}
	
	@Override
	public Result getReward(long actorId, int id) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_MONSTER);
		}
		LoveMonsterGlobalConfig globalConfig = LoveService.getLoveMonsterGlobalConfig(id);
		if(globalConfig == null){
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		LoveMonster loveMonster = loveMonsterDao.get(actorId, love.getLoveActorId());
		ChainLock lock = LockUtils.getLock(love,loveMonster);
		try{
			lock.lock();
			BossFightVO fightVO = love.fightStateMap.get(id);
			if(fightVO.state == 2 && fightVO.rewardList.isEmpty() == false){
				sendReward(actorId,fightVO.rewardList);
				fightVO.state = 3;
				fightVO.rewardList.clear();
				loveDao.update(love);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}
	
	@Override
	public Result unMarry(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_MARRY);
		}
		LoveMonster loveMonster = loveMonsterDao.get(actorId, love.getLoveActorId());
		ChainLock lock = LockUtils.getLock(loveMonster);
		try{
			lock.lock();
			loveMonsterDao.remove(loveMonster);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}
	
	@Override
	public void onLogin(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return;
		}
		LoveMonster loveMonster = loveMonsterDao.get(love.getPkId(), love.getLoveActorId());
		ChainLock lock = LockUtils.getLock(love,loveMonster);
		try{
			lock.lock();
			
			int level = ActorHelper.getActorLevel(actorId) + ActorHelper.getActorLevel(love.getLoveActorId());
			if(loveMonster.bossMap.isEmpty()){
				loveMonster.bossMap = LoveService.initMonster(level);
				loveMonsterDao.update(loveMonster);
			}
			if(DateUtils.isToday(loveMonster.fightTime) == false){
				for(Integer id:LoveService.getAllId()){
					resetMonster(loveMonster, id, level);
				}
				loveMonsterDao.update(loveMonster);
			}
			
			if(love.fightStateMap.isEmpty()){
				love.fightStateMap = LoveService.initFight();
				loveDao.update(love);
			}
			if(DateUtils.isToday(love.fightTime) == false){
				for(Integer id:LoveService.getAllId()){
					BossFightVO fightVO = love.fightStateMap.get(id);
					fightVO.monsterFightNum = 0;
					fightVO.monsterFlushNum = 0;
					if (fightVO.state == 3) {
						fightVO.state = 1;
						fightVO.rewardList = new ArrayList<>();
					} else if (fightVO.state == 2) {
						boolean bossDie = true;
						for(MonsterVO monsterVO : getMonsterVO(id, loveMonster.bossMap.get(id), level).values()){
							if (monsterVO.getHp() != 0) {
								bossDie = false;
								break;
							}
						}
						if (bossDie == false) {
							fightVO.state = 1;
							fightVO.rewardList = new ArrayList<>();
						}
						
					}
				}
				loveDao.update(love);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}
	
	private void resetMonster(LoveMonster loveMonster, int id, int level) {
		BossVO bossVO = loveMonster.bossMap.get(id);
		LoveMonsterConfig config = LoveService.getLoveMonsterConfig(id);
		loveMonster.fightTime = TimeUtils.getNow();
		
		Map<Integer,Integer> hpMap = new ConcurrentHashMap<>();
		bossVO.maxHp = 0;
		for(Integer monsterId : config.getMonsterList().values()){
			Map<Integer, Integer> last = bossVO.monsterLastHpMax;
			Map<Integer, MonsterVO> map = loveDemonMonsterFacade.getMonster(id, level);
			for(Entry<Integer,MonsterVO> entry : map.entrySet()){ 
				if(entry.getValue().getHeroId() == monsterId){
					int hp = config.getExtraHp(last.get(monsterId), bossVO.fightNum);
					if(hp == 0){
						bossVO.maxHp += entry.getValue().getHp();
						hpMap.put(monsterId, entry.getValue().getHp());
					}else{
						bossVO.maxHp += hp;
						hpMap.put(monsterId, hp);
					}
				}
			}
		}
		bossVO.monsterLastHpMax = new ConcurrentHashMap<>();
		bossVO.monsterLastHpMax.putAll(hpMap);
		bossVO.monsterHPMap = hpMap;
		bossVO.fightNum = 0;
		bossVO.lastHurtNum = 0;
	}

	@Override
	public void onZero() {
		for(long actorId : playerSession.onlineActorList()){
			Love love = loveDao.get(actorId);
			if(love.married() == false){
				continue;
			}
			LoveMonster loveMonster = loveMonsterDao.get(love.getPkId(), love.getLoveActorId());
			int level = ActorHelper.getActorLevel(actorId) + ActorHelper.getActorLevel(love.getLoveActorId());
			ChainLock lock = LockUtils.getLock(love,loveMonster);
			try{
				lock.lock();
				for(Integer id:LoveService.getAllId()){
//					BossVO bossVO = loveMonster.bossMap.get(id);
//					LoveMonsterConfig config = LoveService.getLoveMonsterConfig(id);
//					
//					Map<Integer,Integer> hpMap = new HashMap<>();
//					bossVO.maxHp = 0;
//					for(Integer monsterId : config.getMonsterList().values()){
//						Map<Integer, Integer> last = bossVO.monsterHPMap;
//						Map<Integer, MonsterVO> map = loveDemonMonsterFacade.getMonster(id, level);
//						for(Entry<Integer,MonsterVO> entry : map.entrySet()){ 
//							int hp = config.getExtraHp(last.get(entry.getKey()), bossVO.fightNum);
//							if(entry.getValue().getHeroId() == monsterId){
//								if(hp == 0){
//									bossVO.maxHp += entry.getValue().getHp();
//									hpMap.put(monsterId, entry.getValue().getHp());
//								}else{
//									bossVO.maxHp += hp;
//									hpMap.put(monsterId, hp);
//								}
//							}
//						}
//					}
//					bossVO.monsterHPMap = hpMap;
//					bossVO.fightNum = 0;
//					bossVO.lastHurtNum = 0;
					resetMonster(loveMonster, id, level);
					loveMonsterDao.update(loveMonster);
					
					BossFightVO fightVO = love.fightStateMap.get(id);
					fightVO.monsterFightNum = 0;
					fightVO.monsterFlushNum = 0;
					fightVO.state = fightVO.state > 0 ? 1 : 0;
					fightVO.rewardList = new ArrayList<>();
					loveDao.update(love);
				}
				LovePushHelper.pushLoveMonsterInfo(actorId , getInfo(actorId).item);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}
	
	private Map<Integer, BossStateVO> parserBossVO(Love love,LoveMonster loveMonster) {
		Map<Integer,BossStateVO> bossStateMap = new HashMap<>();
		for(BossVO bossVO : loveMonster.bossMap.values()){
			LoveMonsterGlobalConfig globalConfig = LoveService.getLoveMonsterGlobalConfig(bossVO.id);
			BossFightVO fightVO = love.fightStateMap.get(bossVO.id);
			int flushCostTicket = LoveService.getMonstFlushTicket(fightVO.monsterFlushNum + 1);
			BossStateVO stateVO = new BossStateVO(bossVO,fightVO, globalConfig.costTicket,flushCostTicket, globalConfig.costNum);
			bossStateMap.put(bossVO.id, stateVO);
		}
		return bossStateMap;
	}
	
	private Map<Integer, MonsterVO> getMonsterVO(int id, BossVO bossVO,int level) {
		Map<Integer, MonsterVO> monsterMap = loveDemonMonsterFacade.getMonster(id, level);
		for(Entry<Integer,Integer> entry : bossVO.monsterHPMap.entrySet()){
			Integer index = 0;
			for(Entry<Integer,MonsterVO> monsterEntry : monsterMap.entrySet()){
				if(entry.getKey() == monsterEntry.getValue().getHeroId()){
					if(entry.getValue() > 0){
						monsterEntry.getValue().setHp(entry.getValue());
						monsterEntry.getValue().setMaxHp(entry.getValue());
					}else{
						index = monsterEntry.getKey();
					}
				}
			}
			monsterMap.remove(index);
		}
		return monsterMap;
	}
	
	private void sendReward(long actorId, List<RewardObject> rewardList) {
		for (RewardObject rewardObject : rewardList) {
			switch(rewardObject.rewardType){
			case EQUIP:
				equipFacade.addEquip(actorId, EquipAddType.LOVE_MONSTER, rewardObject.id);
				break;
			case GOODS:
				goodsFacade.addGoodsVO(actorId, GoodsAddType.LOVE_MONSTER, rewardObject.id,rewardObject.num);
				break;
			case HEROSOUL:
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.LOVE_MONSTER, rewardObject.id, rewardObject.num);
				break;
			default:
				break;
			}
		}
	}

}
