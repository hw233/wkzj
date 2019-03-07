package com.jtang.gameserver.module.battle.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.module.battle.helper.BattleHelper;
import com.jtang.gameserver.module.battle.helper.FightProcessor;
import com.jtang.gameserver.module.battle.type.Camp;
import com.jtang.gameserver.module.battle.type.MoveDirection;
import com.jtang.gameserver.module.battle.type.TileType;
import com.jtang.gameserver.module.buffer.helper.BufferHelper;
import com.jtang.gameserver.module.buffer.type.CycleType;
import com.jtang.gameserver.module.skill.type.ProcessType;


/**
 * 战斗场景
 * 用于执行一场完整的战斗.
 * @author 0x737263
 *
 */
public class BattleScene {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * 战斗地图
	 */
	private BattleMap battleMap;
	
	/**
	 * 战斗组(仅2方)
	 */
	private Map<Integer,BattleGroup> battleGroups = new HashMap<Integer,BattleGroup>(2);
	
	/**
	 * 当前出手方
	 */
	private Integer readyTeamId = 1;
	
	public BattleScene(BattleMap battleMap) {
		this.battleMap = battleMap;
	}
	
	/**
	 * 获取当前攻击组(出手方)
	 * @return
	 */
	private BattleGroup getAttackGroup() {
		return battleGroups.get(readyTeamId);
	}
	
	/**
	 * 获取当前防御组
	 * @return
	 */
	private BattleGroup getDefenceGroup() {
		BattleGroup defenceGroup;
		if(readyTeamId == 2) {
			defenceGroup = battleGroups.get(1);
		} else {
			defenceGroup = battleGroups.get(2);
		}
		return defenceGroup;
	}
	
	/**
	 * 进入场景
	 * @param team  仙人战斗组(已按战斗顺序排列)
	 */
	public BattleGroup atkTeamEnter(List<Fighter> team, int atkNum, int morale) {	
		return enter(team, atkNum, Camp.BELOW, morale);
	}
	
	/**
	 * 进入场景
	 * @param team  仙人战斗组(已按战斗顺序排列)
	 */
	public BattleGroup defTeamEnter(List<Fighter> team, int atkNum, int morale) {	
		return enter(team, atkNum, Camp.ABOVE, morale);
	}

	/**
	 * 进入场景
	 * @param teamId
	 * @param team
	 * @param atkNum
	 * @return
	 */
	private BattleGroup enter(List<Fighter> team, int atkNum, Camp camp, int morale) {
		List<Fighter> tempList = new ArrayList<Fighter>();
		for(Fighter h : team) {
			tempList.add(h);
		}
		BattleGroup group = new BattleGroup(atkNum,tempList, camp, morale);
		battleGroups.put(camp == Camp.BELOW? 1 : 2, group);
		return group;
	}
	
	/**
	 * 准备工作
	 */
	public void ready() {
		// 计算先出手的组
		if (getAttackGroup().getMorale() > getDefenceGroup().getMorale()) {
			this.readyTeamId = 1;
		} else if (getAttackGroup().getMorale() < getDefenceGroup().getMorale()) {
			this.readyTeamId = 2;
		} else {
			this.readyTeamId = RandomUtils.nextInt(1, 2);
		}
		
		// 两组成员填充地图占位
		Collection<Fighter> spriteList;
		Tile tile;
		for (int i = 1; i <= 2; i++) {
			spriteList = battleGroups.get(i).getList();
			for (Fighter s : spriteList) {
				tile = s.getTile();
				battleMap.setTile(tile, TileType.BARRIER_POINT);
			}
		}
	}
	
