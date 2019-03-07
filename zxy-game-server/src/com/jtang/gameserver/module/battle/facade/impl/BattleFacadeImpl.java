package com.jtang.gameserver.module.battle.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.ATK_TEAM_LINEUP_NULL;
import static com.jiatang.common.GameStatusCodeConstant.BATTLE_REQUEST_SUBMIT_FAIL;
import static com.jiatang.common.GameStatusCodeConstant.BATTLE_SERVER_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.BATTLE_TEAM_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.DEF_TEAM_LINEUP_NULL;
import static com.jiatang.common.GameStatusCodeConstant.MAP_CONFIG_NOT_SUPPORT;
import static com.jiatang.common.GameStatusCodeConstant.MAP_CONFIG_NOT_EXSIT;
import static com.jiatang.common.GameStatusCodeConstant.REPLICA_DUPLICATE_ENTRANCE_ERROR;
import static com.jtang.gameserver.module.battle.constant.BattleRule.BATTLE_BETWEEN_GRID_NUM;
//import static com.jtang.sm2.module.battle.constant.BattleRule.BATTLE_EXPAND_TIME;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.Sprite;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.thread.NamedThreadFactory;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dataconfig.model.SkillConfig;
import com.jtang.gameserver.dataconfig.service.SkillService;
import com.jtang.gameserver.dbproxy.entity.Battle;
import com.jtang.gameserver.module.battle.constant.BattleRule;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.dao.BattleDao;
import com.jtang.gameserver.module.battle.facade.BattleCallBack;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.helper.BattleHelper;
import com.jtang.gameserver.module.battle.helper.Clock;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.model.AttackMonsterRequest;
import com.jtang.gameserver.module.battle.model.AttackPlayerRequest;
import com.jtang.gameserver.module.battle.model.AttackTeam;
import com.jtang.gameserver.module.battle.model.BattleGroup;
import com.jtang.gameserver.module.battle.model.BattleRequest;
import com.jtang.gameserver.module.battle.model.BattleResult;
import com.jtang.gameserver.module.battle.model.BattleScene;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.battle.type.BattleVO;
import com.jtang.gameserver.module.battle.type.Camp;
import com.jtang.gameserver.module.lineup.constant.LineupRule;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * 通用战斗接口
 * @author vinceruan
 *
 */
@Component
public class BattleFacadeImpl implements BattleFacade {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
		
	@Autowired
	private EventBus eventBus;
	
	@Autowired
	private BattleDao battleDao;
	
	/**
	 * 检验用户是否重复进入战场(一个战斗请求未处理完毕前，不接收第二个请求,除非超时)。
	 * 格式:Map<ActorId, MILLISECONDS>
	 */
	private ConcurrentMap<Long, Long> battleCheckMap = new ConcurrentHashMap<Long, Long>();
	
	/**
	 * 线程池的最少线程数
	 */
	@Autowired(required = false)
	@Qualifier("battle.pool_capacity")
	private Integer battlePoolSize = 2;
	
	/**
	 * 线程池的最大线程数
	 */
	@Autowired(required = false)
	@Qualifier("battle.pool_max_capacity")
	private Integer battlePoolMaxSize = 20;

	/**
	 * 线程的空闲存活时间
	 */
	@Autowired(required = false)
	@Qualifier("battle.pool_keep_alive_time")
	private Integer keepAliveTime = 1000;

//	/**
//	 * 批量提交任务的间隔
//	 */
//	@Autowired(required = false)
//	@Qualifier("battle.submit_job_block_time")
//	private Integer entityBlockTime = 1000;
	
//	/**
//	 * 每次提交战斗请求的最大数量
//	 */
//	@Autowired(required = false)
//	@Qualifier("battle.each.submit_num")
//	private Integer batchSubmitNum = 100;
	
//	/**
//	 * 战斗等待队列
//	 */
//	private BlockingQueue<Runnable> WAITING_QUEUE = new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE);
	
