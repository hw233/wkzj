package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
/**
 * 登天塔最好记录
 *  <pre>
 * --以下为db说明---------------------------
 * 主键为autoId,每个角色最多有3条记录
 * </pre>
 * @author ludd
 *
 */
@TableName(name="bableRecord", type = DBQueueType.NONE)
public class BableRecord extends Entity<Long> implements Comparable<BableRecord>{
	private static final long serialVersionUID = -461793257185897180L;
	/**
	 * 自增id
	 */
	@Column(pk = true)
	private long autoId;
	
	/**
	 * 统计时间
	 */
	@Column
	public int statDate;
	
	/**
	 * 登天塔id
	 */
	@Column
	public int bableId;
	
	/**
	 * 角色Id
	 */
	@Column
	public long actorId;

	/**
	 * 最高楼层
	 */
	@Column
	public int maxFloor;
	/**
	 * 累计星星
	 */
	@Column
	public int maxStar;
	/**
	 * 到达最高楼层时间（毫秒）,用于排序
	 */
	@Column
	public long useTime;
	

	@Override
	public Long getPkId() {
		return this.autoId;
	}

	@Override
	public void setPkId(Long pk) {
		this.autoId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet resultset, int rowNum) throws SQLException {
		BableRecord bablerecord = new BableRecord();
		bablerecord.autoId = resultset.getLong("autoId");
		bablerecord.statDate = resultset.getInt("statDate");
		bablerecord.bableId = resultset.getInt("bableId");
		bablerecord.actorId = resultset.getLong("actorId");
		bablerecord.maxFloor = resultset.getInt("maxFloor");
		bablerecord.maxStar = resultset.getInt("maxStar");
		bablerecord.useTime = resultset.getLong("useTime");
		return bablerecord;
	}

	@Override
	protected void hasReadEvent() {

	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.autoId);
		}
		value.add(this.statDate);
		value.add(this.bableId);
		value.add(this.actorId);
		value.add(this.maxFloor);
		value.add(this.maxStar);
		value.add(this.useTime);

		return value;
	}

	@Override
	protected void beforeWritingEvent() {

	}

	public static BableRecord valueOf(long actorId, int bableId, int statDate) {
		BableRecord bableRecord = new BableRecord();
		bableRecord.actorId = actorId;
		bableRecord.bableId = bableId;
		bableRecord.statDate = statDate;
		return bableRecord;
	}
	
	@Override
	protected void disposeBlob() {
		
	}

	@Override
	public int compareTo(BableRecord o) {
		if (o.maxFloor > this.maxFloor) {
			return 1;
		} else if (o.maxFloor < this.maxFloor) {
			return -1;
		} else {
			if (o.maxStar > this.maxStar) {
				return 1;
			} else if (o.maxStar < this.maxStar) {
				return -1;
			} else {
				if (o.useTime < this.useTime) {
					return 1;
				} else {
					return -1;
				}
			}
		}
	}
}
