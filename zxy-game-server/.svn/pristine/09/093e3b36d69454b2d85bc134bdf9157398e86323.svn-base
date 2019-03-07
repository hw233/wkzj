package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.service.TrialCaveService;

/**
 * 试炼洞信息表
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author vinceruan
 */
@TableName(name="trialcave", type = DBQueueType.IMPORTANT)
public class TrialCave extends Entity<Long> {
	private static final long serialVersionUID = -3116763230909163906L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	long actorId;
	
	/**
	 * 关卡1已经试炼的次数
	 */
	@Column
	public int ent1trialed;
	
	/**
	 * 关卡1最后一次试炼的时间
	 */
	@Column
	public int ent1LastAckTime;

	/**
	 * 关卡2已经试炼的次数
	 */
	@Column
	public int ent2trialed;
	
	/**
	 * 关卡2最后一次试炼的时间
	 */
	@Column
	public int ent2LastAckTime;
	
	/**
	 * 试炼重置次数
	 */
	@Column
	public int trialResetCount;
	
	/**
	 * 最后一次重置试炼次数的时间
	 */
	@Column
	public int trialLastResetTime;
	
	/**
	 * 活动期间总共试炼的次数
	 */
	@Column
	public int totalTrialCount;
	
	/**
	 * 活动结束时间
	 */
	@Column
	public int trialEndTime;
	
	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}
	
	public static TrialCave valueOf(long actorId) {
		TrialCave cave = new TrialCave();
		cave.setPkId(actorId);
		cave.ent1trialed = 0;
		cave.ent1LastAckTime = 0;
		cave.ent2trialed = 0;
		cave.ent2LastAckTime = 0;
		cave.trialResetCount = 0;
		cave.trialLastResetTime = 0;
		cave.totalTrialCount = 0;
		cave.trialEndTime = 0;
		return cave;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		TrialCave trialcave = new TrialCave();
		trialcave.actorId = rs.getLong("actorId");
		trialcave.ent1trialed = rs.getInt("ent1trialed");
		trialcave.ent1LastAckTime = rs.getInt("ent1LastAckTime");
		trialcave.ent2trialed = rs.getInt("ent2trialed");
		trialcave.ent2LastAckTime = rs.getInt("ent2LastAckTime");
		trialcave.trialResetCount = rs.getInt("trialResetCount");
		trialcave.trialLastResetTime = rs.getInt("trialLastResetTime");
		trialcave.totalTrialCount = rs.getInt("totalTrialCount");
		trialcave.trialEndTime = rs.getInt("trialEndTime");
		return trialcave;
	}

	@Override
	protected void hasReadEvent() {
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.ent1trialed);
		value.add(this.ent1LastAckTime);
		value.add(this.ent2trialed);
		value.add(this.ent2LastAckTime);
		value.add(this.trialResetCount);
		value.add(this.trialLastResetTime);
		value.add(this.totalTrialCount);
		value.add(this.trialEndTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
	}
	
	public void increaseTrialCaveCount(byte entranceId) {
		if (entranceId == 1) {
			ent1trialed++;
			ent1LastAckTime = TimeUtils.getNow();
		} else {
			ent2trialed++;
			ent2LastAckTime = TimeUtils.getNow();
		}
		if (TrialCaveService.isInSpecifiedDay()) {
			totalTrialCount++;
		}
	}
	
	public void lastTrialCheck(int maxTrialNum, int entranceId) {
		if (entranceId == 1) {
			if (ent1trialed >= maxTrialNum) {
				ent1LastAckTime = 0;
			}
		} else {
			if (ent2trialed >= maxTrialNum) {
				ent2LastAckTime = 0;
			}
		}
	}
	/**
	 * 购买重置
	 */
	public void trialCaveReset () {
		this.ent1trialed = 0;
		this.ent1LastAckTime = 0;
		this.ent2trialed = 0;
		this.ent2LastAckTime = 0;
		this.trialResetCount++;
		this.trialLastResetTime = TimeUtils.getNow();
	}
	/**
	 * 零点重置
	 */
	public void trialCaveZeroUpdate() {
		this.ent1trialed = 0;
		this.ent1LastAckTime = 0;
		this.ent2trialed = 0;
		this.ent2LastAckTime = 0;
		this.trialResetCount = 0;
		this.totalTrialCount = 0;
		this.trialLastResetTime = TimeUtils.getNow();
	}
	
	/**
	 * 增加自定义整点重置
	 */
	public void trialCaveFixTimeReset() {
		this.ent1trialed = 0;
		this.ent1LastAckTime = 0;
		this.ent2trialed = 0;
		this.ent2LastAckTime = 0;
		this.trialLastResetTime = TimeUtils.getNow();
	}
	
	@Override
	protected void disposeBlob() {
		
	}
}
