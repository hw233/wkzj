package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 吸灵室数据库实体
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author ludd
 *
 */
@TableName(name = "vampiir", type = DBQueueType.IMPORTANT)
public class Vampiir extends Entity<Long> {
	private static final long serialVersionUID = 2165352028545089890L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 等级
	 */
	@Column
	public int level;

	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	public static Vampiir valueOf(long actorId) {
		Vampiir vampiir = new Vampiir();
		vampiir.actorId = actorId;
		vampiir.level = 1;
		return vampiir;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Vampiir vampiir = new Vampiir();
		vampiir.actorId = rs.getLong("actorId");
		vampiir.level = rs.getInt("level");
		return vampiir;
	}

	@Override
	protected void hasReadEvent() {
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK){
		    value.add(this.actorId);
		}
	    value.add(this.level);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}
	
	public void reset() {
		this.level = 1;
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}

}
