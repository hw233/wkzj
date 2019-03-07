package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.HoleConfig;
import com.jtang.gameserver.module.hole.type.OpenType;

/**
 * 洞府
 * 
 * @author jianglf 
 * --以下为db说明--------------------------- 
 * 主键为id,由id表分配自增id
 */
@TableName(name = "hole", type = DBQueueType.DEFAULT)
public class Hole extends Entity<Long> {
	private static final long serialVersionUID = -6759322419278796259L;

	/**
	 * 自增id
	 */
	@Column(pk = true)
	private long id;

	/**
	 * 角色id
	 */
	@Column
	public long actorId;

	/**
	 * 邀请类型 0.自己 1.盟友邀请
	 */
	@Column
	public int type;

	/**
	 * 洞府id
	 */
	@Column
	public int holeId;

	/**
	 * 战斗信息 洞府关卡id_通关星数|洞府关卡id2_通关星数
	 */
	@Column
	public String fightInfo;

	/**
	 * 奖励信息 1星_是否领取|2星_是否领取|3星_是否领取
	 */
	@Column
	public String rewardInfo;

	/**
	 * 洞府刷新结束时间
	 */
	@Column
	public int flushTime;

	/**
	 * 我邀请的盟友ids type为0则为空 盟友id_是否通关(0未通关,1已通关)
	 */
	@Column
	public String acceptAllys;

	/**
	 * 邀请我的盟友id type为1则为空
	 */
	@Column
	public long ally;
	
	/**
	 * 盟友通关大礼包
	 */
	@Column
	public int packageGift;

	/**
	 * 战斗信息 key:洞府关卡id value:获得星数
	 */
	private Map<Integer, Integer> fights = new ConcurrentHashMap<Integer, Integer>();

	/**
	 * 奖励信息 key:星数 value:是否领取
	 */
	private Map<Integer, Integer> rewards = new ConcurrentHashMap<Integer, Integer>();

	/**
	 * 邀请的盟友 key:盟友id value:是否通关(0未通关,1已通关)
	 */
	private Map<Long, Integer> allys = new ConcurrentHashMap<Long, Integer>();
	

	@Override
	public Long getPkId() {
		return id;
	}

	@Override
	public void setPkId(Long pk) {
		this.id = pk;
	}

	public Map<Integer, Integer> getFightsMap() {
		return this.fights;
	}

	public Map<Integer, Integer> getRewardMap() {
		return this.rewards;
	}

	public Map<Long, Integer> getAllyMap() {
		return this.allys;
	}

	/**
	 * 创建新的洞府
	 * 
	 * @param holeVO
	 * @return
	 */
	public static Hole valueOf(long actorId, long otherActor, HoleConfig holeConfig, int type, int time) {
		Hole hole = new Hole();
		if (OpenType.ACTOR.getCode() == type) {// 自己开启
			hole.actorId = actorId;
			hole.ally = 0;
			hole.flushTime = TimeUtils.getNow() + holeConfig.time;
		} else {// 盟友开启
			hole.actorId = otherActor;
			hole.ally = actorId;
			hole.flushTime = TimeUtils.getNow()+time;
		}
		hole.holeId = holeConfig.holeId;
		for (Integer key : holeConfig.getAllRewardStar()) {
			hole.rewards.put(key, 0);
		}
		hole.type = type;
		hole.packageGift = 0;
		return hole;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Hole hole = new Hole();
		hole.id = rs.getLong("id");
		hole.actorId = rs.getLong("actorId");
		hole.type = rs.getInt("type");
		hole.holeId = rs.getInt("holeId");
		hole.fightInfo = rs.getString("fightInfo");
		hole.rewardInfo = rs.getString("rewardInfo");
		hole.flushTime = rs.getInt("flushTime");
		hole.acceptAllys = rs.getString("acceptAllys");
		hole.ally = rs.getLong("ally");
		hole.packageGift = rs.getInt("packageGift");
		return hole;
	}

	@Override
	protected void hasReadEvent() {

		if (StringUtils.isNotBlank(this.fightInfo)) {
			List<String[]> list = StringUtils.delimiterString2Array(fightInfo);
			for (String[] str : list) {
				fights.put(Integer.valueOf(str[0]), Integer.valueOf(str[1]));
			}
		}

		if (StringUtils.isNotBlank(this.rewardInfo)) {
			List<String[]> list = StringUtils.delimiterString2Array(rewardInfo);
			for (String[] str : list) {
				rewards.put(Integer.valueOf(str[0]), Integer.valueOf(str[1]));
			}
		}

		if (StringUtils.isNotBlank(this.acceptAllys)) {
			List<String[]> list = StringUtils.delimiterString2Array(acceptAllys);
			for (String[] str : list) {
				allys.put(Long.valueOf(str[0]), Integer.valueOf(str[1]));
			}
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<Object>();
		if (containsPK) {
			value.add(id);
		}
		value.add(actorId);
		value.add(type);
		value.add(holeId);
		value.add(fightInfo);
		value.add(rewardInfo);
		value.add(flushTime);
		value.add(acceptAllys);
		value.add(ally);
		value.add(packageGift);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		this.rewardInfo = StringUtils.numberMap2String(rewards);
		this.fightInfo = StringUtils.numberMap2String(fights);
		this.acceptAllys = StringUtils.numberMap2String(allys);
	}

	@Override
	protected void disposeBlob() {
		fightInfo = EMPTY_STRING;
		rewardInfo = EMPTY_STRING;
		acceptAllys = EMPTY_STRING;
	}
	
	public boolean aviable() {
		return this.flushTime > TimeUtils.getNow();
	}
}