	/**
	 * 处理战斗的线程池
	 */
	private ExecutorService BATTLE_WORKER_POOL;
	
//	/**
//	 * 批量提交战斗的线程
//	 */
//	private Runnable SUBMIT_TASK_WORKER = null;
	
	
	@PostConstruct
	public void init() {		
		
//		this.SUBMIT_TASK_WORKER = new Runnable() {
//			public void run() {
//				while (true) {
//					submitBattleJob2Pool();
//				}
//			}
//		};
		
		ThreadGroup threadGroup = new ThreadGroup("战斗模块");
		NamedThreadFactory threadFactory = new NamedThreadFactory(threadGroup, "处理战斗线程");
		BATTLE_WORKER_POOL = new ThreadPoolExecutor(battlePoolSize.intValue(), battlePoolMaxSize.intValue(), this.keepAliveTime.intValue(),
				TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
		
//		String threadName = "提交战斗线程";
//		ThreadGroup group = new ThreadGroup("战斗模块");
//		NamedThreadFactory factory = new NamedThreadFactory(group, threadName);
//		Thread thread = factory.newThread(this.SUBMIT_TASK_WORKER);
//		thread.setDaemon(true);
//		thread.start();
	}
	
//	/**
//	 * 提交一批战斗请求给线程池处理
//	 */
//	private final void submitBattleJob2Pool() {
//		if (WAITING_QUEUE.isEmpty()) {
//			return;
//		}
//
//		while (!WAITING_QUEUE.isEmpty()) {
//			Runnable task;
//			try {
//				task = WAITING_QUEUE.take();
//				BATTLE_WORKER_POOL.submit(task);
//			} catch (InterruptedException e) {
//			}
//		}
//	}
	
	/**
	 * 开始战斗
	 * @param context
	 * @param atkTeam
	 * @param defTeam
	 * @return
	 */
	private TResult<BattleResult> startBattle(Context context, List<Fighter> atkTeam, List<Fighter> defTeam, int attckTeamMorale, int defTeamMorale) {
		Clock.start("战斗准备");
		int atkNum = Math.max(atkTeam.size(), defTeam.size());
		
		BattleScene scene = new BattleScene(context.getBattleMap());
		/** 进入战场,布阵 */
		BattleGroup atkGroup = scene.atkTeamEnter(atkTeam, atkNum, attckTeamMorale);
		BattleGroup defGroup = scene.defTeamEnter(defTeam, atkNum, defTeamMorale);
		scene.ready();
		
		FightData fightData = context.fightRecorder.getFightData();
		fightData.myTeam = new AttackTeam(atkTeam, BattleRule.BATTLE_MY_TEAM_FACE, attckTeamMorale);
		fightData.enemyTeam = new AttackTeam(defTeam, BattleRule.BATTLE_ENEMY_TEAM_FACE, defTeamMorale);
		context.fightRecorder.setFirstAtkTeam(scene.getReadyTeamId() == 1 ? Camp.BELOW : Camp.ABOVE);
		
		/** 战斗前的各种激活技能的buffer处理 */
		context.fightRecorder.startRecordBufferBeforeFight();
//		context.setProcessType(ProcessType.BEGIN_FIGHT);
		FightProcessor.processSkillEffects(ProcessType.BEGIN_FIGHT, context);
		context.fightRecorder.stopRecordBufferBeforeFight();

		Clock.end();
		
		Clock.start("战斗处理");
		/** 开始战斗*/
		BattleResult result = scene.attack(context);
		Clock.end();
		
		Clock.start("战后胜负判断");
		
		/** 战斗结束,判定胜负 */
		Integer atkGrpPoints = context.atkPoints.get(Camp.BELOW);
		Integer defGrpPoints = context.atkPoints.get(Camp.ABOVE);
		
		if (atkGrpPoints == null) {
			atkGrpPoints = 0;
		}
		if (defGrpPoints == null) {
			defGrpPoints = 0;
		}
		
		//更新双方的攻击伤害值
		result.fightData.atkTeamHitPoints = atkGrpPoints;
		result.fightData.defTeamHitPoints = defGrpPoints;
				
		//更新胜利类型
		WinLevel winLv = null;
		winLv = computeWinLevel(atkGroup, defGroup, atkGrpPoints, defGrpPoints);		
		if (context.overflowRound){// 如果回合数达到最大
			if (winLv.isWin()){//如果胜利 就是险胜
				winLv = WinLevel.SMALL_WIN;
			} else {//如果是失败就是惜败
				winLv = WinLevel.SMALL_FAIL;
			}
		}
		result.fightData.result = winLv;
		result.fightData.mapId = (byte) context.mapConfig.getMapId();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(result.fightData.format(""));
		}
		
		//统计攻击方的仙人存活
		for (Fighter f : atkTeam) {
			result.atkTeamStatus.put(f.getFighterId(), f.getHp());
		}
		
		//统计防御方的仙人存活
		for (Fighter f : defTeam) {
			result.defenseTeamStatus.put(f.getFighterId(), f.getHp());
		}
		result.attackTeam = atkTeam;
		result.defendsTeam = defTeam;		
		Clock.end();
		
		return TResult.sucess(result);
	}

