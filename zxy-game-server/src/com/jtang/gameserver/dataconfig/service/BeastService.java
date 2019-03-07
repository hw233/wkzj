package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RandomExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.BeastGlobalConfig;
import com.jtang.gameserver.dataconfig.model.BeastMonsterConfig;
import com.jtang.gameserver.dataconfig.model.MonsterConfig;
import com.jtang.gameserver.dbproxy.entity.Beast;

/**
 *年兽service类
 * @author lig
 */
@Service
public class BeastService extends ServiceAdapter {
	
	public static BeastGlobalConfig BEAST_GLOBAL_CONFIG = new BeastGlobalConfig();
	
	public static BeastMonsterConfig BEAST_MONSTER_CONFIG = new BeastMonsterConfig();
	
	public static int startTime;

	public static int endTime;
	
	@Override
	public void clear() {
		BEAST_GLOBAL_CONFIG = new BeastGlobalConfig();
		BEAST_MONSTER_CONFIG = new BeastMonsterConfig();
		startTime = 0;
		endTime = 0;
	}
	
	
	@Override
	public void initialize() {
		List<BeastGlobalConfig> list = dataConfig.listAll(this, BeastGlobalConfig.class);
		for (BeastGlobalConfig beastGlobalConfig : list) {
			BEAST_GLOBAL_CONFIG = beastGlobalConfig;
		}
		List<BeastMonsterConfig> list1 = dataConfig.listAll(this, BeastMonsterConfig.class);
		for (BeastMonsterConfig beastMonsterConfig : list1) {
			BEAST_MONSTER_CONFIG = beastMonsterConfig;
		}
		Long start = BEAST_GLOBAL_CONFIG.openDateTime.getTime() / 1000L;
		startTime = start.intValue();
		Long end = BEAST_GLOBAL_CONFIG.closeDateTime.getTime() / 1000L;
		endTime = end.intValue();
	}
	
	public static Map<Integer, MonsterVO> getBoss(int totalLevel) {
		BeastMonsterConfig cfg = BEAST_MONSTER_CONFIG;
		if (cfg == null) {
			return null;
		}
		Map<Integer, Integer> monsterPossition = cfg.getMonsterList();
		Map<Integer,MonsterVO> monsters = new ConcurrentHashMap<Integer, MonsterVO>();
		for (Map.Entry<Integer, Integer> posistion : monsterPossition.entrySet()) {
			int monsterId = posistion.getKey();
			MonsterConfig monsterCfg = MonsterService.get(monsterId);
			if (monsterCfg == null) {
				continue;
			}
			MonsterVO mVO = new MonsterVO(monsterCfg);
			mVO = mVO.clone();
			mVO.setAtk(cfg.getMonsterAttack(totalLevel));
			mVO.setDefense(cfg.getMonsterDeffends(totalLevel));
			mVO.setHp(cfg.getMonsterHp(totalLevel));
			mVO.setMaxHp(mVO.getHp());
			monsters.put(posistion.getValue(), mVO);
		}
		return monsters;
	}
	
	public static List<RewardObject> getRewardList(boolean isFinalBlow, Beast beast, int actorLevel) {
		List<RewardObject> list = new ArrayList<RewardObject>();
		if (isFinalBlow) {
			//最后一击
			List<RewardObject> finalList = BEAST_GLOBAL_CONFIG.finalBlowRewardList;
			int index = RandomUtils.nextIntIndex(finalList.size());
			RewardObject newObject = finalList.get(index);
			list.add(0, newObject);
		}
		//必出物品
		List<ExprRewardObject> actMustList = BEAST_GLOBAL_CONFIG.actMustRewardList;
		int[] indexs = RandomUtils.uniqueRandom(BEAST_GLOBAL_CONFIG.actMustRewardNum, 0, actMustList.size() - 1);
		for (int index : indexs) {
			ExprRewardObject rewardObject = actMustList.get(index);
			list.add(rewardObject.clone(actorLevel));
		}
		//随机物品
		for (RandomExprRewardObject reReward : BEAST_GLOBAL_CONFIG.actProbRewardList) {
			if (RandomUtils.is1000Hit(reReward.rate)) {
				RandomExprRewardObject reward = reReward.clone();
				reward.calculateNum(actorLevel);
				list.add(reward);
			}
		}
		
		List<Integer> ids = BEAST_GLOBAL_CONFIG.actLeastRewardIdList;
		boolean needLeast = true;
		for (RewardObject rewardObject : list) {
			if (ids.contains(rewardObject.id) == true) {
				beast.setLeastNum(0);
				needLeast = false;
			}
		}
		if (needLeast == true) {
			int leastMin = BEAST_GLOBAL_CONFIG.leastMin;
			int leastMax = BEAST_GLOBAL_CONFIG.leastMax;
			if (leastGoods(beast, leastMin, leastMax) == true) {
				list.add(BeastService.getLeastRewardObject());
			}
		}
		return list;
	}
	
	/**
	 * 物品保底
	 * @param actorId
	 * @param goodsId
	 * @return
	 */
	public static boolean leastGoods(Beast beast, int leastMin, int leastMax) {
		if (leastMax < leastMin) {
			return false;
		}
		int attNum = beast.ackTimes;
		if (attNum < leastMin) {
			return false;
		}
		
		//实际攻击次数
		int roundAttNum = beast.getLeastNum();
		if (roundAttNum < leastMin) return false;
		leastMin = Math.max(leastMin, roundAttNum); 
		int leastNum = RandomUtils.nextInt(leastMin, leastMax);
		if (leastNum == 0) {
			return false;
		}
		
		if (roundAttNum % leastNum == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		int count = 3;
		Beast beast = Beast.valueOf(200100000L);
		beast.ackTimes = count;
		boolean isLeast = leastGoods(beast, 3, 5);
		System.out.println(isLeast);
	}
	
	public static RewardObject getLeastRewardObject() {
		List<RewardObject> leastObjects = BEAST_GLOBAL_CONFIG.actLeastRewardList;
		int index = RandomUtils.nextIntIndex(leastObjects.size());
		if (index > leastObjects.size() - 1) {
			return null;
		}
		return leastObjects.get(index);
	}
	
	
	public static boolean isInActBbossTime() {
		if (isOpen() == false) {
			return false;
		}
		
		int actStartTime = getActivityStartTime();
		int actEndTime = actStartTime + BEAST_GLOBAL_CONFIG.delayTime;
		if (DateUtils.isActiveTime(actStartTime, actEndTime) == false) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 活动结束时间
	 * @return
	 */
	public static int getActivityStartTime() {
		List<Integer> dateList = BEAST_GLOBAL_CONFIG.timeList;
		int nowHour = TimeUtils.getHour();
		int startHour = -1;
		for (int i = nowHour; i <= 23; i++) {
			if (dateList.contains(i)) {
				startHour = i;
				break;
			}
		}
		long fixTime = TimeUtils.getTodayFixTime(startHour);
		Long startTime = (fixTime / 1000L);
		return startTime.intValue();
	}
	
	public static int getActivityEndTime() {
		int actStartTime = getActivityStartTime();
		int actEndTime = actStartTime + BEAST_GLOBAL_CONFIG.delayTime;
		return actEndTime;
	}
	
	public static int getCountDownTime() {
		return BEAST_GLOBAL_CONFIG.coolDownTime;
	}
		
	public static boolean isOpen() {
		if (DateUtils.isActiveTime(startTime, endTime)) {
			return true;
		}
		return false;
	}
		
}
