package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.service.LuckStarService;

@TableName(name = "luckstar", type = DBQueueType.IMPORTANT)
public class LuckStar extends Entity<Long> {
	private static final long serialVersionUID = -1658757920244001378L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	public long actorId;

	/**
	 * 上一次刷新的时间
	 */
	@Column
	public int flushTime;

	/**
	 * 可使用次数
	 */
	@Column
	public int useNum;

	/**
	 * 是否已经领取固定奖励 0.未领取 1.已领取
	 */
	@Column
	public int isGet;

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
		LuckStar luckStar = new LuckStar();
		luckStar.actorId = rs.getLong("actorId");
		luckStar.flushTime = rs.getInt("flushTime");
		luckStar.useNum = rs.getInt("useNum");
		luckStar.isGet = rs.getInt("isGet");
		return luckStar;
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<Object>();
		if (containsPK) {
			value.add(actorId);
		}
		value.add(flushTime);
		value.add(useNum);
		value.add(isGet);
		return value;
	}

	@Override
	protected void hasReadEvent() {

	}

	@Override
	protected void beforeWritingEvent() {

	}

	public static LuckStar valueOf(long actorId) {
		LuckStar luckStar = new LuckStar();
		luckStar.actorId = actorId;
		luckStar.flushTime = TimeUtils.getNow();
		luckStar.useNum = LuckStarService.getLuckStarConfig().maxStar;
		luckStar.isGet = 0;
		return luckStar;
	}

	public boolean isGet() {
		return isGet == 0 && useNum == 0;
	}

	public boolean isAddUseNum(int configFlushTime, int maxStar) {
		int now = TimeUtils.getNow();
		int num = (now - flushTime) / configFlushTime;
		if (num == 0) {
			return false;
		}
		if (num + useNum > maxStar) {
			useNum = maxStar;
		} else {
			useNum += num;
		}
		flushTime = now - ((now - flushTime) % configFlushTime);
		return true;
	}

	public void costUseNum() {
		useNum -= 1;
		flushTime = TimeUtils.getNow();
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}

}