	/**
	 * 计算胜败类型
	 * @param atkGroup
	 * @param defGroup
	 * @param atkGrpPoints
	 * @param defGrpPoints
	 * @return
	 */
	private WinLevel computeWinLevel(BattleGroup atkGroup,
			BattleGroup defGroup, Integer atkGrpPoints, Integer defGrpPoints) {
		WinLevel winLv;
		if (defGroup.isActor()) {// 人对人
			if (atkGroup.isLose() && defGroup.isLose()) {// 双方都死了
				if (atkGroup.getAtkNum() > defGroup.getAtkNum()) {
					winLv = WinLevel.SMALL_FAIL;
				} else {
					winLv = WinLevel.SMALL_WIN;
				}
			} else if (atkGroup.isLose()) {
				// 如果战败,敌方一人未挂,则为大败,否则为失败
				if (defGroup.getDeadList().isEmpty()) {
					winLv = WinLevel.BIG_FAIL;
				} else {
					winLv = WinLevel.FAIL;
				}
			} else if (defGroup.isLose()) {
				// 如果战胜，并且一人未挂，则为大胜，否则为胜利
				if (atkGroup.getDeadList().isEmpty()) {
					winLv = WinLevel.BIG_WIN;
				} else {
					winLv = WinLevel.WIN;
				}
			} else {
				// 如果未分胜负,则计算总伤害值,高则为险胜,否则就是惜败
				if (atkGrpPoints > defGrpPoints) {
					winLv = WinLevel.SMALL_WIN;
				} else if (atkGrpPoints == defGrpPoints) {
					if (atkGroup.getMorale() >= defGroup.getMorale()) {
						winLv = WinLevel.SMALL_WIN;
					} else {
						winLv = WinLevel.SMALL_FAIL;
					}
				} else {
					winLv = WinLevel.SMALL_FAIL;
				}
			}

		} else {// 人对怪
			if (atkGroup.isLose() && defGroup.isLose()) {// 双方都死了
				winLv = WinLevel.SMALL_WIN;
			} else if (atkGroup.isLose()) {
				// 如果战败,敌方一人未挂,则为大败,否则为失败
				if (defGroup.getDeadList().isEmpty()) {
					winLv = WinLevel.BIG_FAIL;
				} else {
					winLv = WinLevel.FAIL;
				}
			} else if (defGroup.isLose()) {
				// 如果战胜，并且一人未挂，则为大胜，否则为胜利
				if (atkGroup.getDeadList().isEmpty()) {
					winLv = WinLevel.BIG_WIN;
				} else {
					winLv = WinLevel.WIN;
				}
			} else {
				// 如果未分胜负,则计算总伤害值,高则为险胜,否则就是惜败
				if (atkGrpPoints >= defGrpPoints) {
					winLv = WinLevel.SMALL_WIN;
				} else {
					winLv = WinLevel.SMALL_FAIL;
				}
			}
		}
		return winLv;
	}
	
	/**
	 * 开战前准备, 分配到阵营、准备好技能
	 * @param lineupHeros 阵型每个位置上面的仙人, 格式是Map<GridIndex, HeroVO>
	 * @param context
	 * @param camp
	 * @return
	 */
	private List<Fighter> prepareHeros(long actorId, Map<Integer, HeroVO> lineupHeros, Map<Long, Map<AttackerAttributeKey, Integer>> attrChangeMap, Context context, Camp camp, boolean continueHP, byte dire, byte idStart) {
		List<Fighter> team = new ArrayList<Fighter>();
		for (Entry<Integer, HeroVO> entry : lineupHeros.entrySet()) {			
			
			HeroVO hero = entry.getValue();
			
			//根据阵营位置转换成坐标点
			Tile pos = BattleHelper.getTileInMap(entry.getKey(), camp, context.mapConfig);
			
			//属性加成
			Map<AttackerAttributeKey, Integer> attrChange = null;
			if (attrChangeMap != null) {
				attrChange = attrChangeMap.get(hero.getSpriteId());
			}
			
			Fighter fighter = new Fighter(hero, camp, pos, attrChange, actorId, continueHP, dire, idStart);
			
			idStart += 1;
			//分配到阵营
			context.addFighter2Camp(fighter);
			team.add(fighter);
			
			//找出所有已激活的技能
			processFighterAvaibleSkills(fighter, context);
		}		
		return team;
	}
	
