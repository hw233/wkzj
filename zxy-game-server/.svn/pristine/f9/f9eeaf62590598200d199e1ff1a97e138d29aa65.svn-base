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
import com.jtang.core.utility.StringUtils;

/**
 * vip商店表
 * @author jianglf
 *
 */
@TableName(name="vipshop", type = DBQueueType.IMPORTANT)
public class VipShop extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -768455624862645279L;

	/**
	 * 玩家id
	 */
	@Column(pk=true)
	public long actorId;
	
	/**
	 * 今天购买的物品
	 * 商品id_次数|...
	 */
	@Column
	public String dayContent;
	
	/**
	 * 累计购买的物品
	 * 商品id_次数|...
	 */
	@Column
	public String allContent;
	
	/**
	 * 购买时间
	 */
	@Column
	public int buyTime;
	
	/**
	 * 今天购买的物品
	 */
	public Map<Integer,Integer> dayMap = new HashMap<>();
	
	/**
	 * 所有购买的商品
	 */
	public Map<Integer,Integer> allMap = new HashMap<>();
	
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
		VipShop vipShop = new VipShop();
		vipShop.actorId = rs.getLong("actorId");
		vipShop.dayContent = rs.getString("dayContent");
		vipShop.allContent = rs.getString("allContent");
		vipShop.buyTime = rs.getInt("buyTime");
		return vipShop;
	}

	@Override
	protected void hasReadEvent() {
		dayMap = StringUtils.delimiterString2IntMap(dayContent);
		
		allMap = StringUtils.delimiterString2IntMap(allContent);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if(containsPK){
			value.add(this.actorId);
		}
		value.add(this.dayContent);
		value.add(this.allContent);
		value.add(this.buyTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		dayContent = StringUtils.numberMap2String(dayMap);
		
		allContent = StringUtils.numberMap2String(allMap);
	}

	@Override
	protected void disposeBlob() {
		dayContent = EMPTY_STRING;
		allContent = EMPTY_STRING;
	}

	public static VipShop valueOf(long actorId) {
		VipShop vipShop = new VipShop();
		vipShop.actorId = actorId;
		return vipShop;
	}
	
	public int getDayNum(int id){
		if(dayMap.containsKey(id)){
			return dayMap.get(id);
		}
		return 0;
	}
	
	public int getAllNum(int id){
		if(allMap.containsKey(id)){
			return allMap.get(id);
		}
		return 0;
	}

}
