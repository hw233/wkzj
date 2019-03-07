package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RandomExprRewardObject;
import com.jtang.core.model.RandomRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;
/**
 * 集众降魔配置
 * @author ludd
 *
 */
@DataFile(fileName = "demonConfig")
public class DemonConfig implements ModelAdapter {

	/**
	 * 难度id
	 */
	private int difficultId;
	/**
	 * 对应demonMonsterConfg
	 */
	private int demonMonsterId;
	
	/**
	 * 击杀boss人的奖励
	 */
	private String killBossReward;
	/**
	 * 击杀boss正营奖励
	 */
	private String killBossCampReward;
	/**
	 * 击杀boss失败奖励
	 */
	private String failReward;
	/**
	 * /最强势力排名下限
	 */
	private int powerRankMin;
	/**
	 * 最强势力排名下限
	 */
	private int powerRankMax;
	
	/**
	 * 攻击玩家得到的功勋值上限
	 */
	private String featsOfPlayer;
	/**
	 * 攻击玩家得到的功勋值上限
	 */
	private int maxFeatsOfPlayer;
	/**
	 * 攻击boss得到的功勋值
	 */
	private String featsOfBoss;
	
	/**
	 * 攻击玩家最大次数
	 */
	private int attackPlayerNum;
	
	/**
	 * 攻击boss时间间隔（秒）
	 */
	private int interval;
	
	/**
	 * 攻击玩家点券数
	 */
	private int atkPlayerUseTicketNum;
	/**
	 * 攻击boss点券数
	 */
	private int atkBossUseTicketNum;
	
	/**
	 * 第一名额外奖励
	 */
	private String topPlayerReward;
	/**
	 * 第一名保底奖励
	 */
	private String topPlayerLeastReward;
	/**
	 * 第一名保底次数
	 */
	private int topPlayerLeastNum;
	
	/**
	 * 活动结束，获胜阵营奖励
	 */
	private String upFeatsReward;
	
	/**
	 * 活动结束，失败阵营奖励
	 */
	private String downFeatsReward;
	
	/**
	 * 消耗点券产生的奖励
	 */
	private String consumeTicketReward;
	
	/**
	 * 额外积分
	 */
	private String extraScore;
	/**
	 * 额外功勋值
	 */
	private String extraFeats;
	
	/**
	 * 攻击boss玩家每N次获得额外积分
	 */
	private int attackBossExtScore;
	/**
	 * 攻击boss玩家获得额外积分次数（例如每5次）
	 */
	private int attackBossExtScoreNum;
	/**
	 * 攻击boss玩家每N次获得额外积分最大次数
	 */
	private int attackBossExtScoreNumMax;
	@FieldIgnore
	private List<ExprRewardObject> killBossRewardObjects = new ArrayList<>();
	@FieldIgnore
	private List<RandomExprRewardObject> killBossCampRewardObjects = new ArrayList<>();
	@FieldIgnore
	private List<RandomExprRewardObject> failRewardObjects = new ArrayList<>();
	@FieldIgnore
	private List<RandomRewardObject> topPlayerRewardObjects = new ArrayList<>();
	@FieldIgnore
	private List<RewardObject> topPlayerLeastRewardObjects = new ArrayList<>();
	@FieldIgnore
	private List<RandomExprRewardObject> consumeTicketRewardObjects = new ArrayList<>();
	@FieldIgnore
	private List<ExprRewardObject> upFeatsRewardObjects = new ArrayList<>();
	@FieldIgnore
	private List<ExprRewardObject> downFeatsRewardObjects = new ArrayList<>();	
	