	/**
	 * 开战前准备, 分配到阵营、准备好技能
	 * @param lineupHeros
	 * @param attrChangeMap
	 * @param context
	 * @param camp
	 * @return
	 */
	private List<Fighter> prepareMonsters(Map<Integer, MonsterVO> lineupHeros, Map<Long, Map<AttackerAttributeKey, Integer>> attrChangeMap, Context context, Camp camp, byte idStart) {
		List<Fighter> team = new ArrayList<Fighter>();
		for (Entry<Integer, MonsterVO> entry : lineupHeros.entrySet()) {
			MonsterVO monster = entry.getValue();
			
			//根据阵营位置转换成战场坐标
			Tile tile = BattleHelper.getTileInMap(entry.getKey(), camp, context.mapConfig);
			
			//属性加成
			Map<AttackerAttributeKey, Integer> attrChange = null;
			if (attrChangeMap != null) {
				attrChange = attrChangeMap.get(monster.getSpriteId());
			}
			
			Fighter fighter = new Fighter(monster, camp, tile, attrChange, (byte)0, idStart);
			idStart += 1;
			
			//分配到阵营
			context.addFighter2Camp(fighter);
			team.add(fighter);
			
			//找出所有已激活的技能
			processFighterAvaibleSkills(fighter, context);
		}
		return team;
	}
	
	/**
	 * 根据仙人配置, 找出该仙人所有的技能配置并且分类缓存
	 * @param fighter
	 * @param context
	 */
	public void processFighterAvaibleSkills(Fighter fighter, Context context) {
		//处理普攻技能
		processCommAtkSkill(fighter, context);
		
		//处理主动技能
		processAtkSkill(fighter, context);

		//处理被动技能
		processPassiveSkill(fighter, context);
	}

	/**
	 * 处理被动技能
	 * @param fighter
	 * @param context
	 */
	protected void processPassiveSkill(Fighter fighter, Context context) {
		//已经激活的被动技能
		List<Integer> skillIds = fighter.getPassiveSkillList();
		for (Integer skId : skillIds) {
			PassiveSkillConfig skillConfig = SkillService.getPassiveSkill(skId);
			if (skillConfig == null) {
				LOGGER.warn("用户:[{}],角色[{}] 使用的被动技能:[{}] 已失效,原因：对应的配置无法找到", 
						new Object[] { fighter.getFighterId(), fighter.getName(), skId});
				continue;
			}
			
			//战斗外已经做了处理
			if (skillConfig.getEffectType() == PassiveSkillConfig.EFFECT_TYPE_OUT_BATTLE) {
				continue;
			}
			
			context.addSpriteAvailableSkillEffects(fighter, skillConfig.getSkillEffects());
		}
	}

	/**
	 * 处理主动技能
	 * @param fighter
	 * @param context
	 */
	protected void processAtkSkill(Fighter fighter, Context context) {
		//主动技能
		int skillId = fighter.getSkillId();
		if(skillId == 0) {
			return;
		}

		SkillConfig skillConfig = SkillService.getSkill(skillId);
		
		if (skillConfig == null) {
			LOGGER.error("用户:[{}],角色:[{}]] 使用的主动技能:[{}] 已失效,原因：对应的配置无法找到", 
					new Object[] { fighter.getFighterId(), fighter.getName(), skillId});					
		} else {
			context.addSpriteAvailableSkillEffects(fighter, skillConfig.getSkillEffects());
		}
	}

	/**
	 * 处理普攻技能
	 * @param fighter
	 * @param context
	 */
	protected void processCommAtkSkill(Fighter fighter, Context context) {
		//普攻
		int commAtkSkillId = fighter.getCommAtkSkillId();
		SkillConfig skill = SkillService.getSkill(commAtkSkillId);
		if (skill == null) {
//			LOGGER.warn("用户:[{}],角色:[{}]] 使用的普攻配置:[{}] 已失效,原因：对应的配置无法找到", 
//					new Object[] { fighter.getFighterId(), fighter.getName(), commAtkSkillId});
		} else {
			context.addSpriteAvailableSkillEffects(fighter, skill.getSkillEffects());
		}
	}

