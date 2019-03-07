package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 自增id管理表
 * <pre>
 * 本表用于管理其他各表的自增主键
 * 如有其他需需要管理。需联系DB新增一行记录
 * --以下为db说明---------------------------
 * 多个服合并时，注意powerValue和maxId.
 * </pre>
 * @author ludd
 *
 */
@TableName(name="id", type = DBQueueType.NONE)
public class IdTable extends Entity<Long> {
	private static final long serialVersionUID = -1189616111675927366L;

	/**
	 * 自增主键
	 */
	@Column(pk = true)
	public long autoId;
	
	/**
	 * 游戏服id
	 */
	@Column
	public int serverId; 

	/**
	 * 需要管理自增Id的表名
	 */
	@Column
	public String tableName;
	
	/**
	 * 种子值
	 */
	@Column
	private long powerValue;
	
	/**
	 * 当前值
	 */
	@Column
	private long maxId;
	
	/**
	 * 计算好的偏移量
	 */
	private long powerOffset;
	
	private AtomicLong atomMaxId = new AtomicLong();
	
	/**
	 * 初始化
	 */
	public void init() {
		powerOffset = this.serverId * (long) Math.pow(2, this.powerValue);
		atomMaxId.set(this.maxId);
	}
	
	@Override
	public Long getPkId() {
		return 0L;
	}
	
	@Override
	public void setPkId(Long id) {
	}
	
	/**
	 * 主键++
	 * @return
	 */
	public synchronized long increasePK() {
		// 这里是否有线程安全问题
		return atomMaxId.incrementAndGet() + powerOffset;
	}
	
	public long getAtomMaxId() {
		return atomMaxId.get();
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		IdTable entity = new IdTable();
		entity.autoId = rs.getLong("autoId");
		entity.serverId = rs.getInt("serverId");
		entity.tableName = rs.getString("tableName");
		entity.powerValue = rs.getLong("powerValue");
		entity.maxId = rs.getLong("maxId");
		return entity;
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
		value.add(this.serverId);
		value.add(this.tableName);
		value.add(this.powerValue);
		value.add(this.maxId);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}

	@Override
	protected void disposeBlob() {
	}

}