	/**
	 * 开始战斗
	 * 
	 * <pre>
	 * 计算一个回合的总出手次数
	 * 规则：人多的一方总数  * 2 = 总出手次数
	 * 回合： 指甲乙两队的"所有生物"都操作(攻击、寻路、原地不动)过一次。
	 * 出手： 指当前生物开始动作。
	 * 双方出手：指双方生物都动作过。(有时候因为双方人数不等，另一方会跳过出手机会)
	 * 
	 * 气势最高的先开始
	 * 
	 * 双方遵循一次出手逻辑:
	 * 搜寻攻击范围内是否有敌方生物
	 * 有： 攻击 
	 *    +普攻
	 *    +技能(群攻技能有可能打多 个)
	 *      +受击者触发技能
	 * 无: 寻路(靠近目标)
	 *    +移动一格
	 *    +寻不了路(无动作,动画表现为原地跳动一下)
	 * 切换为对方出手
	 * </pre>
	 */
	public BattleResult attack(Context context) {
		BattleGroup atkGroup = null;
		BattleGroup defGroup = null;
		Fighter atker;
		
		//当前累计的出手次数
		int totalAtkNum = 0;

		int maxRound = context.mapConfig.getMaxRound();
		boolean overflowRound = true;
		while(context.attackRound <= maxRound) {
			//每回合开始前处理地图
			processMap(context);
			atkGroup = getAttackGroup();
			defGroup = getDefenceGroup();
			
			if (atkGroup.isLose() || defGroup.isLose()) {
				overflowRound = false;
				break;
			}
			
			//当前组派出攻击手
			atker = atkGroup.readyAtker();
			
			//一个回合的总出手次数
			int roundAtkNum = atkGroup.getAtkNum() + defGroup.getAtkNum();
			totalAtkNum ++;
			
			//处理回合开始的技能的释放、buffer跳动
			processRoundStart(context, totalAtkNum, roundAtkNum, maxRound);
			if (context.attackRound > maxRound) {
				break; //到达最大轮次,直接退出
			}
			
			//如果对应位置上没有仙人或者仙人已死, 则跳过出手机会或输掉战斗.
			if(atker == null || atker.isDead()) {
				if (atkGroup.isLose()) { //已死光
					overflowRound = false;
					break;
				}
				
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("跳过当次出手机会,原因:" + (atker == null? "对应位置为空，没有仙人占位" : atker.getName() + "已经死亡"));
				}	
			} else {
				if (atker.actorId < 0 && context.isSkipFirstRound() && context.attackRound == 1) {
					continue;
				}
				//计算一次出手的结果
				context.fightRecorder.startRecordOneAtk();
				OnceAttack(atker, context);
				if (context.attackRound == maxRound) {
					processMaxRound(context);
				}
				context.fightRecorder.stopRecordOneAtk();
			}

			//处理回合结束的技能的释放、buffer跳动
			processRoundEnd(context, totalAtkNum, roundAtkNum);
			
			
			//下一个组准备
			readyTeamId = readyTeamId == 1 ? 2 : 1;
		}

		context.overflowRound = overflowRound;
		//战斗后的buffer跳动
		processFightEndEvent(context);

		//伤亡统计
		printFightResult(atkGroup, defGroup);