	@Override
	public Result submitAtkMonsterRequest(AttackMonsterRequest request, BattleCallBack battleCallBack) {
		Result result = check(request.actorId, request.atkTeam, request.defTeam, request.map);
		
		if (result.isFail()) {
			return result;
		}
		
		try {
			//将战斗请求抛到等待队列
			BATTLE_WORKER_POOL.submit(createBattleRunnabel(request, battleCallBack));
		} catch (Exception e) {
			return Result.valueOf(BATTLE_REQUEST_SUBMIT_FAIL);
		}
		
		return Result.valueOf();
	}
	
	public Runnable createBattleRunnabel(final BattleRequest request, final BattleCallBack battleCallBack) {
		return new Runnable() {			
			@Override
			public void run() {
				doHandleBattle(request, battleCallBack);
			}
		};
	}
	
	@Override
	public Result submitAtkPlayerRequest(AttackPlayerRequest request, BattleCallBack battleCallBack) {
		Result result = check(request.actorId, request.atkTeam, request.defTeam, request.map);
		
		if (result.isFail()) {
			return result;
		}
		
		try {
			//将战斗请求提交到等待队列
			BATTLE_WORKER_POOL.submit(createBattleRunnabel(request, battleCallBack));
		} catch (Exception e) {
			return Result.valueOf(BATTLE_REQUEST_SUBMIT_FAIL);
		}
		return Result.valueOf();
	}
	
	/**
	 * 检查是否允许进入战场
	 * @param actorId
	 * @param atkTeam
	 * @param defTeam
	 * @return
	 */
	private Result check(long actorId, Map<Integer, HeroVO> atkTeam, Map<Integer, ? extends Sprite> defTeam, MapConfig map) {
		long newTime = System.currentTimeMillis();
//		Long oldTime = battleCheckMap.get(actorId);

		//重复进入战场
		if (battleCheckMap.containsKey(actorId)) {
			return Result.valueOf(REPLICA_DUPLICATE_ENTRANCE_ERROR);			
		}

		//双方阵型是否有上阵人员
		if (atkTeam == null || atkTeam.isEmpty()) {
			return Result.valueOf(ATK_TEAM_LINEUP_NULL);
		}

		if (defTeam == null || defTeam.isEmpty()) {
			return Result.valueOf(DEF_TEAM_LINEUP_NULL);
		}
		
		if (map == null) {
			return Result.valueOf(MAP_CONFIG_NOT_EXSIT);
		}
		
		int rowCount = map.getGridRow();
		int colCount = map.getGridCol();		
		if (rowCount != LineupRule.ROW_COUNT*2 + BATTLE_BETWEEN_GRID_NUM) {
			return Result.valueOf(MAP_CONFIG_NOT_SUPPORT);			
		}
		//既不是1vs1地图,也不是2vs2地图
		if (colCount != LineupRule.COL_COUNT && colCount != LineupRule.COL_COUNT*2) {
			return Result.valueOf(MAP_CONFIG_NOT_SUPPORT);
		}
		
		//判断地图和阵形是否匹配
		for (Integer grideIndex : atkTeam.keySet()) {
			if (grideIndex > rowCount * colCount){
				return Result.valueOf(MAP_CONFIG_NOT_SUPPORT);
			}
		}
		for (Integer grideIndex : defTeam.keySet()) {
			if (grideIndex > rowCount * colCount){
				return Result.valueOf(MAP_CONFIG_NOT_SUPPORT);
			}
		}
		
		// 检查队伍双方是否有同一个角色
		List<Long> heroIds = new ArrayList<>();
		for (HeroVO hero : atkTeam.values()) {
			heroIds.add(hero.getSpriteId());
		}
		
		for (Sprite sprite : defTeam.values()) {
			if (heroIds.contains(sprite.getSpriteId())){
				return Result.valueOf(BATTLE_TEAM_ERROR);
			}
		}
		
		
		battleCheckMap.put(actorId, newTime);

		return Result.valueOf();
	}
	
