package com.jtang.gameserver.module.snatch.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.util.ConcurrentHashSet;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

/**
 * 积分角色管理类
 * <pre>
 * 用于维护:
 * >角色等级集合
 * >角色集分集合
 * </pre>
 * @author 0x737263
 *
 */
public class SnatchActorMap {

	/**
	 * <pre>
	 * 抢夺魂魄、碎片、金币时用到
	 * 部分活跃真实玩家的缓存,根据等级分类(可考虑定时重新构建)
	 * 1.格式:
	 * key=等级
	 * value=ConcurrentHashSet<角色id>
	 * </pre>
	 */
	private ConcurrentMap<Integer, ConcurrentHashSet<Long>> actorLevelMap;
	
	/**
	 * <pre>
	 * 构建排行榜时用到
	 * 每个分数和该分数下的玩家集合
	 * key:积分  ,value: 该段分数的角色id集合
	 * </pre>
	 */
	private ConcurrentMap<Integer, ConcurrentHashSet<Long>> scoreActorIdMap;
	
	public SnatchActorMap() {
		actorLevelMap = new ConcurrentLinkedHashMap.Builder<Integer, ConcurrentHashSet<Long>>().maximumWeightedCapacity(Short.MAX_VALUE).build();
		scoreActorIdMap = new ConcurrentLinkedHashMap.Builder<Integer, ConcurrentHashSet<Long>>().maximumWeightedCapacity(Short.MAX_VALUE).build();
	}
	
	/**
	 * 添加角色等级
	 * @param actorId
	 * @param oldLevel
	 * @param newLevel
	 */
	public void addActorLevel(long actorId, int oldLevel, int newLevel) {
		synchronized (actorLevelMap) {
			Set<Long> oldSet = actorLevelMap.get(oldLevel);
			if (oldSet != null && oldSet.size() > 0) {
				oldSet.remove(actorId);
			}

			ConcurrentHashSet<Long> newSet = actorLevelMap.get(newLevel);
			if (newSet == null) {
				newSet = new ConcurrentHashSet<Long>();
				actorLevelMap.putIfAbsent(newLevel, newSet);
			}
			newSet.add(actorId);
		}
	}
	
	/**
	 * 根据等级获取角色id集合
	 * @param level	等级
	 * @return
	 */
	public Set<Long> getLevelActorIds(int level) {
		Set<Long> set = actorLevelMap.get(level);
		if (set == null) {
			return new ConcurrentHashSet<>();
		}
		return set;
	}
	
	/**
	 * 添加角色积分
	 * @param actorId		角色idg
	 * @param oldScore		当前积分
	 * @param newScore		新增加的积分
	 */
	public void addActorScore(long actorId, int score, int addScore) {
		synchronized (scoreActorIdMap) {
			Set<Long> oldSet = scoreActorIdMap.get(score);
			if (oldSet != null && oldSet.size() > 0) {
				oldSet.remove(actorId);
			}

			int totalScore = score + addScore;
			ConcurrentHashSet<Long> actorSet = scoreActorIdMap.get(totalScore);
			if (actorSet == null) {
				actorSet = new ConcurrentHashSet<>();
				scoreActorIdMap.putIfAbsent(totalScore, actorSet);
			}
			actorSet.add(actorId);
		}
	}
	
	/**
	 * 根据积分获取角色id集合
	 * @param score		积分
	 * @return
	 */
	public List<Long> getScoreActorIds(int score) {
		Set<Long> set = scoreActorIdMap.get(score);
		if (set == null) {
			return Collections.emptyList();
		}
		
		List<Long> list = new ArrayList<>();
		list.addAll(set);
		return list;
	}

}