		return context.fightRecorder.getFightResult();
	}
	
	/**
	 * 处理地图
	 * @param context
	 */
	public void processMap(Context context) {
		List<Fighter> fighters = context.getAllFighter();
//		Map<Tile, Boolean> flags = new HashMap<>();
//		for (Fighter fighter : fighters) {
//			if (fighter.isDead()) {
//				flags.put(fighter.getTile(), false);
//			}
//		}
//		
//		for (Fighter fighter : fighters) {
//			if (!fighter.isDead()) {
//				if (flags.containsKey(fighter.getTile())) {
//					flags.put(fighter.getTile(), true);
//				}
//			}
//		}
//		
//		for (Map.Entry<Tile, Boolean> flag : flags.entrySet()) {
//			if (flag.getValue() == false) {
//				context.battleMap.clearBarrier(flag.getKey());
//			}
//		}
		context.battleMap.reset();
		for (Fighter fighter : fighters) {
			if (!fighter.isDead()) {
				context.battleMap.setTile(fighter.getTile(), TileType.BARRIER_POINT);
			}
		}
	}

	/**
	 * @param context
	 */
	private void processFightEndEvent(Context context) {
		context.fightRecorder.startRecordBufferAfterFight();
		context.setProcessType(ProcessType.END_FIGHT);
		FightProcessor.processSkillEffects(ProcessType.END_FIGHT, context);
		BufferHelper.triggerBuffers(context, CycleType.END_FIGHT);
		context.fightRecorder.stopRecordBufferAfterFight();
	}
	
	private void processMaxRound(Context context) {
		FightProcessor.processSkillEffects(ProcessType.MAX_ROUND, context);
	}

	/**
	 * 伤亡统计,调试用
	 * @param attackGroup
	 * @param defenceGroup
	 */
	private void printFightResult(BattleGroup attackGroup, BattleGroup defenceGroup) {
		if (LOGGER.isDebugEnabled()) {
			for (Fighter fighter : attackGroup.getList()) {
				LOGGER.debug("[{}]-[{}]:[{}]]", fighter.getCamp(), fighter.getName(), fighter.getHp());
			}
			
			for (Fighter fighter : attackGroup.getDeadList()) {
				LOGGER.debug("[{}]-[{}]:[{}]]", fighter.getCamp(), fighter.getName(), fighter.getHp());
			}
			
			for (Fighter fighter : defenceGroup.getList()) {
				LOGGER.debug("[{}]-[{}]:[{}]]", fighter.getCamp(), fighter.getName(), fighter.getHp());
			}
			
			for (Fighter fighter : defenceGroup.getDeadList()) {
				LOGGER.debug("[{}]-[{}]:[{}]]", fighter.getCamp(), fighter.getName(), fighter.getHp());
			}
		}
	}

	/**
	 * 处理一轮的开始
	 * @param context
	 * @param totalAtkNum
	 * @param roundAtkNum
	 */
	private boolean processRoundStart(Context context, int totalAtkNum, int roundAtkNum, int maxRound) {
		if (totalAtkNum % roundAtkNum != 1) {//还没到一轮的开始点
			return false;
		}
				
		context.attackRound++;
		if (context.attackRound > maxRound) {
			return true;
		}
			
		//一轮的开始
		context.fightRecorder.startRecordRound();
		context.fightRecorder.startRecordBufferBeforeRound();
		processRoundStartEvent(context);
		context.fightRecorder.stopRecordBufferBeforeRound();		
		return true;
	}

	/**
	 * 处理一轮的结束
	 * @param context
	 * @param totalAtkNum
	 * @param roundAtkNum
	 */
	private boolean processRoundEnd(Context context, int totalAtkNum, int roundAtkNum) {
		if (totalAtkNum % roundAtkNum != 0) {//还没到一轮的结束点
			return false;
		}
		context.fightRecorder.startRecordBufferAfterRound();
		processRoundEndEvent(context);
		context.fightRecorder.stopRecordBufferAfterRound();
		return true;
	}
	
	/**
	 * 一轮开始时事件处理
	 * @param context
	 */
	private void processRoundStartEvent(Context context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("开始新一轮战斗:" + context.attackRound);
		}
		context.setProcessType(ProcessType.START_ROUND);
		BufferHelper.triggerBuffers(context, CycleType.BEGIN_ROUND);
		FightProcessor.processSkillEffects(ProcessType.START_ROUND, context);
	}

	/**
	 * 一轮结束时事件处理
	 * @param context
	 */
	private void processRoundEndEvent(Context context) {		
		//处理技能和buffer
		context.setProcessType(ProcessType.END_ROUND);		
		BufferHelper.triggerBuffers(context, CycleType.END_ROUND);
		FightProcessor.processSkillEffects(ProcessType.END_ROUND, context);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("结束一轮战斗:" + context.attackRound);
		}
	}
	
	/**
	 * 寻找敌人，如果有标记的，返回标记敌人
	 * @param fighter
	 * @param enermies
	 * @return
	 */
	private Fighter getEnemy(Fighter fighter, List<Fighter> enermies) {
		Fighter target = null;
		Fighter markFighter = searchMarkEnemy(enermies);
		if (markFighter != null && isInScope(fighter, markFighter)) {
			target = markFighter;
		} else {
			target = searchEnemy(fighter, enermies);
		}
		
		return target;
	}
	/**
	 * 尝试一次攻击
	 * @param fighter
	 * @param context
	 * @return
	 */
	public boolean tryAttack(Fighter fighter, Context context) {
		//先尝试进行技能攻击,如果技能释放失败或者没有普攻阶段的技能，才进行普通攻击
		boolean result = FightProcessor.processSkillEffects(fighter, context, ProcessType.BEGIN_STAGE_1);
		if (result) {
			//技能释放成功后触发buffer
			BufferHelper.triggerBuffers(fighter, CycleType.SKILL, context);
			//处理攻击完成时触发的技能
			FightProcessor.processSkillEffects(fighter, context, ProcessType.END_ATK);
			return true;
		}
		
		//以下是普通攻击
		if (context.getTargetEnermy() != null) {
			commonAttack1(fighter, context);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 尝试移动
	 * @param fighter
	 * @param context
	 * @param target
	 */
	private void tryMove(Fighter fighter, Context context, Fighter target) {
		//以下是寻路
		FightProcessor.processSkillEffects(fighter, context, ProcessType.BEFORE_MOVE);
		boolean flag = false;
		for (int i = 0; i < fighter.moveNum; i++) {
			boolean temp = move2Area(fighter, context, target.getTile());
			flag = flag || temp;
		}
		if (flag) {
			//移动后触发buffer
			BufferHelper.triggerBuffers(fighter, CycleType.MOVE, context);
		}
	}
	
	/**
	 * 处理普攻环节:普通攻击或技能攻击
	 * @param fighter
	 * @param enermies
	 * @param context
	 */
	private void skillOrCommonAttack(Fighter fighter, List<Fighter> enermies, Context context) {
		
		Fighter target = getEnemy(fighter, enermies);
		if (target == null) {//敌人已经全部消灭
			return;
		}
		
		if (isInScope(fighter, target)) { //在射程范围内
			context.setTargetEnermy(target);
		}
		
		if (BufferHelper.isInFighting(fighter.getBuffers()) || BufferHelper.isInFightingHertChange(fighter.getBuffers())){ //内讧只触发普攻
			commonAttack(fighter, context);
			return;
		}
		
		boolean result = tryAttack(fighter, context);
		if (result) {
			return;
		}
		
		//射程不够而无法攻击时,触发buffer
		BufferHelper.triggerBuffers(fighter, CycleType.ATTACK_UNREACHABLE, context);
		
		tryMove(fighter, context, target);
		
		//再寻找目标
		target = searchEnemy(fighter, enermies);
		if (target == null) {//敌人已经全部消灭
			return;
		}
		
		if (isInScope(fighter, target)) {//在射程范围内
			context.setTargetEnermy(target);
		} else {
			return;
		}
		
		tryAttack(fighter, context);
	}
	
	/**
	 * 寻路到可攻击区域
	 * @param fighter
	 * @param context
	 * @param target
	 */
	private boolean move2Area(Fighter fighter, Context context, Tile target) {
		MapConfig conf = context.mapConfig;
		int scop = fighter.getAtkScope();
		int x = target.getX();
		int y = target.getY();
		//找出可攻击区域的上下左右4个点
		Tile topLeft = new Tile(x-scop, y+scop);
		Tile topRight = new Tile(x+scop, y+scop);
		Tile buttomLeft = new Tile(x-scop, y-scop);
		Tile buttomRight = new Tile(x+scop, y-scop);
		adjustTile(topLeft, conf);
		adjustTile(topRight, conf);
		adjustTile(buttomLeft, conf);
		adjustTile(buttomRight, conf);
		Tile from = fighter.getTile();
		
		//已经在可攻击区域
		if (from.getX() >= topLeft.getX() && from.getX() <= topRight.getX()
			&& from.getY() >= buttomLeft.getY() && from.getY() <= topLeft.getY()) {
			return false;
		}
		
		//Y轴方向走动即可到达
		if (from.getX() >= topLeft.getX() && from.getX() <= topRight.getX()) {
			boolean flag = false;
			if (from.getY() > topLeft.getY()) {
				flag = move2Point(fighter, context, new Tile(from.getX(), topLeft.getY()));
			} else {
				flag = move2Point(fighter, context, new Tile(from.getX(), buttomLeft.getY()));
			}
			return flag;
		}
		
		//x轴方向走动即可到达
		if(from.getY() >= buttomLeft.getY() && from.getY() <= topLeft.getY()) {
			boolean flag = false;
			if (from.getX() < topLeft.getX()) {
				flag = move2Point(fighter, context, new Tile(topLeft.getX(), from.getY()));
			} else {
				flag = move2Point(fighter, context, new Tile(topRight.getX(), from.getY()));
			}
			return flag;
		}

		//找出可攻击区域的4个角点哪个最近
		int distant1 = Math.abs(from.getX() - topLeft.getX()) + Math.abs(from.getY() - topLeft.getY());
		int distant2 = Math.abs(from.getX() - topRight.getX()) + Math.abs(from.getY() - topRight.getY());
		int distant3 = Math.abs(from.getX() - buttomLeft.getX()) + Math.abs(from.getY() - buttomLeft.getY());
		int distant4 = Math.abs(from.getX() - buttomRight.getX()) + Math.abs(from.getY() - buttomRight.getY());
		Tile nearestPoint = topLeft;
		int distant = distant1;
		if (distant2 < distant) {
			distant = distant2;
			nearestPoint = topRight;
		}
		if (distant3 < distant) {
			distant = distant3;
			nearestPoint = buttomLeft;
		}
		if (distant4 < distant) {
			distant = distant4;
			nearestPoint = buttomRight;
		}
		
		//移动到最近的点即可
		return move2Point(fighter, context, nearestPoint);
	}
	
	/**
	 * 确保tile不超出地图区域
	 * @param tile
	 * @param conf
	 */
	private void adjustTile(Tile tile, MapConfig conf) {
		if (tile.getX() < 0) {
			tile.setX(0);
		}
		if (tile.getX() >= conf.getGridCol()) {
			tile.setX(conf.getGridCol() - 1);
		}
		if (tile.getY() < 0) {
			tile.setY(0);
		}
		if (tile.getY() >= conf.getGridRow()) {
			tile.setY(conf.getGridRow() - 1);
		}
	}

	/**
	 * 寻路到某个点
	 * @param fighter
	 * @param context
	 * @param toTile
	 */
	private boolean move2Point(Fighter fighter, Context context, Tile toTile) {
		Tile fromTile = fighter.getTile();
		Camp camp = fighter.getCamp();
		List<MoveDirection> directions = new ArrayList<>();
		//选择最优路径(只走一步)
		if (fromTile.getY() == toTile.getY()) {
			if (camp.isRight(toTile, fromTile)) {
				directions.add(MoveDirection.RIGHT);//尝试向右,除非有阻碍
				directions.add(MoveDirection.FORWARD);
				directions.add(MoveDirection.BACKWARD);
			} else {
				directions.add(MoveDirection.LEFT);//尝试向左,除非有阻碍
				directions.add(MoveDirection.FORWARD);
				directions.add(MoveDirection.BACKWARD);
			}
		} else {
			if (camp.isBehind(toTile, fromTile)) {
				directions.add(MoveDirection.BACKWARD);//尝试向后,除非有阻碍
				directions.add(MoveDirection.LEFT);
				directions.add(MoveDirection.RIGHT);
				directions.add(MoveDirection.FORWARD);
			} else {
				directions.add(MoveDirection.FORWARD);//尝试向前,除非有阻碍
				MoveDirection backupNext = computeMoveDirection(camp, fromTile, context.battleMap);
				directions.add(backupNext);
				directions.add(backupNext.getOppositeDirection());
			}
		}
		
		return move(fighter, context, directions);
	}
	
	public MoveDirection computeMoveDirection(Camp camp, Tile tile, BattleMap map) {
		Tile left = camp.getLefPosition(tile);
		Tile right = camp.getRightPosition(tile);
		boolean canMoveLeft = map.isWalk(left);
		boolean canMoveRight = map.isWalk(right);
		if (canMoveLeft&&canMoveRight == true) {
			left = camp.getAheadPosition(left);
			right = camp.getAheadPosition(right);
			if (map.isWalk(right) == false) {
				return MoveDirection.LEFT;
			} else {
				return MoveDirection.RIGHT;
			}
		} else if (canMoveLeft) {
			return MoveDirection.LEFT;
		} else {
			return MoveDirection.RIGHT;
		}
	}
	
	

	/**
	 * 走动
	 * @param fighter
	 * @param context
	 * @param fromTile
	 * @param camp
	 * @param directions
	 */
	protected boolean move(Fighter fighter, Context context, List<MoveDirection> directions) {
		Camp camp = fighter.getCamp();
		Tile fromTile = fighter.getTile();
		for (MoveDirection direct : directions) {			
			Tile moveTile = camp.move(fromTile, direct);
			if (battleMap.move(fighter.getTile(), moveTile)) {
				fighter.setTile(moveTile);
				break;
			}
		}
		
		if (fighter.getTile().equals(fromTile)) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("前方有障碍物,无法走动,当前坐标:(%s)", fromTile.getX()+","+fromTile.getY());
			}
			return false;
		} else {
			context.fightRecorder.addAction(MoveAction.valueOf(fighter.getFighterId(), fighter.getTile()));
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("寻路到......" + fighter.getTile());
			}
			return true;
		}
		
	}

	/**
	 * 进行普通攻击及一系列普通攻击后的处理
	 * @param fighter
	 * @param context
	 */
	private void commonAttack1(Fighter fighter, Context context) {
		commonAttack(fighter, context);
		
		//处理普通攻击完成时触发的技能
		FightProcessor.processSkillEffects(fighter, context, ProcessType.END_COMMON_ATK);
		//处理攻击完成时触发的技能
		FightProcessor.processSkillEffects(fighter, context, ProcessType.END_ATK);
		//处理被普通攻击时触发的技能		
		FightProcessor.processSkillEffects(fighter, context, ProcessType.AFTER_BE_COMM_ATKED);
		//普攻后触发buffer
		BufferHelper.triggerBuffers(fighter, CycleType.SKILL, context);
	}
	
	/**
	 * 普通攻击
	 * @param fighter
	 * @param context
	 */
	private void commonAttack(Fighter fighter, Context context) {
		FightProcessor.processSkillEffects(fighter, context, ProcessType.ADD_COMMON_ATK_TIMES);
		int commonAtkTimes = 1 + fighter.getExtraCommAtkTimes();
		for (int i = 1; i <= commonAtkTimes; i++) {
			FightProcessor.processSkillEffects(fighter, context, ProcessType.COMMON_ATK);
		}
	}
	
	private Fighter searchMarkEnemy(List<Fighter> enermies) {
		for (Fighter fighter : enermies) {
			if (fighter.isDead()) {
				continue;
			}
			if (fighter.isMark()) {
				return fighter;
			}
		}
		
		return null;
	}
	
	/**
	 * 搜索最佳攻击目标：距离最短且靠右边的目标
	 * @param figther
	 * @param context
	 * @return
	 */
	private Fighter searchEnemy(Fighter figther, List<Fighter> enemies) {
		
		//按距离排序
		Map<Integer, List<Fighter>> sortEnemies = new TreeMap<Integer, List<Fighter>>();
		Tile start = figther.getTile();
		//将敌人按距离长远分类
		for (Fighter enemy : enemies) {
			if (enemy.isDead()) {
				continue;//跳过死亡者
			}
			Tile end = enemy.getTile();
			int distantx = Math.abs(end.getX() - start.getX());
			int distanty = Math.abs(end.getY() - start.getY());
			
			int distant = Math.max(distantx, distanty);
			
			if (sortEnemies.get(distant) == null) {
				sortEnemies.put(distant, new ArrayList<Fighter>());
			}
			sortEnemies.get(distant).add(enemy);
		}
		
		//找出最佳目标
		Fighter target = null;
		if (sortEnemies.size() > 0) {
			List<Fighter> enemyList = sortEnemies.values().iterator().next();
			//同等距离情况下,寻找最右边的
			for (Fighter enemy : enemyList) {
				if (target == null) {
					target = enemy;
				} else if (figther.getCamp().isRight(enemy.getTile(), target.getTile())) {
					//如果新搜到的敌人是在右边,则更新目标
					target = enemy;
				}
			}
		}
		
		return target;
	}
	
	/**
	 * 一次出手攻击处理
	 * @param atkSprite
	 * @param Context 战斗上下文
	 * @return
	 */
	private void OnceAttack(Fighter attacker, Context context) {
		if (attacker.isDead()) {
			return;
		}
		
		//处理出手前跳动的buffer
		BufferHelper.triggerBuffers(attacker, CycleType.BEFORE_ATK, context);
		
		//有可能上面的buffer跳动也会造成仙人死亡
		if (attacker.isDead()) {
			return;
		}
		
		/** 判断是否已定身,直接跳过 */
		if (BufferHelper.isUnitInImmobilize(attacker.getBuffers()) > 0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("放弃当次出手机会,原因:" + attacker.getName() + "已处于定身状态");
			}
			return;
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(attacker.getName() + "开始出击");
		}
		
		context.getFightersBeAtked().clear();
		context.getFightersHpAdded().clear();
		context.setTargetEnermy(null);
		context.setCommonAtkHurt(0);
		
		/** 第一步: 普攻环节 */
		List<Fighter> enermies = BattleHelper.getEnemies(attacker, context);		
		skillOrCommonAttack(attacker, enermies, context);
		
		
		
		/** 第二步: 攻击后的技能处理环节*/
		FightProcessor.processSkillEffects(attacker, context, ProcessType.BEGIN_STAGE_2);
		Fighter tarFighter = context.getTargetEnermy();
		context.setTargetEnermy(attacker); //以下是受害者的反攻
				
		/** 第三步: 被攻击者触发的技能处理 */
		Set<Fighter> affectedFighters = new HashSet<>(context.getFightersBeAtked());
		boolean flag = false;
		for (Fighter enermy : affectedFighters) {
			flag = flag || FightProcessor.processSkillEffects(enermy, context, ProcessType.AFTER_BE_ATKED);
			flag = flag || FightProcessor.processSkillEffects(enermy, context, ProcessType.AFTER_BE_SKILL_ATKED);
			
			//受伤害给自己增益效果
			flag = flag || FightProcessor.processSkillEffects(attacker, context, ProcessType.HERT);
			flag = flag ||FightProcessor.processSkillEffects(enermy, context, ProcessType.HERT);
			
		}
		if (flag) { //如果反击类技能触发成功 把目标设设置回原来的目标
			context.setTargetEnermy(tarFighter);
		}
		//处理治疗目标触发治疗目标技能
		Set<Fighter> addHpFighters = new HashSet<>(context.getFightersHpAdded());
		for (Fighter fighter : addHpFighters) {
			FightProcessor.processSkillEffects(fighter, context, ProcessType.ADD_HP);
		}
		
		/** 第四步:死亡者引起的技能处理  例如复活等*/
		processSkillWhenDie(attacker, context);
		
		//处理出手后跳动的buffer
		BufferHelper.triggerBuffers(attacker, CycleType.AFTER_ATK, context);
		
	}
	
	private void processDead(Context context, Set<Fighter> fightersAffected) {
		for (Fighter hero : fightersAffected) {
			if(hero.isDead()) {
				boolean flag = FightProcessor.processSkillEffects(hero, context, ProcessType.DIE);
				if (flag) {
					for (List<Action> skillAction : context.fightRecorder.skillActions) {
						for (Action action : skillAction) {
							
							if (action instanceof SequenceAction) {
								SequenceAction sqAction = (SequenceAction) action;
								Iterator<Action> it = sqAction.actions.iterator();
								while (it.hasNext()) {
									Action next = it.next();
									if (next instanceof DeadAction) {
										DeadAction deadAction = (DeadAction) next;
										if (deadAction.uid == hero.getFighterId()) {
											it.remove();
											context.fightRecorder.addAction(deadAction);
											DisapperAction disapperAction = deadAction.getDisapperAction();
											sqAction.actions.remove(disapperAction);
											context.fightRecorder.addAction(disapperAction);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 判断是否有死亡者，如果有则判断其是否有对应的死亡时触发的技能,有则触发
	 * @param context
	 * @param fightersAffected
	 */
	private void processSkillWhenDie(Fighter attacker, Context context) {
		
		Set<Fighter> fightersAffected = new HashSet<>(context.getFightersBeAtked());
		processDead(context, fightersAffected);
		for (Fighter hero : fightersAffected) {
			if (hero.isDead()) {
				FightProcessor.processSkillEffects(hero, context, ProcessType.AFTER_DEAD);
				
				if (hero.isDead()) {
					//被击者死亡时攻击者的技能处理
					context.setTargetEnermy(hero);
					FightProcessor.processSkillEffects(attacker, context, ProcessType.KILL_ENEMY);
					BufferHelper.triggerBuffers(attacker, CycleType.KILL_ENEMY, context);
				}
					
			}
		}
		Set<Fighter> fightersAffected1 = new HashSet<>(context.getFightersBeAtked());
		Set<Fighter> temp = new HashSet<>();
		for (Fighter fighter : fightersAffected1) {
			if (fightersAffected.contains(fighter) == false){
				temp.add(fighter);
			}
		}
		
		processDead(context, temp);
		for (Fighter hero : temp) {
			if (hero.isDead()) {
				FightProcessor.processSkillEffects(hero, context, ProcessType.AFTER_DEAD);
			}
		}
		fightersAffected = new HashSet<>(context.getFightersBeAtked());
		for (Fighter hero : fightersAffected) {
			if (hero.isDead()) {
				BufferHelper.triggerBuffers(hero, CycleType.DIE, context);
				
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("仙人:[{}]-[{}]在本回合过程中光荣牺牲,hp[{}]", hero.getFighterId(), hero.getName(), hero.getHp());
				}
			}
		}
		
	
	}

	
	/**
	 * 是否在攻击范围
	 * @param fighter
	 * @return
	 */
	private boolean isInScope(Fighter fighter, Fighter target) {
		int distantx = Math.abs(fighter.getTile().getX() - target.getTile().getX());
		int distanty = Math.abs(fighter.getTile().getY() - target.getTile().getY());
		int distant = Math.max(distantx, distanty);
		if (distant <= fighter.getAtkScope()) {
			return true;
		}
		return false;
	}

	public Integer getReadyTeamId() {
		return readyTeamId;
	}
}