	/**
	 * 发布战斗结果给监听者
	 * @param eventType
	 * @param data
	 */
	private void publishBattleResult(BattleRequest req, BattleResult result, BattleCallBack battleCallBack) {
		result.battleReq = req;
//		BattleEvent event = new BattleEvent(req.eventKey, result);
//		eventBus.post(event);
		try {
			battleCallBack.execute(result);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		}
		battleCheckMap.remove(req.actorId);
	}

	@Override
	public void register(String eventKey, Receiver receiver) {
		this.eventBus.register(eventKey, receiver);
	}

	public void doHandleBattle(BattleRequest req, BattleCallBack battleCallBack) {		
		try {
			TResult<BattleResult> result = null;
			Context context = new Context(req.map);
			Map<Integer, HeroVO> heros = req.atkTeam;
			Map<Long, Map<AttackerAttributeKey, Integer>> atkAttrChange = req.getAtkTeamAttrChange();
			long actorId = req.actorId;
			if (req instanceof AttackMonsterRequest) {
				//攻打怪物事件
				AttackMonsterRequest event = (AttackMonsterRequest)req;
				context.setSkipFirstRound(event.skipFirstRound);
				Map<Integer, MonsterVO> monsters = event.defTeam;
				Map<Long, Map<AttackerAttributeKey, Integer>> defAttrChange = event.getDefTeamAttrChange();								
				List<Fighter> atkTeam = prepareHeros(actorId, heros, atkAttrChange, context, Camp.BELOW, false, (byte)1, (byte)1);
				List<Fighter> defTeam = prepareMonsters(monsters, defAttrChange, context, Camp.ABOVE, (byte)20);	
				result = startBattle(context, atkTeam, defTeam, req.morale, event.monsterMorale);
				
			} else if (req instanceof AttackPlayerRequest) {
				//攻打玩家事件
				AttackPlayerRequest event = (AttackPlayerRequest)req;							
				Map<Integer, HeroVO> enemies = event.defTeam;
				long targetActor = event.targetActorId;
				Map<Long, Map<AttackerAttributeKey, Integer>> defAttrChange = event.getDefTeamAttrChange();
				
				List<Fighter> atkTeam = prepareHeros(actorId, heros, atkAttrChange, context, Camp.BELOW, event.continueHP, (byte)1, (byte)1);
				List<Fighter> defTeam = prepareHeros(targetActor, enemies, defAttrChange, context, Camp.ABOVE, event.continueHP, (byte)0,(byte)20);
				result = startBattle(context, atkTeam, defTeam, req.morale, event.targetMorale);
			
			} else {
				LOGGER.error("无法处理的事件类型:" + req.eventKey);
			}

			//记录战斗胜利到数据库
			Battle battle = battleDao.get(actorId);
			battle.updateBattleData(req.battleType, result.item.fightData.result);
			battleDao.update(actorId);
			result.item.battleVO = battle.getBattleVO(req.battleType);
			if (result != null && result.isOk()) {
				//战斗处理成功
				publishBattleResult(req, result.item, battleCallBack);
			} else {
				//战斗处理失败
				BattleResult br = new BattleResult();
				br.statusCode = result != null? result.statusCode : BATTLE_SERVER_ERROR;
				publishBattleResult(req, result.item, battleCallBack);
			}
			
		} catch(Exception ex) {
			LOGGER.error(String.format("actorId:%s  处理事件%s时出错", req.actorId, req.eventKey), ex);
			battleCheckMap.remove(req.actorId);
		} 
	}

	@Override
	public int getBatteWinNum(long actorId, BattleType battleType) {
		Battle battle = battleDao.get(actorId);
		BattleVO battleVO = battle.getBattleVO(battleType);
		if (battleVO == null){
			return 0;
		}
		return battleVO.winNum;
	}

	@Override
	public int getBatteFailNum(long actorId, BattleType battleType) {
		Battle battle = battleDao.get(actorId);
		BattleVO battleVO = battle.getBattleVO(battleType);
		if (battleVO == null){
			return 0;
		}
		return battleVO.failNum;
	}

	@Override
	public int getBatteTotalNum(long actorId, BattleType battleType) {
		Battle battle = battleDao.get(actorId);
		BattleVO battleVO = battle.getBattleVO(battleType);
		if (battleVO == null){
			return 0;
		}
		return battleVO.winNum + battleVO.failNum;
	}
	
	
	
}
