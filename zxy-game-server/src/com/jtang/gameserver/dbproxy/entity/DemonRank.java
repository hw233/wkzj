package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 集众降魔上次排名记录
 *  <pre>
 * --以下为db说明---------------------------
 * 说明:
 * >主键为actorId,每个角色有一行记录
 * </pre>
 * @author ludd
 *
 */
@TableName(name="demonRank", type=DBQueueType.DEFAULT)
public class DemonRank extends Entity<Long> implements Comparable<DemonRank>{
	private static final long serialVersionUID = -8600570094738861182L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 上次排名
	 */
	@Column
	public int lastRank;
	
	/**
	 * 上次奖励积分
	 */
	@Column
	public long lastFeats;
	
	/**
	 * 上次阵营是否胜利 1：胜利，0：失败
	 */
	@Column
	public byte lastIsWin;
	
	/**
	 * 上次难度
	 */
	@Column
	public long lastDifficult;
	
	/**
	 * 是否已读
	 */
	@Column
	public byte isRead;
	
	/**
	 * 上次获得的积分
	 */
	@Column
	public int lastRewardScore;
	
	/**
	 * 上次第一名奖励
	 */
	@Column
	private String firstDemonRewardRecord;
	
	/**
	 * 上次功勋排名奖励
	 */
	@Column
	private String featsRankRewardRecord;
	
	/**
	 * 上次获胜阵营奖励
	 * TODO 本字段需求有变，改为：功勋值获得奖劢 详情demonConfig
	 * 胜利方的玩家读取 upFeatsReward
	 * 失败方的玩爱读取 downFeatsReward
	 */
	@Column
	private String winCampRewardRecord;
	
	/**
	 * 上次使用点券奖励
	 */
	@Column
	private String useTicketRewardRecord;
	
	
	/**
	 * 第一名奖励
	 */
	private List<RewardObject> firstDemonReward = new Vector<>();
	
	/**
	 * 功勋值排名奖励
	 */
	private List<RewardObject> featsRankReward = new Vector<>();
	
	/**
	 * 获胜正营奖励
	 */
	private List<RewardObject> featsTotalReward = new Vector<>();
	
	/**
	 * 使用点券产生的奖励
	 */
	private List<RewardObject> useTicketReward = new Vector<>();

	
	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet resultset, int rowNum) throws SQLException {
		DemonRank demonrank = new DemonRank();
		demonrank.actorId = resultset.getLong("actorId");
		demonrank.lastRank = resultset.getInt("lastRank");
		demonrank.lastFeats = resultset.getLong("lastFeats");
		demonrank.lastIsWin = resultset.getByte("lastIsWin");
		demonrank.lastDifficult = resultset.getLong("lastDifficult");
		demonrank.isRead = resultset.getByte("isRead");
		demonrank.lastRewardScore = resultset.getInt("lastRewardScore");
		demonrank.firstDemonRewardRecord = resultset.getString("firstDemonRewardRecord");
		demonrank.featsRankRewardRecord = resultset.getString("featsRankRewardRecord");
		demonrank.winCampRewardRecord = resultset.getString("winCampRewardRecord");
		demonrank.useTicketRewardRecord = resultset.getString("useTicketRewardRecord");
		return demonrank;

	}

	@Override
	protected void hasReadEvent() {
		parseRecord(this.firstDemonRewardRecord, this.firstDemonReward);
		parseRecord(this.featsRankRewardRecord, this.featsRankReward);
		parseRecord(this.winCampRewardRecord, this.featsTotalReward);
		parseRecord(this.useTicketRewardRecord, this.useTicketReward);

	}
	
	private void parseRecord(String str, List<RewardObject> list) {
		List<String[]> items = StringUtils.delimiterString2Array(this.firstDemonRewardRecord);
		for (String[] strings : items) {
			RewardObject obj = RewardObject.valueOf(strings);
			list.add(obj);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.lastRank);
		value.add(this.lastFeats);
		value.add(this.lastIsWin);
		value.add(this.lastDifficult);
		value.add(this.isRead);
		value.add(this.lastRewardScore);
		value.add(this.firstDemonRewardRecord);
		value.add(this.featsRankRewardRecord);
		value.add(this.winCampRewardRecord);
		value.add(this.useTicketRewardRecord);

		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		this.firstDemonRewardRecord = StringUtils.collection2SplitString(this.firstDemonReward, Splitable.ELEMENT_DELIMITER);
		this.featsRankRewardRecord = StringUtils.collection2SplitString(this.featsRankReward, Splitable.ELEMENT_DELIMITER);
		this.winCampRewardRecord = StringUtils.collection2SplitString(this.featsTotalReward, Splitable.ELEMENT_DELIMITER);
		this.useTicketRewardRecord = StringUtils.collection2SplitString(this.useTicketReward, Splitable.ELEMENT_DELIMITER);
	}
	
	public void setFeatsRankReward(List<RewardObject> featsRankReward) {
		this.featsRankReward.clear();
		this.featsRankReward.addAll(featsRankReward);
	}
	
	public void setFirstDemonReward(List<RewardObject> firstDemonReward) {
		this.firstDemonReward.clear();
		this.firstDemonReward.addAll(firstDemonReward);
	}
	
	public void setUseTicketReward(List<RewardObject> useTicketReward) {
		this.useTicketReward.clear();
		this.useTicketReward.addAll(useTicketReward);
	}
	
	public void setFeatsTotalReward(List<RewardObject> featsTotalReward) {
		this.featsTotalReward.clear();
		this.featsTotalReward.addAll(featsTotalReward);
	}

	public static DemonRank valueOf(long actorId) {
		DemonRank demonRank = new DemonRank();
		demonRank.actorId = actorId;
		return demonRank;
	}
	
	
	@Override
	public int compareTo(DemonRank o) {
		if (this.lastRank > o.lastRank) {
			return 1;
		}  else {
			return -1;
		}
	}

	public List<RewardObject> getFirstDemonReward() {
		return firstDemonReward;
	}

	public List<RewardObject> getFeatsRankReward() {
		return featsRankReward;
	}

	public List<RewardObject> getFeatsTotalReward() {
		return featsTotalReward;
	}

	public List<RewardObject> getUseTicketReward() {
		return useTicketReward;
	}
	@Override
	protected void disposeBlob() {
		this.firstDemonRewardRecord = EMPTY_STRING;
		this.featsRankRewardRecord = EMPTY_STRING;
		this.winCampRewardRecord = EMPTY_STRING;
		this.useTicketRewardRecord = EMPTY_STRING;
	}
	

}