	@Override
	public void initialize() {
		killBossRewardObjects.clear();
		killBossCampRewardObjects.clear();
		failRewardObjects.clear();
		topPlayerRewardObjects.clear();
		topPlayerLeastRewardObjects.clear();
		upFeatsRewardObjects.clear();
		downFeatsRewardObjects.clear();
		consumeTicketRewardObjects.clear();
		List<String[]> winList = StringUtils.delimiterString2Array(killBossReward);
		for (String[] winstr : winList) {
			ExprRewardObject obj = ExprRewardObject.valueOf(winstr);
			killBossRewardObjects.add(obj);
		}
		winList = StringUtils.delimiterString2Array(killBossCampReward);
		for (String[] winstr : winList) {
			RandomExprRewardObject obj = RandomExprRewardObject.valueOf(winstr);
			killBossCampRewardObjects.add(obj);
		}
		List<String[]> topList = StringUtils.delimiterString2Array(topPlayerReward);
		for (String[] topstr : topList) {
			RandomRewardObject obj = RandomRewardObject.valueOf(topstr);
			topPlayerRewardObjects.add(obj);
		}
		List<String[]> failList = StringUtils.delimiterString2Array(failReward);
		for (String[] failstr : failList) {
			RandomExprRewardObject obj = RandomExprRewardObject.valueOf(failstr);
			failRewardObjects.add(obj);
		}
		List<String[]> topLeastList = StringUtils.delimiterString2Array(topPlayerLeastReward);
		for (String[] topLeastStr : topLeastList) {
			RewardObject obj = RewardObject.valueOf(topLeastStr);
			topPlayerLeastRewardObjects.add(obj);
		}
		List<String[]> up = StringUtils.delimiterString2Array(upFeatsReward);
		for (String[] upStr : up) {
			ExprRewardObject obj = ExprRewardObject.valueOf(upStr);
			upFeatsRewardObjects.add(obj);
		}

		List<String[]> down = StringUtils.delimiterString2Array(downFeatsReward);
		for(String[] downString : down) {
			ExprRewardObject obj = ExprRewardObject.valueOf(downString);
			downFeatsRewardObjects.add(obj);
		}
		
		List<String[]> ticketsUseRewards = StringUtils.delimiterString2Array(consumeTicketReward);
		for (String[] str : ticketsUseRewards) {
			RandomExprRewardObject obj = RandomExprRewardObject.valueOf(str);
			consumeTicketRewardObjects.add(obj);
		}
		
		this.killBossReward = null;
		this.killBossCampReward = null;
		this.topPlayerReward = null;
		this.failReward = null;
		this.topPlayerLeastReward = null;
		this.upFeatsReward = null;
		this.downFeatsReward = null;
		this.consumeTicketReward = null;
	}
	

	public int getDifficultId() {
		return difficultId;
	}

	public int getDemonMonsterId() {
		return demonMonsterId;
	}

	public int getPowerRankMin() {
		return powerRankMin;
	}

	public int getPowerRankMax() {
		return powerRankMax;
	}

	public int getAttackPlayerNum() {
		return attackPlayerNum;
	}
	
	public int getAtkBossUseTicketNum() {
		return atkBossUseTicketNum;
	}

	public int getInterval() {
		return interval;
	}

	public int getAtkPlayerUseTicketNum() {
		return atkPlayerUseTicketNum;
	}
	
	public int getMaxFeatsOfPlayer() {
		return maxFeatsOfPlayer;
	}
	
	public String getFeatsOfBoss() {
		return featsOfBoss;
	}

	public List<RewardObject> getKillBossRewardObjects(int level) {
		List<RewardObject> result = new ArrayList<>();
		if (killBossRewardObjects.size() > 0) {
			for (int i = 0; i < killBossRewardObjects.size(); i++) {
				ExprRewardObject rewardObject = killBossRewardObjects.get(i);
				result.add(rewardObject.clone(level));
			}
		}
		return result;
	}
	public List<RewardObject> getKillBossCampRewardObjects(int level) {
		List<RewardObject> list = new ArrayList<>();
		List<RewardObject> result = new ArrayList<>();
		if (killBossCampRewardObjects.size() > 0) {
			Map<Integer, Integer> rate = new HashMap<Integer, Integer>();
			for (int i = 0; i < killBossCampRewardObjects.size(); i++) {
				RandomExprRewardObject rewardObject = killBossCampRewardObjects.get(i);
				rewardObject = rewardObject.clone();
				rewardObject.calculateNum(level);
				result.add(rewardObject);
				rate.put(i, rewardObject.rate);
			}
			Integer index = RandomUtils.randomHit(1000, rate);
			if(index !=null){
				list.add(result.get(index));
			}
		}
		return list;
	}
	
