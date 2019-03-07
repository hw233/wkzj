package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * vip每日箱子表
 * @author jianglf
 *
 */
@TableName(name="vipBox", type = DBQueueType.IMPORTANT)
public class VipBox extends Entity<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8523204612913194639L;

	/**
	 * 玩家id
	 */
	@Column(pk = true)
	public long actorId;
	
	/**
	 * 是否已经领取奖励
	 * 0.未领取 1.已领取
	 */
	@Column
	public int isReward;
	
	/**
	 * 领取箱子时间
	 */
	@Column
	public int getTime;
	
	/**
	 * 开启箱子次数
	 */
	@Column
	public int openNum;
	
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
		VipBox vipBox = new VipBox();
		vipBox.actorId = rs.getLong("actorId");
		vipBox.isReward = rs.getInt("isReward");
		vipBox.getTime = rs.getInt("getTime");
		vipBox.openNum = rs.getInt("openNum");
		return vipBox;
	}

	@Override
	protected void hasReadEvent() {
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(actorId);
		}
		values.add(isReward);
		values.add(getTime);
		values.add(openNum);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
	}

	@Override
	protected void disposeBlob() {
	}
	
	public static VipBox valueOf(long actorId){
		VipBox vipBox = new VipBox();
		vipBox.actorId = actorId;
		return vipBox;
	}

}
