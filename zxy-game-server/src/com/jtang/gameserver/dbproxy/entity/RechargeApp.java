package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@TableName(name="rechargeApp", type = DBQueueType.IMPORTANT)
public class RechargeApp extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1640660504904366619L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	public long actorId;
	
	/**
	 * 充值记录
	 * 充值金额_充值次数|...
	 */
	@Column
	public String recharge;
	
	public Map<Integer,Integer> rechargeMap = new HashMap<>();
	
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
		RechargeApp rechargeApp = new RechargeApp();
		rechargeApp.actorId = rs.getLong("actorId");
		rechargeApp.recharge = rs.getString("recharge");
		return rechargeApp;
	}

	@Override
	protected void hasReadEvent() {
		rechargeMap = StringUtils.delimiterString2IntMap(recharge);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(this.actorId);
		}
		values.add(this.recharge);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		this.recharge = StringUtils.map2DelimiterString(rechargeMap,Splitable.ATTRIBUTE_SPLIT,Splitable.ELEMENT_DELIMITER);
	}

	@Override
	protected void disposeBlob() {
		this.recharge = EMPTY_STRING;
	}

	public static RechargeApp valueOf(long actorId) {
		RechargeApp rechargeApp = new RechargeApp();
		rechargeApp.actorId = actorId;
		return rechargeApp;
	}

	public boolean isRecharge(int money) {
		return rechargeMap.containsKey(money);
	}

}
