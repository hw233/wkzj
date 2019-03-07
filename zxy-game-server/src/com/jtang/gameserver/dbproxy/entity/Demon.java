package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
/**
 * 集众降魔
 * <pre>
 * --以下为db说明---------------------------
 * 说明:
 * >主键为actorId,每个角色有一行记录
 * </pre>
 * @author ludd
 *
 */
@TableName(name="demon", type=DBQueueType.IMPORTANT)
public class Demon extends Entity<Long> {
	private static final long serialVersionUID = 3610514194696935233L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 积分
	 */
	@Column
	public long score;

	/**
	 * 获得第一名次数
	 */
	@Column
	public int topPlayerNum;
	
	
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet resultset, int rowNum) throws SQLException {
		Demon demon = new Demon();
		demon.actorId = resultset.getLong("actorId");
		demon.score = resultset.getLong("score");
		demon.topPlayerNum = resultset.getInt("topPlayerNum");
		return demon;
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
		value.add(this.score);
		value.add(this.topPlayerNum);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {

	}

	public static Demon valueOf(long actorId) {
		Demon demon = new Demon();
		demon.actorId = actorId;
		return demon;
	}
	
	@Override
	protected void disposeBlob() {
		
	}

	
	
	

}
