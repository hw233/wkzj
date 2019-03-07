package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.module.recruit.type.RecruitType;

/**
 * 聚仙阵信息
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name="recruit", type = DBQueueType.IMPORTANT)
public class Recruit extends Entity<Long> {
	private static final long serialVersionUID = -3886254117346431033L;

	/**
	 * 角色Id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 小聚仙使用次数
	 */
	@Column
	private byte smallUseNum;
	
	/**
	 * 小聚仙使用时间
	 */
	@Column
	private int smallUseTime;
	/**
	 * 大聚仙使用次数
	 */
	@Column
	private byte bigUseNum;
	/**
	 * 大聚仙使用时间
	 */
	@Column
	private int bigUseTime;
	
	/**
	 * 小聚仙总共使用次数
	 */
	@Column
	public long smallTotalUseNum;
	/**
	 * 大聚仙聚仙总共使用次数
	 */
	@Column
	public long bigTotalUseNum;
	
	/**
	 * 上次使用时间
	 */
	@Column
	public int lastUseTime;
	
	/**
	 * 大聚仙10次历史记录
	 * 次数_是否出5星(0:未出，1：出了)|...
	 */
	@Column
	private String bigHistory;
	/**
	 * 小聚仙聚仙10次历史记录
	 * 次数_是否出5星(0:未出，1：出了)|...
	 */
	@Column
	private String smallHistory;
	
	private ConcurrentHashMap<Long, Integer> historyBig = new ConcurrentHashMap<Long, Integer>();
	private ConcurrentHashMap<Long, Integer> historySmall = new ConcurrentHashMap<Long, Integer>();

	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long id) {
		this.actorId = id;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Recruit recruit = new Recruit();
		recruit.actorId = rs.getLong("actorId");
		recruit.smallUseNum = rs.getByte("smallUseNum");
		recruit.smallUseTime = rs.getInt("smallUseTime");
		recruit.bigUseNum = rs.getByte("bigUseNum");
		recruit.bigUseTime = rs.getInt("bigUseTime");
		recruit.smallTotalUseNum = rs.getLong("smallTotalUseNum");
		recruit.bigTotalUseNum = rs.getLong("bigTotalUseNum");
		recruit.lastUseTime = rs.getInt("lastUseTime");
		recruit.bigHistory = rs.getString("bigHistory");
		recruit.smallHistory = rs.getString("smallHistory");
		return recruit;
	}

	@Override
	protected void hasReadEvent() {
		Map<Long, Integer> mapbig = StringUtils.delimiterString2Long_IntMap(this.bigHistory);
		historyBig.putAll(mapbig);
		Map<Long, Integer> mapsmall = StringUtils.delimiterString2Long_IntMap(this.smallHistory);
		historyBig.putAll(mapsmall);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.smallUseNum);
		value.add(this.smallUseTime);
		value.add(this.bigUseNum);
		value.add(this.bigUseTime);
		value.add(this.smallTotalUseNum);
		value.add(this.bigTotalUseNum);
		value.add(this.lastUseTime);
		value.add(this.bigHistory);
		value.add(this.smallHistory);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		this.bigHistory = StringUtils.map2DelimiterString(historyBig, Splitable.ATTRIBUTE_SPLIT, Splitable.ELEMENT_DELIMITER);
		this.smallHistory = StringUtils.map2DelimiterString(historySmall, Splitable.ATTRIBUTE_SPLIT, Splitable.ELEMENT_DELIMITER);
	}


	public byte getSmallUseNum() {
		return smallUseNum;
	}

	public void setSmallUseNum(byte smallUseNum) {
		this.smallUseNum = smallUseNum;
	}

	public int getSmallUseTime() {
		return smallUseTime;
	}

	public void setSmallUseTime(int smallUseTime) {
		this.smallUseTime = smallUseTime;
	}

	public byte getBigUseNum() {
		return bigUseNum;
	}

	public void setBigUseNum(byte bigUseNum) {
		this.bigUseNum = bigUseNum;
	}

	public int getBigUseTime() {
		return bigUseTime;
	}

	public void setBigUseTime(int bigUseTime) {
		this.bigUseTime = bigUseTime;
	}

	public int getUseNum(byte type) {
		RecruitType recruitType = RecruitType.get(type);
		if (recruitType == RecruitType.SMALL){
			return getSmallUseNum();
		}
		return getBigUseNum();
	}

	public void setUseNum(byte type, byte num) {
		RecruitType recruitType = RecruitType.get(type);
		if (recruitType == RecruitType.SMALL){
			setSmallUseNum(num);
		} else {
			setBigUseNum(num);
		}
	}

	public void setUseTime(byte type) {
		RecruitType recruitType = RecruitType.get(type);
		if (recruitType == RecruitType.SMALL){
			setSmallUseTime(TimeUtils.getNow());
		} else {
			setBigUseTime(TimeUtils.getNow());
		}
	}
	
	public void timeReset(byte type) {
		RecruitType recruitType = RecruitType.get(type);
		if (recruitType == RecruitType.SMALL){
			setSmallUseTime(0);
		} else {
			setBigUseTime(0);
		}
	}
	
	public int getUseTime(byte type){
		RecruitType recruitType = RecruitType.get(type);
		if (recruitType == RecruitType.SMALL){
			return getSmallUseTime();
		}
		return getBigUseTime();
	}

	public static Recruit valueOf(long actorId) {
		Recruit recruit = new Recruit();
		recruit.actorId = actorId;
		return recruit;
	}
	
	public void setTotleUseNum(byte type) {
		RecruitType recruitType = RecruitType.get(type);
		if (recruitType == RecruitType.SMALL){
			this.smallTotalUseNum += 1;
		} else {
			this.bigTotalUseNum += 1;
		}
	}
	public long getTotleUseNum(byte type) {
		RecruitType recruitType = RecruitType.get(type);
		if (recruitType == RecruitType.SMALL){
			return this.smallTotalUseNum; 
		} else {
			return this.bigTotalUseNum;
		}
	}
	
	@Override
	protected void disposeBlob() {
		this.bigHistory = EMPTY_STRING;
	}
	
	public void putHistory(boolean star, byte type) {
		ConcurrentHashMap<Long, Integer> history = null;
		RecruitType recruitType = RecruitType.get(type);
		if (recruitType == RecruitType.SMALL){
			history = historySmall;
		} else {
			history = historyBig;
		}
		if (history.size() == 10) {
			history.clear();
		}
		long key = getTotleUseNum(type) % 10;
		int value = star ? 1:0;
		history.put(key, value);
		
	}
	
	public int historyNum(byte type) {
		ConcurrentHashMap<Long, Integer> history = null;
		RecruitType recruitType = RecruitType.get(type);
		if (recruitType == RecruitType.SMALL){
			history = historySmall;
		} else {
			history = historyBig;
		}
		Collection<Integer> values = history.values();
		int num = 0;
		for (int v : values) {
			if (v > 0) {
				num += 1;
			}
		}
		return num;
	}
	
	
}
