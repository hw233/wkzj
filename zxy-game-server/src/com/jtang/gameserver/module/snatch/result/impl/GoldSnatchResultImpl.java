package com.jtang.gameserver.module.snatch.result.impl;

import static com.jiatang.common.GameStatusCodeConstant.SNATCH_ENEMY_GOLD_LIMIT;
import static com.jtang.gameserver.module.snatch.type.SnatchEnemyType.ACTOR;
import static com.jtang.gameserver.module.snatch.type.SnatchEnemyType.ROBOT;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.gameserver.dataconfig.model.SnatchConfig;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.goods.constant.GoodsRule;
import com.jtang.gameserver.module.snatch.facade.SnatchActorFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchEnemyFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchRobotFacade;
import com.jtang.gameserver.module.snatch.helper.SnatchHelper;
import com.jtang.gameserver.module.snatch.model.SnatchEnemyVO;
import com.jtang.gameserver.module.snatch.model.SnatchVO;
import com.jtang.gameserver.module.snatch.result.SnatchResult;
import com.jtang.gameserver.module.snatch.result.SnatchResultContext;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;

/**
 * 抢夺金币,结果解析
 * @author 0x737263
 *
 */
@Component
public class GoldSnatchResultImpl implements SnatchResult {

	@Autowired
	SnatchResultContext context;
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	SnatchRobotFacade snatchRobotFacade;
	@Autowired
	SnatchEnemyFacade snatchEnemyFacade;
	@Autowired
	SnatchActorFacade snatchActorFacade;
	
	@PostConstruct
	private void init() {
		context.put(this);
	}
	
	@Override
	public Result checkSnatch(long actorId, long targetActorId, SnatchEnemyType enemyType, SnatchConfig config) {
		
		//检查自己是否被抢到了上限
//		if(snatchActorFacade.allowSnatchGold(actorId) == false) {
//			return Result.valueOf(SNATCH_ACTOR_GOLD_LIMIT);
//		}
		
		//检查至方是否被抢到了上限
		if (snatchActorFacade.isAllowSnatchGold(targetActorId, enemyType) == false) {
			return Result.valueOf(SNATCH_ENEMY_GOLD_LIMIT);
		}

		return Result.valueOf(); // 通过
	}
	
	@Override
	public SnatchVO calculateSnatch(long actorId, long targetActorId, SnatchEnemyType enemyType, WinLevel winLevel,SnatchConfig config) {

		int actorLevel = ActorHelper.getActorLevel(actorId);
		int targetActorLevel = SnatchHelper.getTargetLevel(targetActorId, enemyType);
		int score = config.getAwardScoreNum(winLevel, actorLevel, targetActorLevel);
		// 增加抢夺者的金币，扣除目标角色的金币
		int goldNum = getGold(winLevel, actorId, targetActorId, enemyType, config);
		if (winLevel.isWin()) {
			// 如果，对方是真实角色，则计算一下扣除金币的上限
			if (enemyType == SnatchEnemyType.ACTOR) {
				goldNum = snatchActorFacade.allowSnatchGoldNum(targetActorId, goldNum);
			}
		} else {
			goldNum = snatchActorFacade.allowSnatchGoldNum(actorId, goldNum);
		}
		return SnatchVO.valueOf(winLevel.isWin(), score, GoodsRule.GOODS_ID_GOLD, goldNum);
	}
	
	@Override
	public void rewardSnatch(long actorId, long targetActorId, SnatchEnemyType enemyType, WinLevel winLevel, SnatchVO snatchVo, SnatchConfig config) {
		
		//增加、奖励、扣除 双方的各种物品等
		if(winLevel.isWin()) {
			//给抢夺者加金币
			addActorGold(actorId, actorId, snatchVo.goodsNum);
			
			if (enemyType == SnatchEnemyType.ACTOR) {
				decreaseActorGold(actorId, targetActorId, snatchVo.goodsNum);
			} else {
				decreaseRobotGold(targetActorId, snatchVo.goodsNum);
			}
		} else {
			//扣除抢夺者的
			decreaseActorGold(actorId, actorId, snatchVo.goodsNum);
			
			//给被抢者添加金币
			if (enemyType == SnatchEnemyType.ACTOR) {
				addActorGold(actorId, targetActorId, snatchVo.goodsNum);
			} else {
				addRobotGold(targetActorId, snatchVo.goodsNum);
			}
			
		}
	}

	
	private int getGold(WinLevel winLevel, long actorId, long targetActorId, SnatchEnemyType enemyType, SnatchConfig config) {
		long gold = 0;
		Double percent = config.getSnatchGoldPercent(winLevel) * 0.01;
		if (winLevel.isWin()) {
			if (enemyType == ACTOR) {
				gold = actorFacade.getActor(targetActorId).gold;
			} else if (enemyType == ROBOT) {
				SnatchEnemyVO enemy = snatchRobotFacade.getSnatchEnemy(targetActorId);
				if (enemy != null) {
					gold = enemy.gold;
				}
			}
		} else {
			gold = actorFacade.getActor(actorId).gold;
		}
		return (int) Math.ceil(gold * percent);
	}
	
	/**
	 * 
	 * @param actorId			用于获取对手列表的角色id
	 * @param targetActorId		加减金币操作的角色id
	 * @param gold				金币数
	 * @param snatchType		抢夺类型
	 * @param star				星级
	 */
	private void addActorGold(long actorId, long targetActorId, int gold) {
		actorFacade.addGold(targetActorId, GoldAddType.SNATCH, gold);

		List<SnatchEnemyVO> cacheList = snatchEnemyFacade.getEnemyList(actorId, false);
		for (SnatchEnemyVO vo : cacheList) {
			if (vo.actorId == targetActorId) {
				vo.increase(gold);
			}
		}
	}
	
	private void addRobotGold(long actorId, int gold) {
		SnatchEnemyVO enemy = this.snatchRobotFacade.getSnatchEnemy(actorId);
		if (enemy != null) {
			enemy.increase(gold);
		}
	}
	
	/**
	 * 
	 * @param actorId			用于获取对手列表的角色id
	 * @param targetActorId		加减金币操作的角色id
	 * @param gold				金币数
	 * @param snatchType		抢夺类型
	 * @param star				星级
	 */
	private void decreaseActorGold(long actorId, long targetActorId, int gold) {
		actorFacade.decreaseGold(targetActorId, GoldDecreaseType.SNATCH_FAIL, gold);

		// 更新对手列表
		List<SnatchEnemyVO> cacheList = snatchEnemyFacade.getEnemyList(actorId, false);
		for (SnatchEnemyVO vo : cacheList) {
			if (vo.actorId == targetActorId) {
				vo.decreaseGold(gold);
			}
		}
	}
	
	private void decreaseRobotGold(long actorId, int gold) {
		SnatchEnemyVO enemy = snatchRobotFacade.getSnatchEnemy(actorId);
		if (enemy != null) {
			enemy.decreaseGold(gold);
		}
	}

}