	public List<RewardObject> getFailRewardObjects(int level) {
		List<RewardObject> list = new ArrayList<>();
		List<RewardObject> result = new ArrayList<>();
		if (failRewardObjects.size() > 0) {
			Map<Integer, Integer> rate = new HashMap<Integer, Integer>();
			for (int i = 0; i < failRewardObjects.size(); i++) {
				RandomExprRewardObject rewardObject = failRewardObjects.get(i);
				rewardObject = rewardObject.clone();
				rewardObject.calculateNum(level);
				result.add(rewardObject);
				rate.put(i, rewardObject.rate);
			}
			Integer index = RandomUtils.randomHit(1000, rate);
			if(index != null){
				list.add(result.get(index));
			}
		}
		return list;
	}
	
	public int getTopPlayerLeastNum() {
		return topPlayerLeastNum;
	}
	
	public List<RewardObject> getTopPlayerLeastRewardObjects() {
		List<RewardObject> list = new ArrayList<>();
		list.addAll(topPlayerLeastRewardObjects);
		return list;
	}
	
	public List<RewardObject> getTopPlayerRewardObjects() {
		Map<Integer, Integer> rate = new HashMap<Integer, Integer>();
		for (int i = 0; i < topPlayerRewardObjects.size(); i++) {
			RandomRewardObject rewardObject = topPlayerRewardObjects.get(i);
			rate.put(i, rewardObject.rate);
		}
		Integer index = RandomUtils.randomHit(1000, rate);
		List<RewardObject> list = new ArrayList<>();
		if(index != null){
			list.add(topPlayerRewardObjects.get(index));
		}
		return list;
	}
	
	/**
	 * 活动结束，获胜阵营奖励列表
	 * @return
	 */
	public List<RewardObject> getUpFeatsRewardObjects(int level) {
		List<RewardObject> list = new ArrayList<>();
		for (ExprRewardObject rewardObject : this.upFeatsRewardObjects) {
			RewardObject temp = rewardObject.clone(level);
			list.add(temp);
		}
		return list;
	}
	
	/**
	 * 活动结束，失败阵营奖励列表
	 * @return
	 */
	public List<RewardObject> getDownFeatsRewardObjects(int level) {
		List<RewardObject> list = new ArrayList<>();
		for (ExprRewardObject rewardObject : this.downFeatsRewardObjects) {
			RewardObject temp = rewardObject.clone(level);
			list.add(temp);
		}
		return list;
	}
	
	public List<RewardObject> getConsumeTicketRewards(int value) {
		List<RandomExprRewardObject> list = new ArrayList<>();
		for (RandomExprRewardObject rewardObject : this.consumeTicketRewardObjects) {
			list.add(rewardObject.clone());
		}
		
		Map<Integer, Integer> rate = new HashMap<Integer, Integer>();
		for (int i = 0; i < consumeTicketRewardObjects.size(); i++) {
			RandomExprRewardObject rewardObject = consumeTicketRewardObjects.get(i);
			rate.put(i, rewardObject.rate);
		}
		Integer index = RandomUtils.randomHit(1000, rate);
		if (index == null) {
			return new ArrayList<>();
		}
		
		RandomExprRewardObject resultObj = list.get(index);
		resultObj.calculateNum(value);
		List<RewardObject> resultList = new ArrayList<>();
		resultList.add(resultObj);
		return resultList;
	}
	/**
	 * 获取额外功勋值
	 * @param num 相差人数
	 * @param feats 固定功勋值
	 * @return
	 */
	public int getExtraFeats(int num, long feats) {
		if (num <= 0 || feats <= 0) {
			return 0;
		}
		return FormulaHelper.executeInt(this.extraFeats, num, feats);
	}
	
	/**
	 * 获取额外积分
	 * @param num 相差人数
	 * @return
	 */
	public int getExtraScore(int num) {
		if (num <= 0) {
			return 0;
		}
		return FormulaHelper.executeCeilInt(this.extraScore, num);
	}
	
	public int getFeatsOfPlayer(long feats) {
		return FormulaHelper.executeCeilInt(this.featsOfPlayer, feats);
	}
	
	
	public int getAttackBossExtScore() {
		return attackBossExtScore;
	}
	
	public int getAttackBossExtScoreNum() {
		return attackBossExtScoreNum;
	}
	
	public int getAttackBossExtScoreNumMax() {
		return attackBossExtScoreNumMax;
	}

}
