package com.jtang.worldserver.module.crossbattle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.jiatang.common.crossbattle.model.ActorCrossData;
import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.worldserver.dataconfig.model.CrossBattleRankConfig;
import com.jtang.worldserver.dataconfig.service.CrossBattleRankService;
import com.jtang.worldserver.module.crossbattle.type.CrossBattleResult;

public class ServerCrossData {
	/**
	 * 服务器id
	 */
	private int serverId;
	/**
	 * 总伤害
	 */
	private long totalHurt;
	
	/**
	 * 组id
	 */
	private int groupId;
	
	/**
	 * 角色跨服战数据
	 */
	private Map<Long, ActorCrossData> actorCrossDataMap;
	
	/**
	 * 内部默认排名
	 */
	private List<ActorCrossData> attributeRankList;
	
	/**
	 * 日结束战斗结果
	 */
	private CrossBattleResult crossBattleResult = CrossBattleResult.NONE;
	
	private int otherServerId;
	
	/**
	 * 服务器排名
	 */
	private int serverScoreRank;
 
	public ServerCrossData(int serverId, long totalHurt, Map<Long, ActorCrossData> actorCrossDataMap, int groupId, int otherServerId) {
		super();
		this.serverId = serverId;
		this.totalHurt = totalHurt;
		this.actorCrossDataMap = actorCrossDataMap;
		attributeRankList = new ArrayList<>();
		List<ActorCrossData> list = new ArrayList<>();
		for (ActorCrossData actorCrossData : actorCrossDataMap.values()) {
			list.add(actorCrossData);
		}
		Collections.sort(list, new Comparator<ActorCrossData>() {

			@Override
			public int compare(ActorCrossData o1, ActorCrossData o2) {
				CrossBattleRankConfig cfg = CrossBattleRankService.get(o1.powerRank);
				int totalValue1 = cfg == null? 0 : cfg.getTotalValueExpr(o1.getTotalAttribute(AttackerAttributeKey.ATK),
						o1.getTotalAttribute(AttackerAttributeKey.HP_MAX), o1.getTotalAttribute(AttackerAttributeKey.DEFENSE));
				cfg = CrossBattleRankService.get(o2.powerRank);
				int totalValue2 = cfg == null? 0 : cfg.getTotalValueExpr(o2.getTotalAttribute(AttackerAttributeKey.ATK),
						o2.getTotalAttribute(AttackerAttributeKey.HP_MAX), o2.getTotalAttribute(AttackerAttributeKey.DEFENSE));
				if (totalValue1 > totalValue2) {
					return -1;
				} else if (totalValue1 < totalValue2) {
					return 1;
				}
				return 0;
			}
		});
		
		for (ActorCrossData actorCrossData : list) {
			attributeRankList.add(actorCrossData);
		}
		
		this.groupId = groupId;
		this.otherServerId = otherServerId;
	}

	public int getServerId() {
		return serverId;
	}

	public long getTotalHurt() {
		return totalHurt;
	}

	public Map<Long, ActorCrossData> getActorCrossDataMap() {
		return actorCrossDataMap;
	}
	
	public ActorCrossData getActorCrossData(long actorId) {
		return actorCrossDataMap.get(actorId);
	}
	
	/**
	 * 判断玩家是否能受伤害
	 * @param actorId
	 * @return
	 */
	public boolean canHurt(long actorId) {
		ActorCrossData actorCrossData = getActorCrossData(actorId);
		if (actorCrossData.isDead()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 记录服务器总伤害
	 */
	public void updateHurt(int actorBaseHurt, int extActorHurt) {
		totalHurt += actorBaseHurt;
		updateExtHurt(extActorHurt);
	}
	
	/**
	 * 更新额外伤害
	 * @param value
	 */
	public void updateExtHurt(int value) {
		totalHurt += value;
	}
	
	public List<ActorCrossData> getAttributeRankList() {
		return attributeRankList;
	}
	public void setCrossBattleResult(CrossBattleResult crossBattleResult) {
		this.crossBattleResult = crossBattleResult;
	}
	
	public CrossBattleResult getCrossBattleResult() {
		return crossBattleResult;
	}
	
	public int getGroupId() {
		return groupId;
	}
	
	public int getOtherServerId() {
		return otherServerId;
	}
	
	public void setServerScoreRank(int serverScoreRank) {
		this.serverScoreRank = serverScoreRank;
	}
	public int getServerScoreRank() {
		return serverScoreRank;
	}
	
	
}
