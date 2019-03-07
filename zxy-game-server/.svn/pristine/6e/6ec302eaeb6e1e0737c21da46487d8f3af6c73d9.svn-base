package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;

/**
 * 欢乐摇奖活动
 * <pre>
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色对应一条记录
 * </pre>
 * @author lig
 *
 */
@TableName(name="ernie", type = DBQueueType.IMPORTANT)
public class Ernie extends Entity<Long> {
	
	private static final long serialVersionUID = 7289275816284341689L;

	/**
	 * 玩家id
	 */
	@Column(pk=true)
	public long actorId;
	
	/**
	 * 摇奖次数
	 */
	@Column
	public int ernieCount;
	
	/**
	 *上次抽奖情况
	 */
	@Column
	private String lastErnie;

	/*
	 *上次抽奖时间 
	 */
	@Column
	public int lastErnieTime;
	
	
	/**
	 * 保底次数
	 * key:物品id
	 * value:保底次数
	 */
	private int leastNum = 0;
	
	/**
	 * 上次抽奖情况
	 */
	public List<RewardObject> rewardList = new ArrayList<RewardObject>();
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		Ernie invite = new Ernie();
		invite.actorId = rs.getLong("actorId");
		invite.ernieCount = rs.getInt("ernieCount");
		invite.lastErnie = rs.getString("lastErnie");
		invite.lastErnieTime = rs.getInt("lastErnieTime");
		return invite;
	}

	@Override
	protected void hasReadEvent() {
		List<String> list1 = StringUtils.delimiterString2List(lastErnie, Splitable.ELEMENT_SPLIT);
		for (String str : list1) {
			String[] value = StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT);
			RewardObject e = RewardObject.valueOf(value);
			rewardList.add(e);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(actorId);
		}
		values.add(ernieCount);
		values.add(lastErnie);
		values.add(lastErnieTime);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		lastErnie = StringUtils.collection2SplitString(rewardList, Splitable.ELEMENT_DELIMITER);
	}

	@Override
	protected void disposeBlob() {
		lastErnie = EMPTY_STRING;
	}
	
	public static Ernie valueOf(long actorId) {
		Ernie ernie = new Ernie();
		ernie.actorId = actorId;
		ernie.ernieCount = 0;
		ernie.lastErnie = "";
		ernie.lastErnieTime = TimeUtils.getNow();
		ernie.leastNum = 0;
		return ernie;
	}
	
	
	
	/**
	 * 获取保底次数
	 * @return
	 */
	public int getLeastNum() {
		return leastNum;
	}
	
	/**
	 * 设置保底次数
	 * @param num
	 */
	public void setLeastNum(int num){
		this.leastNum = num;
	}

}
